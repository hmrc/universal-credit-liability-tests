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

package uk.gov.hmrc.api.specs.notification

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class InsertBadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("400 BadRequest scenarios for 'Insert' record type") {

    val cases: Seq[(String, Seq[(String, String)], JsValue, Reason)] = Seq(
      (
        "UCL_TC_???: Invalid header: correlationID",
        headersInvalidCorrelationId,
        insertNotificationPayload(),
        constraintViolation("correlationId")
      ),
      (
        "UCL_TC_???: Invalid parameter: nationalInsuranceNumber",
        validHeaders,
        insertNotificationPayload(nino = "INVALID"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "UCL_TC_???: Invalid parameter: universalCreditRecordType",
        validHeaders,
        insertNotificationPayload(recordType = "INVALID"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_TC_???: Invalid parameter: universalCreditAction",
        validHeaders,
        notificationPayload(recordAction = "INVALID"),
        constraintViolation("universalCreditAction")
      ),
      (
        "UCL_TC_???: Invalid parameter: dateOfBirth",
        validHeaders,
        insertNotificationPayload(dateOfBirth = "INVALID"),
        constraintViolation("dateOfBirth")
      ),
      (
        "UCL_TC_005_0.2: Missing header: correlationId",
        headersMissingCorrelationId,
        insertNotificationPayload(),
        constraintViolation("correlationId")
      ),
      (
        "UCL_TC_???: Missing parameter: nationalInsuranceNumber",
        validHeaders,
        insertNotificationPayloadMissing("nationalInsuranceNumber"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "UCL_TC_???: Missing parameter: universalCreditRecordType",
        validHeaders,
        insertNotificationPayloadMissing("universalCreditRecordType"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_TC_???: Missing parameter: universalCreditAction",
        validHeaders,
        insertNotificationPayloadMissing("universalCreditAction"),
        constraintViolation("universalCreditAction")
      ),
      (
        "UCL_TC_???: Missing parameter: dateOfBirth",
        validHeaders,
        insertNotificationPayloadMissing("dateOfBirth"),
        constraintViolation("dateOfBirth")
      ),
      (
        "UCL_TC_???: Missing parameter: liabilityStartDate",
        validHeaders,
        insertNotificationPayloadMissing("liabilityStartDate"),
        constraintViolation("liabilityStartDate")
      )
//      (
//        "UCL_TC_001_0.5: Insert Invalid LCW/LCWRA details",
//        validHeaders,
//        insertPayload(recordType = "INVALID"),
//        constraintViolation("universalCreditRecordType")
//      ),
//      (
//        "UCL_TC_001_0.6: Insert Invalid UC details", // FIXME: this is identical to the above
//        validHeaders,
//        insertPayload(recordType = "INVALID"),
//        constraintViolation("universalCreditRecordType")
//      ),
//      (
//        "UCL_TC_002_0.3: Invalid Credit Action",
//        validHeaders,
//        notificationPayload(recordAction = "INVALID"),
//        constraintViolation("universalCreditAction")
//      ),
//      (
//        "UCL_TC_002_0.4: Invalid Date of Birth with Insert action",
//        validHeaders,
//        insertPayload(dateOfBirth = "INVALID"),
//        constraintViolation("dateOfBirth")
//      ),
//      (
//        "UCL_TC_002_0.5: Invalid Start Date with Insert action",
//        validHeaders,
//        insertPayload(startDate = "INVALID"),
//        constraintViolation("liabilityStartDate")
//      ),
//      (
//        "UCL_TC_002_0.6: Invalid NINO with Insert action",
//        validHeaders,
//        insertPayload(nino = "INVALID"),
//        constraintViolation("nationalInsuranceNumber")
//      ),
//      (
//        "UCL_TC_003_0.1: Empty Credit Record Type with Insert action",
//        validHeaders,
//        insertPayload(recordType = ""),
//        constraintViolation("universalCreditRecordType")
//      ),
//      (
//        "UCL_TC_003_0.2: Empty Credit Action with Insert action",
//        validHeaders,
//        notificationPayload(recordAction = ""),
//        constraintViolation("universalCreditAction")
//      ),
//      (
//        "UCL_TC_003_0.3: Empty Date of Birth with Insert action",
//        validHeaders,
//        insertPayload(dateOfBirth = ""),
//        constraintViolation("dateOfBirth")
//      ),
//      (
//        "UCL_TC_003_0.4:Empty Start Date with Insert action",
//        validHeaders,
//        insertPayload(startDate = ""),
//        constraintViolation("liabilityStartDate")
//      ),
//      (
//        "UCL_TC_003_0.5: Empty NINO with Insert action",
//        validHeaders,
//        insertPayload(nino = ""),
//        constraintViolation("nationalInsuranceNumber")
//      ),
    )

    cases.foreach { case (scenarioName, headers, payload, reason) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("a request is sent")

        println("\nPAYLOAD:")
        println(payload)

        val apiResponse = apiService.postNotification(headers, payload)

        Then("400 BadRequest should be returned")
        withClue(s"Expected 400, got ${apiResponse.status}. Body ${apiResponse.body}\n") {
          apiResponse.status mustBe Status.BAD_REQUEST
        }

        And("response body should contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)

        (responseBody \ "code").as[String] mustBe InvalidInput
        (responseBody \ "message").as[String] mustBe reason
      }
    }
  }
}
