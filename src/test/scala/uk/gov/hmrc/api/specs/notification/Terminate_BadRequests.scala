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

import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class Terminate_BadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("400 Bad Request scenarios for Terminate Record Type") {

    val cases = Seq(
      (
        "UCL_Terminate_TC_002_0.1: Invalid Credit Record Type LCW/LCWRA with Terminate action",
        invalidTerminationLCWLCWRARequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_Terminate_TC_002_0.1: Invalid Credit Record Type UC  with Terminate action",
        invalidTerminationUCRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_Terminate_TC_002_0.2: Invalid Credit Action universalCreditAction",
        invalidTerminationActionRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditAction"
      ),
      (
        "UCL_Terminate_TC_002_0.3: Invalid Start Date with Terminate action",
        invalidTerminateStartDate,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UCL_Terminate_TC_002_0.4: Invalid End Date with Terminate action",
        invalidTerminateEndDate,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UCL_Terminate_TC_002_0.5: Invalid NINO with Terminate action",
        invalidNINOTerminationRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: nationalInsuranceNumber"
      ),
      (
        "UCL_Terminate_TC_003_0.1: Empty Credit Record Type with Terminate action",
        emptyCreditRecordTypeTerminateRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_Terminate_TC_003_0.2: Empty Credit Action with Terminate action",
        emptyCreditActionTerminationRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditAction"
      ),
      (
        "UCL_Terminate_TC_003_0.3: Empty Start Date with Terminate action",
        emptyStartDateTerminationRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UCL_Terminate_TC_003_0.4: Empty NINO with Terminate action",
        emptyNINOTerminationRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: nationalInsuranceNumber"
      ),
      (
        "UCL_Terminate_TC_003_0.5: Empty End Date with Terminate action",
        emptyEndDateTerminationRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      )
    )

    cases.foreach { case (scenarioName, payload, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")
        val response = apiService.postNotificationWithValidToken(validHeaders, payload)

        Then("400 Bad Request should be returned")
        assert(response.status == Status.BAD_REQUEST)

        Then("Response body should contain correct error details")
        val actualJson = Json.parse(response.body)
        val actualCode = (actualJson \ "code").as[String]
        val actualMsg  = (actualJson \ "message").as[String]

        assert(actualCode == expCode)
        assert(actualMsg == expMessage)
      }
    }
  }
}
