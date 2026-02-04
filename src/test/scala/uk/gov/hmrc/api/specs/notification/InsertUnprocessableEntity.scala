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

package uk.gov.hmrc.api.specs.notification

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.UNPROCESSABLE_ENTITY
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class InsertUnprocessableEntity extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("422 Insert Unprocessable Entity scenarios") {

    val cases: Seq[(String, JsValue, String, String)] = Seq(
      (
        "UCL_TC_008_0.1: ConflictLiability with Insert request",
        conflictingInsertLiabilityRequest,
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UCL_TC_008_0.3: Start date before 16th Birthday with insert request",
        startDateBefore16thBirthdayInsertRequest,
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_008_0.5: Start date after state pension age with insert request",
        startDateAfterStatePensionAgeInsertRequest,
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UCL_TC_008_0.7: Start date After Death with insert request",
        startDateAfterDeathInsertRequest,
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UCL_TC_008_0.9: Start date and End date after death with insert request",
        startAndEndDateAfterDeathInsertRequest,
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UCL_TC_008_0.11: Start date and End date after death with insert request",
        endDateAfterStatePensionAgeInsertRequest,
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UCL_TC_008_0.13: Start date and End date after death with insert request",
        endDateAfterDeathInsertRequest,
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UCL_TC_008_0.15: Not Within UC Period with Insert request",
        notWithinUCPeriodInsertRequest,
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UCL_TC_008_0.17: LCWLCWRA Override with Insert request",
        lcwLcwrOverrideInsertRequest,
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UCL_TC_008_0.19: NoMatchingLiability with Insert request",
        notMatchingLiabilityInsertRequest,
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UCL_TC_008_0.21: StartDateBefore29042013 with Insert request",
        startDateBefore29042013InsertRequest,
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UCL_TC_008_0.23: EndDateBeforeStartDate with Insert request",
        endDateBeforeStartDateInsertRequest,
        "65537",
        "End date before start date"
      ),
      (
        "UCL_TC_008_0.25: PseudoAccount with Insert request",
        pseudoAccountInsertRequest,
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UCL_TC_008_0.27: NonLiveAccount with Insert request",
        nonLiveAccountInsertRequest,
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UCL_TC_008_0.29: AccountTransferredIsleOfMan with Insert request",
        accountTransferredIsleOfManInsertRequest,
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UCL_TC_008_0.31: StartDateAfterDeath2 with Insert request",
        startDateAfterDeath2InsertRequest,
        "99999",
        "Start Date after Death"
      )
    )

    cases.foreach { case (scenarioName, payload, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("422 Unprocessable entity should be returned")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe UNPROCESSABLE_ENTITY
        }

        And("Response body should contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)

        (responseBody \ "code").as[String] mustBe expCode
        (responseBody \ "message").as[String] mustBe expMessage
      }
    }
  }
}
