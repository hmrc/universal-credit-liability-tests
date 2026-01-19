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

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataHip

class Create_BadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("400 Bad Request scenarios for HIP Create UC Liability") {

    val cases: Seq[(String, JsValue, String, String, String)] = Seq(
      (
        "UC_TC_007_0.1: Invalid Credit Record Type (HoD)",
        invalidCreditRecordTypeTTTRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UC_TC_007_0.2: Empty Credit Record Type (HoD)",
        emptyCreditRecordTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UC_TC_010_0.1: Invalid Start Date - Start date is after End date (Value 2028-08-19)",
        invalidStartDateAfterEndDateActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.2: Invalid Start Date - Invalid day in Fab (Value 2028-02-30)",
        invalidStartDateInFebActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.3: Invalid Start Date - Invalid Month (Value 2028-13-01)",
        invalidStartDateInvalidMonthActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.4: Invalid Start Date - Month can't be 00 (Value 2028-00-11)",
        invalidStartDateMonthZeroActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.5: Invalid Start Date - Day can't be 00 (Value 2028-02-00)",
        invalidStartDateDayZeroActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.6: Invalid Start Date - More than 30 days, ex: April (Value 2028-04-31)",
        invalidStartDateDayAprilActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.7: Invalid Start Date - Wrong Format (Value 11-05-2025)",
        invalidStartDateWrongFormatActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.8: Invalid Start Date - Zero date (Value 0000-00-00)",
        invalidStartDateZeroActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.9: Invalid Start Date - Empty Start Date ()",
        invalidStartDateEmptyActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_010_0.10: Invalid Start Date - Missing Start date ()",
        invalidStartDateMissingActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
      ),
      (
        "UC_TC_011_0.01: Invalid End Date - End date is before Start date (Value 2028-08-18)",
        invalidEndDateAfterEndDateActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Not Leap Year (Value 2027-02-29))",
        invalidEndDateNotLeapYearActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Invalid day in Feb(Value 2026-02-30)",
        invalidEndDateInvalidDayFebActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Invalid Month (Value 2026-15-18)",
        invalidEndDateInvalidMonthActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Month can't be 00 (Value 2026-00-11)",
        invalidEndDateMonthZeroActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Day can't be 00 (Value 2026-02-00)",
        invalidEndDateDayZeroActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - More than 30 days, ex: April (Value 2028-04-31)",
        invalidEndDateDayAprilActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Format issue - YYYY-M-DD (Value 2028-9-11)",
        invalidEndDateFormatIssueActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Wrong Format - DD-MM-YYYY(Value 11-05-2025)",
        invalidEndDateWrongFormatActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Zero date (Value 0000-00-00)",
        invalidEndDateZeroActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Empty Start Date ()",
        invalidEndDateEmptyActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Missing end date",
        invalidEndDateMissingActionTypeRequest,
        "HoD",
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
      )
    )

    cases.foreach { case (scenarioName, payload, origin, expectedCode, expectedReason) =>
      Scenario(scenarioName) {
        Given("The HIP API is up and running")
        When("A request is sent with invalid data")

        val hipResponse: StandaloneWSResponse = apiService.postHipUcLiability(validHeaders, randomNino, payload)

        Then("400 Bad Request should be returned")
        withClue(s"Expected 400, got ${hipResponse.status}. Body ${hipResponse.body}\n") {
          hipResponse.status mustBe Status.BAD_REQUEST
        }

        And("Response body should match HIP error format")
        val actualJson     = Json.parse(hipResponse.body)
        val actualFailures = (actualJson \ "response" \ "failures").as[JsArray]
        val firstFailure   = actualFailures.value.head

        (actualJson \ "origin").as[String] mustBe origin
        (firstFailure \ "code").as[String] mustBe expectedCode
        (firstFailure \ "reason").as[String] mustBe expectedReason

      }
    }
  }

}
