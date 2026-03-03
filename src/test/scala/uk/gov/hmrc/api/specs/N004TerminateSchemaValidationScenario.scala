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
import play.api.http.Status.BAD_REQUEST
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.testData.TestDataNotification

class N004TerminateSchemaValidationScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N004 : Terminate Request_MDTP returns 400 with error response body to DWP on request schema validation failure"
  ) {

    val cases: Seq[(String, Seq[(String, String)], JsValue, ErrorResponseMessage)] = Seq(
      (
        "Error: nationalInsuranceNumber is invalid in request body",
        validHeaders,
        terminateNotificationPayload(nino = "WW2344Z"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error: nationalInsuranceNumber is missing in request body",
        validHeaders,
        terminateNotificationPayloadMissing("nationalInsuranceNumber"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error: nationalInsuranceNumber is empty in request body",
        validHeaders,
        terminateNotificationPayload(nino = ""),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error: universalCreditRecordType is invalid in request body",
        validHeaders,
        terminateNotificationPayload(recordType = "123Test"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error: universalCreditRecordType is empty in request body",
        validHeaders,
        terminateNotificationPayload(recordType = ""),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error: universalCreditRecordType is missing in request body",
        validHeaders,
        terminateNotificationPayloadMissing("universalCreditRecordType"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error: universalCreditAction is invalid in request body",
        validHeaders,
        terminateNotificationPayload(creditAction = "Delete"),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error: universalCreditAction is missing in request body",
        validHeaders,
        terminateNotificationPayloadMissing("universalCreditAction"),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error: universalCreditAction is empty in request body",
        validHeaders,
        terminateNotificationPayload(creditAction = ""),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error: liabilityStartDate is missing in request body",
        validHeaders,
        terminateNotificationPayloadMissing("liabilityStartDate"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "Error: liabilityStartDate has invalid format in request body",
        validHeaders,
        terminateNotificationPayload(startDate = "1234-0-1"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "Error: liabilityStartDate is empty in request body",
        validHeaders,
        terminateNotificationPayload(startDate = ""),
        constraintViolation("liabilityStartDate")
      )
    )

    cases.foreach { case (scenarioName, headers, payload, errorResponseMessage) =>
      Scenario(scenarioName) {

        Given("a request with invalid request body is sent")
        val apiResponse = apiService.postNotification(headers, payload)

        Then("MDTP returns HTTP status code 400 Bad Request to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe BAD_REQUEST
        }
        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe InvalidInput
        (responseBody \ "message").as[String] mustBe errorResponseMessage

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
