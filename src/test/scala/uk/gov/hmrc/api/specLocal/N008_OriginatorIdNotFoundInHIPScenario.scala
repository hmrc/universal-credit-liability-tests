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
import play.api.http.Status.FORBIDDEN
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.*

class N008_OriginatorIdNotFoundInHIPScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N008:HIP fails to process the request from MDTP when originatorId is not found and returns 403 to MDTP and MDTP cascades the response to DWP"
  ) {

    val cases: Seq[(String, JsValue, ErrorResponseCode, ErrorResponseMessage)] = Seq(
      (
        "Error:Insert Request_MDTP cascades the HTTP 403 status with error payload from HIP to DWP when originator Id is not found in HIP",
        insertNotificationPayload(nino = ninoWithPrefix("??")),
        "403.2",
        "FORBIDDEN"
      ),
      (
        "Error: Terminate Request_MDTP cascades the HTTP 403 to DWP when originator Id is not found in HIP",
        terminateNotificationPayload(nino = ninoWithPrefix("??")),
        "403.2",
        "FORBIDDEN"
      )
    )

    cases.foreach { case (scenarioName, payload, errorResponseCode, errorResponseMessage) =>
      Scenario(scenarioName) {

        Given("a valid UCL notification is sent by DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)
        System.out.println(
          "For Scenario " + scenarioName + " Error Response Body ==> " + Json.parse(apiResponse.body)
        )

        Then("MDTP returns HTTP status code 403 Forbidden to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe FORBIDDEN
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
