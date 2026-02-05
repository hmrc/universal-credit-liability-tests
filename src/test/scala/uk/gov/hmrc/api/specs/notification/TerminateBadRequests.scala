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
import play.api.http.Status.BAD_REQUEST
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class TerminateBadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("400 BadRequest scenarios for Terminate Record Type") {

    val cases: Seq[(String, JsValue, Reason)] = Seq(
      (
        "UCL_Terminate_TC_002_0.1: Invalid Credit Record Type LCW/LCWRA",
        terminateNotificationPayload(recordType = "INVALID"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_Terminate_TC_002_0.1: Invalid Credit Record Type UC ",
        terminateNotificationPayload(recordType = "INVALID"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_Terminate_TC_002_0.2: Invalid Credit Action universalCreditAction",
        notificationPayload(recordAction = "INVALID"),
        constraintViolation("universalCreditAction")
      ),
      (
        "UCL_Terminate_TC_002_0.3: Invalid Start Date",
        terminateNotificationPayload(startDate = "INVALID"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "UCL_Terminate_TC_002_0.4: Invalid End Date",
        terminateNotificationPayload(endDate = "INVALID"),
        constraintViolation("liabilityEndDate")
      ),
      (
        "UCL_Terminate_TC_002_0.5: Invalid NINO",
        terminateNotificationPayload(nino = "INVALID"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "UCL_Terminate_TC_003_0.1: Empty Credit Record Type",
        terminateNotificationPayload(recordType = ""),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_Terminate_TC_003_0.2: Empty Credit Action",
        notificationPayload(recordAction = ""),
        constraintViolation("universalCreditAction")
      ),
      (
        "UCL_Terminate_TC_003_0.3: Empty Start Date",
        terminateNotificationPayload(startDate = ""),
        constraintViolation("liabilityStartDate")
      ),
      (
        "UCL_Terminate_TC_003_0.4: Empty NINO",
        terminateNotificationPayload(nino = ""),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "UCL_Terminate_TC_003_0.5: Empty End Date",
        terminateNotificationPayload(endDate = ""),
        constraintViolation("liabilityEndDate")
      ),
      (
        "UCL_Terminate_TC_???: Missing parameter nationalInsuranceNumber",
        terminateNotificationPayloadMissing("nationalInsuranceNumber"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "UCL_Terminate_TC_???: Missing parameter universalCreditRecordType",
        terminateNotificationPayloadMissing("universalCreditRecordType"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_Terminate_TC_???: Missing parameter universalCreditAction",
        terminateNotificationPayloadMissing("universalCreditAction"),
        constraintViolation("universalCreditAction")
      ),
      (
        "UCL_Terminate_TC_???: Missing parameter liabilityStartDate",
        terminateNotificationPayloadMissing("liabilityStartDate"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "UCL_Terminate_TC_???: Missing parameter liabilityEndDate",
        terminateNotificationPayloadMissing("liabilityEndDate"),
        constraintViolation("liabilityEndDate")
      )
    )

    cases.foreach { case (scenarioName, payload, expectedMessage) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("a request is sent")

        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("400 BadRequest should be returned")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe BAD_REQUEST
        }
        Then("response body should contain correct error details")
        val responseBody = Json.parse(apiResponse.body)

        (responseBody \ "code").as[String] mustBe InvalidInput
        (responseBody \ "message").as[String] mustBe expectedMessage
      }
    }
  }
}
