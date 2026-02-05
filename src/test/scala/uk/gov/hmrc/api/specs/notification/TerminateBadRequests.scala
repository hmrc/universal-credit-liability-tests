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

  Feature("400 Bad Request scenarios for Terminate Record Type") {

    val cases: Seq[(String, JsValue, Reason)] = Seq(
      (
        "UCL_Terminate_TC_002_0.1: Invalid Credit Record Type LCW/LCWRA with Terminate action",
        invalidTerminationLCWLCWRARequest,
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_Terminate_TC_002_0.1: Invalid Credit Record Type UC  with Terminate action",
        invalidTerminationUCRequest,
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_Terminate_TC_002_0.2: Invalid Credit Action universalCreditAction",
        invalidTerminationActionRequest,
        constraintViolation("universalCreditAction")
      ),
      (
        "UCL_Terminate_TC_002_0.3: Invalid Start Date with Terminate action",
        invalidTerminateStartDate,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UCL_Terminate_TC_002_0.4: Invalid End Date with Terminate action",
        invalidTerminateEndDate,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UCL_Terminate_TC_002_0.5: Invalid NINO with Terminate action",
        invalidNINOTerminationRequest,
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "UCL_Terminate_TC_003_0.1: Empty Credit Record Type with Terminate action",
        emptyCreditRecordTypeTerminateRequest,
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UCL_Terminate_TC_003_0.2: Empty Credit Action with Terminate action",
        emptyCreditActionTerminationRequest,
        constraintViolation("universalCreditAction")
      ),
      (
        "UCL_Terminate_TC_003_0.3: Empty Start Date with Terminate action",
        emptyStartDateTerminationRequest,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UCL_Terminate_TC_003_0.4: Empty NINO with Terminate action",
        emptyNINOTerminationRequest,
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "UCL_Terminate_TC_003_0.5: Empty End Date with Terminate action",
        emptyEndDateTerminationRequest,
        constraintViolation("liabilityEndDate")
      )
    )

    cases.foreach { case (scenarioName, payload, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")
        val response = apiService.postNotification(validHeaders, payload)

        Then("400 BadRequest should be returned")
        response.status mustBe BAD_REQUEST

        Then("Response body should contain correct error details")
        val responseBody = Json.parse(response.body)

        (responseBody \ "code").as[String] mustBe InvalidInput
        (responseBody \ "message").as[String] mustBe expMessage
      }
    }
  }
}
