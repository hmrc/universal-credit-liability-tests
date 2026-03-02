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

package uk.gov.hmrc.api.specs

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.SERVICE_UNAVAILABLE
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.testData.*

class N005_HIPServiceUnavailableScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N005:MDTP successfully processes a valid UCL Notification received from DWP but returns 503 when HIP server is unavailable"
  ) {

    val cases: Seq[(String, JsValue, ErrorResponseCode, ErrorResponseMessage)] = Seq(
      (
        "Error:Insert Request_MDTP returns 503 to DWP when HIP server is unavailable",
        insertNotificationPayload(nino = ninoWithPrefix("XY503")),
        "SERVER_ERROR",
        "The 'misc/universal-credit/liability' API is currently unavailable"
      ),
      (
        "Error: Terminate Request_MDTP handles HIP server unavailable error",
        terminateNotificationPayload(nino = ninoWithPrefix("XY503")),
        "SERVER_ERROR",
        "The 'misc/universal-credit/liability' API is currently unavailable"
      )
    )

    cases.foreach { case (scenarioName, payload, errorResponseCode, errorResponseMessage) =>
      Scenario(scenarioName) {

        When("a valid UCL notification is sent by DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)
        System.out.println(
          "For Scenario " + scenarioName + " Error Response Body ==> " + Json.parse(apiResponse.body)
        )

        Then("MDTP returns HTTP status code 503 Service unavailable to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe SERVICE_UNAVAILABLE
        }

        And("Error response body must contain correct error details")
        val responseBody = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe errorResponseCode
        (responseBody \ "message").as[String] mustBe errorResponseMessage

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
