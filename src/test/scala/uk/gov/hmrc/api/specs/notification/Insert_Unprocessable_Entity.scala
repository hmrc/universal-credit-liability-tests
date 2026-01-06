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

import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class Insert_Unprocessable_Entity
  extends BaseSpec
    with GuiceOneServerPerSuite
    with TestDataNotification {

  Feature("422 Insert Unprocessable Entity scenarios") {

    val cases: Seq[(String, Seq[(String, String)], JsValue, String, String)] = Seq(
      (
        "UCL_TC_008_0.1: ConflictLiability with Insert request",
        validHeaders,
        conflictingInsertLiabilityRequest,
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UCL_TC_008_0.3: Start date before 16th Birthday with insert request",
        validHeaders,
        startDateBefore16thBirthdayInsertRequest,
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_008_0.5: Start date after state pension age with insert request",
        validHeaders,
        startDateAfterStatePensionAgeInsertRequest,
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UCL_TC_008_0.7: Start date After Death with insert request",
        validHeaders,
        startDateAfterDeathInsertRequest,
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UCL_TC_008_0.9: Start date and End date after death with insert request",
        validHeaders,
        startAndEndDateAfterDeathInsertRequest,
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UCL_TC_008_0.11: Start date and End date after death with insert request",
        validHeaders,
        endDateAfterStatePensionAgeInsertRequest,
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UCL_TC_008_0.13: Start date and End date after death with insert request",
        validHeaders,
        endDateAfterDeathInsertRequest,
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UCL_TC_008_0.15: Not Within UC Period with Insert request",
        validHeaders,
        notWithinUCPeriodInsertRequest,
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UCL_TC_008_0.17: LCWLCWRA Override with Insert request",
        validHeaders,
        lcwLcwrOverrideInsertRequest,
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UCL_TC_008_0.19: NoMatchingLiability with Insert request",
        validHeaders,
        notMatchingLiabilityInsertRequest,
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UCL_TC_008_0.21: StartDateBefore29042013 with Insert request",
        validHeaders,
        startDateBefore29042013InsertRequest,
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UCL_TC_008_0.23: EndDateBeforeStartDate with Insert request",
        validHeaders,
        endDateBeforeStartDateInsertRequest,
        "65537",
        "End date before start date"
      ),
      (
        "UCL_TC_008_0.25: PseudoAccount with Insert request",
        validHeaders,
        pseudoAccountInsertRequest,
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UCL_TC_008_0.27: NonLiveAccount with Insert request",
        validHeaders,
        nonLiveAccountInsertRequest,
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UCL_TC_008_0.29: AccountTransferredIsleOfMan with Insert request",
        validHeaders,
        accountTransferredIsleOfManInsertRequest,
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UCL_TC_008_0.31: StartDateAfterDeath2 with Insert request",
        validHeaders,
        startDateAfterDeath2InsertRequest,
        "99999",
        "Start Date after Death"
      )
    )

    cases.foreach { case (scenarioName, headers, payload, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postNotificationWithValidToken(headers, payload)

        Then("422 Unprocessable entity should be returned")
        assert(response.status == Status.UNPROCESSABLE_ENTITY)

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
