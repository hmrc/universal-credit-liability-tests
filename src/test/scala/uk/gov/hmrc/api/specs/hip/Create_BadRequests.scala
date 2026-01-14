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

  Feature("400 Bad Request scenarios for Invalid Record Type") {
    val cases: Seq[(String, Seq[(String, String)], JsValue, String, String)] = Seq(
      (
        "UC_TC_007_0.1: Empty or Invalid Credit Record Type (value TTT)",
        validHeaders,
        invalidCreditRecordTypeTTTRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UC_TC_007_0.1: Empty or Invalid Credit Record Type (empty value)",
        validHeaders,
        emptyCreditRecordTypeRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
//      (
//        "UC_TC_008_0.1: Empty or Invalid Credit Action (Value Dummy)",
//        validHeaders,
//        invalidCreditActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: universalCreditAction"
//      ),
//      (
//        "UC_TC_008_0.2: Empty or Invalid Credit Action (empty value)",
//        validHeaders,
//        emptyCreditActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: universalCreditAction"
//      ),
//      (
//        "UC_TC_009_0.1: Empty or Invalid Date of Birth (empty value)",
//        validHeaders,
//        emptyDOBActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: dateOfBirth"
//      ),
//      (
//        "UC_TC_009_0.1: Empty or Invalid Date of Birth (Value 2030-10-10)",
//        validHeaders,
//        invalidDOBActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: dateOfBirth"
//      ),
//      (
//        "UC_TC_009_0.2: Missing Date of Birth (Value 2030-10-10)",
//        validHeaders,
//        missingDOBActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: dateOfBirth"
//      ),
//      (
//        "UC_TC_010_0.1: Invalid Start Date - Start date is after End date (Value 2028-08-19)",
//        validHeaders,
//        invalidStartDateAfterEndDateActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.2: Invalid Start Date - Invalid day in Fab (Value 2028-02-30)",
//        validHeaders,
//        invalidStartDateInFebActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.3: Invalid Start Date - Invalid Month (Value 2028-13-01)",
//        validHeaders,
//        invalidStartDateInvalidMonthActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.4: Invalid Start Date - Month can't be 00 (Value 2028-00-11)",
//        validHeaders,
//        invalidStartDateMonthZeroActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.5: Invalid Start Date - Day can't be 00 (Value 2028-02-00)",
//        validHeaders,
//        invalidStartDateDayZeroActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.6: Invalid Start Date - More than 30 days, ex: April (Value 2028-04-31)",
//        validHeaders,
//        invalidStartDateDayAprilActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.7: Invalid Start Date - Format issue (Value 2028-9-11)",
//        validHeaders,
//        invalidStartDateFormatIssueActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.8: Invalid Start Date - Wrong Format (Value 11-05-2025)",
//        validHeaders,
//        invalidStartDateWrongFormatActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.9: Invalid Start Date - Zero date (Value 0000-00-00)",
//        validHeaders,
//        invalidStartDateZeroActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.10: Invalid Start Date - Empty Start Date ()",
//        validHeaders,
//        invalidStartDateEmptyActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_010_0.11: Invalid Start Date - Missing Start date",
//        validHeaders,
//        invalidStartDateMissingActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityStartDate"
//      ),
//      (
//        "UC_TC_011_0.1: Invalid End Date - End date is before Start date (Value 2028-08-18)",
//        validHeaders,
//        invalidEndDateAfterEndDateActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.2: Invalid End Date - Not Leap Year (Value 2027-02-29)",
//        validHeaders,
//        invalidEndDateNotLeapYearActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.3: Invalid End Date - Invalid day in Feb(Value 2026-02-30)",
//        validHeaders,
//        invalidEndDateInvalidDayFebActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.4: Invalid End Date - Invalid Month (Value 2026-15-18)",
//        validHeaders,
//        invalidEndDateInvalidMonthActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.5: Invalid End Date - Month can't be 00 (Value 2026-00-11)",
//        validHeaders,
//        invalidEndDateMonthZeroActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.6: Invalid End Date - Day can't be 00 (Value 2026-02-00)",
//        validHeaders,
//        invalidEndDateDayZeroActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.7: Invalid End Date - More than 30 days, ex: April (Value 2028-04-31)",
//        validHeaders,
//        invalidEndDateDayAprilActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.8: Invalid End Date - Format issue - YYYY-M-DD (Value 2028-9-11)",
//        validHeaders,
//        invalidEndDateFormatIssueActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.9: Invalid End Date - Wrong Format - DD-MM-YYYY(Value 11-05-2025)",
//        validHeaders,
//        invalidEndDateWrongFormatActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.10: Invalid End Date - Zero date (Value 0000-00-00)",
//        validHeaders,
//        invalidEndDateZeroActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.11: Invalid End Date - Empty Start Date ()",
//        validHeaders,
//        invalidEndDateEmptyActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      ),
//      (
//        "UC_TC_011_0.12: Invalid End Date - Missing end date",
//        validHeaders,
//        invalidEndDateMissingActionTypeRequest,
//        "400.1",
//        "Constraint Violation - Invalid/Missing input parameter: liabilityEndDate"
//      )
    )
//
    cases.foreach { case (scenarioName, headers, payload, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")
        //println("test payload:  "+payload)
        val response = apiService.postHipUcLiability(headers, randomNino, payload)

        Then("400 Bad Request should be returned")
        assert(response.status == Status.BAD_REQUEST)

        And("Response body should contain correct error details")
        val actualJson = Json.parse(response.body)

        (actualJson \ "origin").as[String] shouldBe "HoD"

//                  val failures = (actualJson \ "response" \ "failures").as[JsArray]
//                  val firstFailure = failures.value.head.as[JsObjec]
//
//                  (firstFailure \ "code").as[String] shouldBe "400.1"
//                  (firstFailure \ "reason").as[String] shouldBe "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      }
    }
  }
}