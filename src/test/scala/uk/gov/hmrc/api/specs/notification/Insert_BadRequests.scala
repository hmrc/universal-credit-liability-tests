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
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class Insert_BadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("400 Bad Request scenarios for Insert Record Type") {

    val cases: Seq[(String, Seq[(String, String)], JsValue, String, String)] = Seq(
      (
        "UCL_TC_001_0.5: Insert Invalid LCW/LCWRA details",
        validHeaders,
        invalidInsertLCWLCWRALiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_TC_001_0.6: Insert Invalid UC details",
        validHeaders,
        invalidInsertUCLiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_TC_002_0.3: Invalid Credit Action",
        validHeaders,
        invalidInsertCreditActionRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditAction"
      ),
      (
        "UCL_TC_002_0.4: Invalid Date of Birth with Insert action",
        validHeaders,
        invalidInsertDateOfBirthRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: dateOfBirth"
      ),
      (
        "UCL_TC_002_0.5: Invalid Start Date with Insert action",
        validHeaders,
        invalidInsertStartDateRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UCL_TC_002_0.6: Invalid NINO with Insert action",
        validHeaders,
        invalidInsertNINORequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: nationalInsuranceNumber"
      ),
      (
        "UCL_TC_003_0.1: Empty Credit Record Type with Insert action",
        validHeaders,
        emptyInsertCreditRecordTypeRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_TC_003_0.2: Empty Credit Action with Insert action",
        validHeaders,
        emptyInsertCreditActionRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditAction"
      ),
      (
        "UCL_TC_003_0.3: Empty Date of Birth with Insert action",
        validHeaders,
        emptyInsertDateOfBirthRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: dateOfBirth"
      ),
      (
        "UCL_TC_003_0.4:Empty Start Date with Insert action ",
        validHeaders,
        emptyInsertStartDateRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UCL_TC_003_0.5: Empty NINO with Insert action",
        validHeaders,
        emptyInsertNINORequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: nationalInsuranceNumber"
      ),
      (
        "UCL_TC_005_0.1: Invalid Parameters: correlation ID",
        invalidCorrelationIdHeaders,
        validInsertUCLiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: correlationId"
      ),
      (
        "UCL_TC_005_0.2: Empty or Missing Parameters: correlation ID field",
        missingCorrelationIdHeaders,
        validInsertUCLiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: correlationId"
      )
    )

    cases.foreach { case (scenarioName, headers, payload, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postNotificationWithValidToken(headers, payload)

        Then("400 Bad Request should be returned")
        assert(response.status == Status.BAD_REQUEST)

        And("Response body should contain correct error details")
        val actualJson = Json.parse(response.body)
        val actualCode = (actualJson \ "code").as[String]
        val actualMsg  = (actualJson \ "message").as[String]

        assert(actualCode == expCode)
        assert(actualMsg == expMessage)
      }
    }
  }
}
