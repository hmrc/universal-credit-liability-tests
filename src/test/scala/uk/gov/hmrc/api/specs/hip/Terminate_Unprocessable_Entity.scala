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
import uk.gov.hmrc.api.specs.BaseSpec
import play.api.libs.json.{JsArray, JsValue, Json}
import uk.gov.hmrc.api.testData.TestDataHip

class Terminate_Unprocessable_Entity extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("422 Terminate Unprocessable Entity scenarios") {

    val cases: Seq[(String, Seq[(String, String)], JsValue, String, String)] = Seq(
      (
        "UC_TC_045: ConflictLiability with terminate request",
        validHeaders,
        conflictingTerminateLiabilityHIPRequest,
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UC_TC_039: Start date before 16th Birthday with terminate request",
        validHeaders,
        startDateBefore16thBirthdayTerminateHIPRequest,
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_040: Start date after state pension age with terminate request",
        validHeaders,
        startDateAfterStatePensionAgeTerminateHIPRequest,
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UC_TC_041: Start date After Death with terminate request",
        validHeaders,
        startDateAfterDeathTerminateHIPRequest,
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UC_TC_043: Start date and End date after death with terminate request",
        validHeaders,
        startAndEndDateAfterDeathTerminateHIPRequest,
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UC_TC_042: End date after State pension age with terminate request",
        validHeaders,
        endDateAfterStatePensionAgeTerminateHIPRequest,
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UC_TC_044: End date after death with terminate request",
        validHeaders,
        endDateAfterDeathTerminateHIPRequest,
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UC_TC_046: Not Within UC Period with terminate request",
        validHeaders,
        notWithinUCPeriodTerminateHIPRequest,
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UC_TC_047: LCWLCWRA Override with terminate request",
        validHeaders,
        lcwLcwrOverrideTerminateHIPRequest,
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UC_TC_048: NoMatchingLiability with terminate request",
        validHeaders,
        notMatchingLiabilityTerminateHIPRequest,
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UC_TC_049: StartDateBefore29042013 with terminate request",
        validHeaders,
        startDateBefore29042013TerminateHIPRequest,
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UC_TC_050: EndDateBeforeStartDate with terminate request",
        validHeaders,
        endDateBeforeStartDateTerminateHIPRequest,
        "65537",
        "End date before start date"
      ),
      (
        "UC_TC_036: PseudoAccount with terminate request",
        validHeaders,
        pseudoAccountTerminateHIPRequest,
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UC_TC_037: NonLiveAccount with terminate request",
        validHeaders,
        nonLiveAccountTerminateHIPRequest,
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UC_TC_038: AccountTransferredIsleOfMan with terminate request",
        validHeaders,
        accountTransferredIsleOfManTerminateHIPRequest,
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UC_TC_051: End date missing with terminate request",
        validHeaders,
        endDateMissingTerminateHIPRequest,
        "65538",
        "End date missing but the input was a Termination"
      )
    )
    cases.foreach { case (scenarioName, headers, payload, expCode, expReason) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postHipUcTermination(headers, randomNino, payload)

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
        val failuresList  = failuresArray.value

        failuresList should not be empty

        val firstFailure: JsValue = failuresList.head

        assert((firstFailure \ "code").as[String] == expCode)
        assert((firstFailure \ "reason").as[String] == expReason)

      }
    }
  }
}
