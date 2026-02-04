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

import org.scalatest.matchers.must.Matchers.{must, mustBe}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import uk.gov.hmrc.api.specs.BaseSpec
import play.api.libs.json.{JsArray, JsValue, Json}
import uk.gov.hmrc.api.testData.TestDataHip

class Insert_Unprocessable_Entity extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("422 Insert Unprocessable Entity scenarios") {

    val cases: Seq[(String, String, JsValue, String, String)] = Seq(
      (
        "UC_TC_032: ConflictLiability with Insert request",
        "GE100123",
        conflictingInsertLiabilityHIPRequest,
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UC_TC_026: Start date before 16th Birthday with insert request",
        "HC210123",
        startDateBefore16thBirthdayInsertHIPRequest,
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_027: Start date after state pension age with insert request",
        "ET060123",
        startDateAfterStatePensionAgeInsertHIPRequest,
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UC_TC_028: Start date After Death with insert request",
        "EK310123",
        startDateAfterDeathInsertHIPRequest,
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UC_TC_030: Start date and End date after death with insert request",
        "BW130123",
        startAndEndDateAfterDeathInsertHIPRequest,
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UC_TC_029: End date after State pension age with insert request",
        "EZ200123",
        endDateAfterStatePensionAgeInsertHIPRequest,
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UC_TC_031: End date after death with insert request",
        "BK190123",
        endDateAfterDeathInsertHIPRequest,
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UC_TC_033: Not Within UC Period with Insert request",
        "HS260123",
        notWithinUCPeriodInsertHIPRequest,
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UC_TC_034: LCWLCWRA Override with Insert request",
        "CE150123",
        lcwLcwrOverrideInsertHIPRequest,
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UC_TC_035: NoMatchingLiability with Insert request",
        "GP050123",
        notMatchingLiabilityInsertHIPRequest,
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UC_TC_017: StartDateBefore29042013 with Insert request",
        "GX240123",
        startDateBefore29042013InsertHIPRequest,
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UC_TC_018: EndDateBeforeStartDate with Insert request",
        "HT230123",
        endDateBeforeStartDateInsertHIPRequest,
        "65537",
        "End date before start date"
      ),
      (
        "UC_TC_023: PseudoAccount with Insert request",
        "BX100123",
        pseudoAccountInsertHIPRequest,
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UC_TC_024: NonLiveAccount with Insert request",
        "HZ310123",
        nonLiveAccountInsertHIPRequest,
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UC_TC_025: AccountTransferredIsleOfMan with Insert request",
        "BZ230123",
        accountTransferredIsleOfManInsertHIPRequest,
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UC_TC_028: StartDateAfterDeath2 with Insert request",
        "AB150123",
        startDateAfterDeath2InsertHIPRequest,
        "99999",
        "Start Date after Death"
      )
    )
    cases.foreach { case (scenarioName, nino, payload, expCode, expReason) =>
      Scenario(s"$scenarioName: Should return 422 with code $expCode") {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postHipUcLiability(validHeaders, nino = nino, requestBody = payload)

        Then("422 Unprocessable Entity should be returned")
        assert(
          response.status == Status.UNPROCESSABLE_ENTITY,
          s"Expected 422 but got ${response.status}. Body: ${response.body}"
        )

        And("The response body should contain the correct error code and reason")
        val json = Json.parse(response.body)

        val failuresArray = (json \ "failures")
          .asOpt[JsArray]
          .getOrElse(fail(s"Field 'failures' is missing in response. Body: ${response.body}"))

        val failuresList = failuresArray.value

        failuresList must not be empty

        val firstFailure: JsValue = failuresList.head

        (firstFailure \ "code").as[String] mustBe expCode
        (firstFailure \ "reason").as[String] mustBe expReason

      }
    }
  }
}
