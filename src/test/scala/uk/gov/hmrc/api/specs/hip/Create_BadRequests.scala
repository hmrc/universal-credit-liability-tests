/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.api.specs.hip
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.libs.json.{JsArray, JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataHip

class Create_BadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  case class BadRequestScenario(
    description: String,
    payload: JsValue,
    expectedOrigin: String,
    expectedCodeOrType: String,
    expectedReason: String
  )

  Feature("400 Bad Request scenarios (Unified HIP & HoD)") {

    val scenarios: Seq[BadRequestScenario] = Seq(
      BadRequestScenario(
        description = "UC_TC_007_0.1: Invalid Credit Record Type (HoD)",
        payload = invalidCreditRecordTypeTTTRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      BadRequestScenario(
        description = "UC_TC_007_0.1: Invalid Credit Record Type (HIP)",
        payload = invalidCreditRecordTypeTTTHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_007_0.2: Empty Credit Record Type (HoD)",
        payload = emptyCreditRecordTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      BadRequestScenario(
        description = "UC_TC_007_0.2: Empty Credit Record Type (HIP)",
        payload = emptyCreditRecordTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.1: Invalid Start Date - Start date is after End date (Value 2028-08-19)",
        payload = invalidStartDateAfterEndDateActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.1: Invalid Start Date - Start date is after End date (Value 2028-08-19) (HIP)",
        payload = invalidStartDateAfterEndDateActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.2: Invalid Start Date - Invalid day in Fab (Value 2028-02-30)",
        payload = invalidStartDateInFebActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.2: Invalid Start Date - Invalid day in Fab (Value 2028-02-30) (HIP)",
        payload = invalidStartDateInFebActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.3: Invalid Start Date - Invalid Month (Value 2028-13-01)",
        payload = invalidStartDateInvalidMonthActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.3: Invalid Start Date - Invalid Month (Value 2028-13-01) (HIP)",
        payload = invalidStartDateInvalidMonthActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.4: Invalid Start Date - Month can't be 00 (Value 2028-00-11)",
        payload = invalidStartDateMonthZeroActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.4: Invalid Start Date - Month can't be 00 (Value 2028-00-11) (HIP)",
        payload = invalidStartDateMonthZeroActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.5: Invalid Start Date - Day can't be 00 (Value 2028-02-00)",
        payload = invalidStartDateDayZeroActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.5: Invalid Start Date - Day can't be 00 (Value 2028-02-00) (HIP)",
        payload = invalidStartDateDayZeroActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.6: Invalid Start Date - More than 30 days, ex: April (Value 2028-04-31)",
        payload = invalidStartDateDayAprilActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.6: Invalid Start Date - More than 30 days, ex: April (Value 2028-04-31) (HIP)",
        payload = invalidStartDateDayAprilActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.7: Invalid Start Date - Wrong Format (Value 11-05-2025)",
        payload = invalidStartDateWrongFormatActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.7: Invalid Start Date - Wrong Format (Value 11-05-2025) (HIP)",
        payload = invalidStartDateWrongFormatActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.8: Invalid Start Date - Zero date (Value 0000-00-00)",
        payload = invalidStartDateZeroActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.8: Invalid Start Date - Zero date (Value 0000-00-00) (HIP)",
        payload = invalidStartDateZeroActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.9: Invalid Start Date - Empty Start Date ()",
        payload = invalidStartDateEmptyActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.9: Invalid Start Date - Empty Start Date () (HIP)",
        payload = invalidStartDateEmptyActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.10: Invalid Start Date - Missing Start date ()",
        payload = invalidStartDateMissingActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      BadRequestScenario(
        description = "UC_TC_010_0.10: Invalid Start Date - Missing Start date (HIP)",
        payload = invalidStartDateMissingActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.01: Invalid End Date - End date is before Start date (Value 2028-08-18)",
        payload = invalidEndDateAfterEndDateActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.01: Invalid End Date - End date is before Start date (Value 2028-08-18) (HIP)",
        payload = invalidEndDateAfterEndDateActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Not Leap Year (Value 2027-02-29))",
        payload = invalidEndDateNotLeapYearActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Not Leap Year (Value 2027-02-29) (HIP)",
        payload = invalidEndDateNotLeapYearActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Invalid day in Feb(Value 2026-02-30)",
        payload = invalidEndDateInvalidDayFebActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Invalid day in Feb(Value 2026-02-30) (HIP)",
        payload = invalidEndDateInvalidDayFebActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Invalid Month (Value 2026-15-18)",
        payload = invalidEndDateInvalidMonthActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Invalid Month (Value 2026-15-18) (HIP)",
        payload = invalidEndDateInvalidMonthActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Month can't be 00 (Value 2026-00-11)",
        payload = invalidEndDateMonthZeroActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Month can't be 00 (Value 2026-00-11) (HIP)",
        payload = invalidEndDateMonthZeroActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Day can't be 00 (Value 2026-02-00)",
        payload = invalidEndDateDayZeroActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.02: Invalid End Date - Day can't be 00 (Value 2026-02-00) (HIP)",
        payload = invalidEndDateDayZeroActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - More than 30 days, ex: April (Value 2028-04-31)",
        payload = invalidEndDateDayAprilActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - More than 30 days, ex: April (Value 2028-04-31) (HIP)",
        payload = invalidEndDateDayAprilActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Format issue - YYYY-M-DD (Value 2028-9-11)",
        payload = invalidEndDateFormatIssueActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Format issue - YYYY-M-DD (Value 2028-9-11) (HIP)",
        payload = invalidEndDateFormatIssueActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Wrong Format - DD-MM-YYYY(Value 11-05-2025)",
        payload = invalidEndDateWrongFormatActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Wrong Format - DD-MM-YYYY(Value 11-05-2025) (HIP)",
        payload = invalidEndDateWrongFormatActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Zero date (Value 0000-00-00)",
        payload = invalidEndDateZeroActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Zero date (Value 0000-00-00) (HIP)",
        payload = invalidEndDateZeroActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Empty Start Date ()",
        payload = invalidEndDateEmptyActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Empty Start Date () (HIP)",
        payload = invalidEndDateEmptyActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Missing end date",
        payload = invalidEndDateMissingActionTypeRequest,
        expectedOrigin = "HoD",
        expectedCodeOrType = "400.1",
        expectedReason = "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      BadRequestScenario(
        description = "UC_TC_011_0.03: Invalid End Date - Missing end date (HIP)",
        payload = invalidEndDateMissingActionTypeHIPRequest,
        expectedOrigin = "HIP",
        expectedCodeOrType = "Type of Failure",
        expectedReason = "Reason for Failure"
      )
    )

    scenarios.foreach { scenario =>
      Scenario(scenario.description) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postHipUcLiability(validHeaders, randomNino, requestBody = scenario.payload)

        Then("400 Bad Request should be returned")
        assert(response.status == Status.BAD_REQUEST, s"Expected 400, got ${response.status}. Body: ${response.body}")

        And(s"The response should match the ${scenario.expectedOrigin} error format")
        val json         = Json.parse(response.body)
        val actualOrigin = (json \ "origin").as[String]

        actualOrigin shouldBe scenario.expectedOrigin

        // 4. Conditional Logic based on Origin
        if (actualOrigin == "HoD") {

          val failures     = (json \ "response" \ "failures").as[JsArray]
          val firstFailure = failures.value.head

          (firstFailure \ "code").as[String]   shouldBe scenario.expectedCodeOrType
          (firstFailure \ "reason").as[String] shouldBe scenario.expectedReason

        } else {

          val failures     = (json \ "response").as[JsArray]
          val firstFailure = failures.value.head

          (firstFailure \ "type").as[String]   shouldBe scenario.expectedCodeOrType
          (firstFailure \ "reason").as[String] shouldBe scenario.expectedReason
        }
      }
    }
  }
}
