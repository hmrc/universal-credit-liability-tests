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

class Terminate_Unprocessable_entity
  extends BaseSpec
    with GuiceOneServerPerSuite
    with TestDataNotification {

  Feature("422 Terminate Unprocessable Entity scenarios") {

    val cases: Seq[(String, Seq[(String, String)], JsValue, String, String)] = Seq(
      (
        "UCL_TC_008_0.2: ConflictLiability with Terminate request",
        validHeaders,
        conflictingTerminateLiabilityRequest,
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UCL_TC_008_0.4: Start date before 16th Birthday with Terminate request",
        validHeaders,
        startDateBefore16thBirthdayTerminateRequest,
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_008_0.6: Start date after state pension age with Terminate request",
        validHeaders,
        startDateAfterStatePensionAgeTerminateRequest,
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UCL_TC_008_0.8: Start date After Death with Terminate request",
        validHeaders,
        startDateAfterDeathTerminateRequest,
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UCL_TC_008_0.10: Start date and End date after death with Terminate request",
        validHeaders,
        startAndEndDateAfterDeathTerminateRequest,
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UCL_TC_008_0.12: End Date After StatePensionAge with Terminate request",
        validHeaders,
        endDateAfterStatePensionAgeTerminateRequest,
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UCL_TC_008_0.14: End Date After Death with Terminate request",
        validHeaders,
        endDateAfterDeathTerminateRequest,
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UCL_TC_008_0.16: Not Within UC Period with Terminate request",
        validHeaders,
        notWithinUCPeriodTerminateRequest,
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UCL_TC_008_0.18: LCWLCWRA Override with Terminate request",
        validHeaders,
        lcwlcwraOverrideTerminateRequest,
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UCL_TC_008_0.20: NoMatchingLiability with Terminate request",
        validHeaders,
        noMatchingLiabilityTerminateRequest,
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UCL_TC_008_0.22: StartDateBefore29042013 with Terminate request",
        validHeaders,
        startDateBefore29042013TerminateRequest,
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UCL_TC_008_0.24: EndDateBeforeStartDate with Terminate request",
        validHeaders,
        endDateBeforeStartDateTerminateRequest,
        "65537",
        "End date before start date"
      ),
      (
        "UCL_TC_008_0.26: PseudoAccount with Terminate request",
        validHeaders,
        pseudoAccountTerminateRequest,
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UCL_TC_008_0.28: NonLiveAccount with Terminate request",
        validHeaders,
        nonLiveAccountTerminateRequest,
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UCL_TC_008_0.30: AccountTransferredIsleOfMan with Terminate request",
        validHeaders,
        accountTransferredIsleOfManTerminateRequest,
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UCL_TC_008_0.32: StartDateAfterDeath2 with Terminate request",
        validHeaders,
        startDateAfterDeath2TerminateRequest,
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
