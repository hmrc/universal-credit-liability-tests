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
import uk.gov.hmrc.api.testData.TestDataFile

class ErrorValidation_BadRequest extends BaseSpec with GuiceOneServerPerSuite with TestDataFile {

  Feature("400 Bad Request scenarios") {

    val cases = Seq(
      (
        "UCL_TC_001_0.5: Insert Invalid LCW/LCWRA details",
        invalidInsertLCWLCWRALiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_TC_001_0.6: Insert Invalid UC details",
        invalidInsertUCLiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_TC_002_0.3: Invalid Credit Action",
        invalidInsertCreditActionRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditAction"
      ),
      (
        "UCL_TC_002_0.4: Invalid Date of Birth with Insert action",
        invalidInsertDateOfBirthRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: dateOfBirth"
      ),
      (
        "UCL_TC_002_0.5: Invalid Start Date with Insert action",
        invalidInsertStartDateRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UCL_TC_002_0.6: Invalid NINO with Insert action",
        invalidInsertNINORequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: nationalInsuranceNumber"
      ),
      (
        "UCL_TC_001_0.7: Termination Invalid LCW/LCWRA details",
        inValidTerminationLCWLCWRALiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_TC_001_0.8: Termination Invalid UC details",
        inValidTerminationUCLiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
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
