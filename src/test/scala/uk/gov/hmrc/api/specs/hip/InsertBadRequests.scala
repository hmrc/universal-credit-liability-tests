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
import play.api.libs.json.JsValue
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataHip

class InsertBadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("BadRequest (400) scenarios for HIP 'Insert' Universal Credit Liability details") {

    val cases: Seq[(String, JsValue, String, Reason)] = Seq(
      (
        "UC_TC_007_0.1: Invalid Credit Record Type",
        insertHipPayload(recordType = "INVALID"),
        randomNino,
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UC_TC_007_0.2: Empty Credit Record Type",
        insertHipPayload(recordType = ""),
        randomNino,
        constraintViolation("universalCreditRecordType")
      ),
      (
        "UC_TC_010_0.2: Invalid Start Date - Invalid day in Feb (value 2028-02-30)",
        insertHipPayload(startDate = "2028-02-30"),
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.3: Invalid Start Date - Invalid Month (Value 2028-13-01)",
        insertHipPayload(startDate = "2028-13-01"),
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.4: Invalid Start Date - Month can't be 00 (Value 2028-00-11)",
        insertHipPayload(startDate = "2028-00-11"),
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.5: Invalid Start Date - Day can't be 00 (Value 2028-02-00)",
        invalidStartDateDayZeroActionTypeRequest,
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.6: Invalid Start Date - More than 30 days, ex: April (Value 2028-04-31)",
        invalidStartDateDayAprilActionTypeRequest,
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.7: Invalid Start Date - Wrong Format (Value 11-05-2025)",
        invalidStartDateWrongFormatActionTypeRequest,
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.8: Invalid Start Date - Zero date (Value 0000-00-00)",
        invalidStartDateZeroActionTypeRequest,
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.9: Invalid Start Date - Empty Start Date ()",
        invalidStartDateEmptyActionTypeRequest,
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_010_0.10: Invalid Start Date - Missing Start date ()",
        invalidStartDateMissingActionTypeRequest,
        randomNino,
        constraintViolation("liabilityStartDate")
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Not Leap Year (Value 2027-02-29))",
        insertHipPayload(startDate = "2027-02-29"),
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Invalid day in Feb(Value 2026-02-30)",
        invalidEndDateInvalidDayFebActionTypeRequest,
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Invalid Month (Value 2026-15-18)",
        invalidEndDateInvalidMonthActionTypeRequest,
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Month can't be 00 (Value 2026-00-11)",
        invalidEndDateMonthZeroActionTypeRequest,
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.02: Invalid End Date - Day can't be 00 (Value 2026-02-00)",
        invalidEndDateDayZeroActionTypeRequest,
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - More than 30 days, ex: April (Value 2028-04-31)",
        invalidEndDateDayAprilActionTypeRequest,
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Format issue - YYYY-M-DD (Value 2028-9-11)",
        insertHipPayload(endDate = "2028-9-11"),
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Wrong Format - DD-MM-YYYY (Value 11-05-2025)",
        insertHipPayload(endDate = "11-05-2025"),
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Zero date (Value 0000-00-00)",
        insertHipPayload(endDate = "0000-00-00"),
        randomNino,
        constraintViolation("liabilityEndDate")
      ),
      (
        "UC_TC_011_0.03: Invalid End Date - Empty Start Date ()",
        insertHipPayload(startDate = ""),
        randomNino,
        constraintViolation("liabilityEndDate")
      )

      // TODO: implement these scenarios
      //  (
      //    "UC_TC_016_0.3 Missing correlationId header",
      //    headersWithoutCorrelationId,
      //    randomNino,
      //    validUCLiabilityRequest
      //  )
      //  ,
      //  (
      //    "UC_TC_016_0.4 Invalid correlationId header",
      //    headersWithInvalidCorrelationId,
      //    randomNino,
      //    validUCLiabilityRequest
      //  )
    )

    cases.foreach { case (scenarioName, payload, nino, _) =>
      Scenario(scenarioName) {
        Given("The HIP API is up and running")
        When("a request is sent with invalid data")

        val hipResponse: StandaloneWSResponse = apiService.postHipUcLiability(validHeaders, nino, payload)

        Then("400 Bad Request must be returned")
        withClue(s"Expected 400, got ${hipResponse.status}. Body ${hipResponse.body}\n") {
          hipResponse.status mustBe Status.BAD_REQUEST
        }

        And("response body must be empty")
        hipResponse.body mustBe empty
      }
    }
  }

}
