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
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class N002_Terminate_OriginatorIdValidationScenario
    extends BaseSpec
    with GuiceOneServerPerSuite
    with TestDataNotification {

  Feature(
    "UCL_TC_N002 : Terminate Request_MDTP returns 403 with error response body to DWP on request header - 'gov-uk-originator-id' validation failure"
  ) {

    val cases: Seq[(String, Seq[(String, String)], ErrorResponseCode, ErrorResponseMessage)] = Seq(
      (
        "Error: GovUkOriginatorId (Special Chars) is invalid in request header",
        headersInvalidCharsOriginatorId,
        "403.2",
        "Forbidden"
      ),
      (
        "Error: GovUkOriginatorId is missing in request header",
        headersMissingGovUkOriginatorId,
        "403.2",
        "Forbidden"
      ),
      (
        "Error: GovUkOriginatorId (Long) is invalid in request header",
        headersInvalidLongOriginatorId,
        "403.2",
        "Forbidden"
      ),
      (
        "Error: GovUkOriginatorId (Short) is invalid in request header",
        headersInvalidShortOriginatorId,
        "403.2",
        "Forbidden"
      ),
      (
        "Error: GovUkOriginatorId (Short) is empty in request header",
        headersEmptyOriginatorId,
        "403.2",
        "Forbidden"
      )
    )

    cases.foreach { case (scenarioName, headers, errorResponseCode, errorResponseMessage) =>
      Scenario(scenarioName) {

        Given("a request with invalid/missing/empty GovUkOriginatorId header is sent")
        val apiResponse = apiService.postNotification(headers, terminateNotificationPayload())
        System.out.println("For Scenario " + scenarioName + " Error Response Body ==> " + Json.parse(apiResponse.body))

        Then("MDTP returns HTTP status code 403 Forbidden to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe FORBIDDEN
        }
        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe errorResponseCode
        (responseBody \ "message").as[String] mustBe errorResponseMessage

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
