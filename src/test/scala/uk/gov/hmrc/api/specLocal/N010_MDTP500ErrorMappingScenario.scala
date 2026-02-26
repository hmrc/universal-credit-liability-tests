/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.api.specLocal

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.*

class N010_MDTP500ErrorMappingScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N010:MDTP error handling for downstream errors 400 and 401"
  ) {

    val cases: Seq[(String, JsValue, String, String)] = Seq(
      (
        "Error:Insert Request_MDTP returns 500 to DWP when HIP returns 400",
        insertNotificationPayload(recordType = "UC"),
        "500",
        "INTERNAL_SERVER_ERROR"
      ),
      (
        "Error: Terminate Request_MDTP returns 500 to DWP when HIP returns 401",
        terminateNotificationPayload(recordType = "LCW/LCWRA"),
        "500",
        "INTERNAL_SERVER_ERROR"
      )
    )

    cases.foreach { case (scenarioName, payload, errorCode, errorMessage) =>
      Scenario(scenarioName) {

        Given("a valid UCL notification is sent by DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)
        System.out.println(
          "For Scenario " + scenarioName + " Error Response Body ==> " + apiResponse.statusText
        )

        Then("MDTP returns HTTP status code 404 Not Found to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe INTERNAL_SERVER_ERROR
        }

        And("Success response body must be empty")
        apiResponse.body mustBe empty

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
