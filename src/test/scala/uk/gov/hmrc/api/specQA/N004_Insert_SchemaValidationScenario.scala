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

package uk.gov.hmrc.api.specQA

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.BAD_REQUEST
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class N004_Insert_SchemaValidationScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N004 : Insert Request_MDTP returns 400 with error response body to DWP on request schema validation failure"
  ) {

    val cases: Seq[(String, Seq[(String, String)], JsValue, ErrorResponseMessage)] = Seq(
      (
        "Error: nationalInsuranceNumber is invalid in request body",
        validHeaders,
        insertNotificationPayload(nino = "WW2344Z"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error: nationalInsuranceNumber is missing in request body",
        validHeaders,
        insertNotificationPayloadMissing("nationalInsuranceNumber"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error: nationalInsuranceNumber is empty in request body",
        validHeaders,
        insertNotificationPayload(nino = ""),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error: universalCreditRecordType is invalid in request body",
        validHeaders,
        insertNotificationPayload(recordType = "ABCD"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error: universalCreditRecordType is empty in request body",
        validHeaders,
        insertNotificationPayload(recordType = ""),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error: universalCreditRecordType is missing in request body",
        validHeaders,
        insertNotificationPayloadMissing("universalCreditRecordType"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error: universalCreditAction is invalid in request body",
        validHeaders,
        insertNotificationPayload(creditAction = "Add"),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error: universalCreditAction is missing in request body",
        validHeaders,
        insertNotificationPayloadMissing("universalCreditAction"),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error: universalCreditAction is empty in request body",
        validHeaders,
        insertNotificationPayload(creditAction = ""),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error: dateOfBirth had invalid format in request body",
        validHeaders,
        insertNotificationPayload(dateOfBirth = "2020/12/01"),
        constraintViolation("dateOfBirth")
      ),
      (
        "Error: dateOfBirth is missing in request body",
        validHeaders,
        insertNotificationPayloadMissing("dateOfBirth"),
        constraintViolation("dateOfBirth")
      ),
      (
        "Error: dateOfBirth is empty in empty body",
        validHeaders,
        insertNotificationPayload(dateOfBirth = ""),
        constraintViolation("dateOfBirth")
      ),
      (
        "Error: liabilityStartDate is missing in request body",
        validHeaders,
        insertNotificationPayloadMissing("liabilityStartDate"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "Error: liabilityStartDate has invalid format in request body",
        validHeaders,
        insertNotificationPayload(startDate = "01-01-1990"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "Error: liabilityStartDate is empty in request body",
        validHeaders,
        insertNotificationPayload(startDate = ""),
        constraintViolation("liabilityStartDate")
      )
    )

    cases.foreach { case (scenarioName, headers, payload, expMessage) =>
      Scenario(scenarioName) {
        Given("Universal Credit Liability Notification API is up and running")
        // need to add code
        When("a request with invalid request body is sent")
        val apiResponse = apiService.postNotification(headers, payload)
        System.out.println("For Scenario " + scenarioName + " Error Response Body ==> " + Json.parse(apiResponse.body))

        Then("MDTP returns HTTP status code 400 Bad Request to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe BAD_REQUEST
        }
        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe InvalidInput
        (responseBody \ "message").as[String] mustBe expMessage

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
