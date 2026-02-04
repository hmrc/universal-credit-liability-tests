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

class TerminateUnprocessableEntity extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  //  Feature("422 Unprocessable Entity Scenarios") {
  //
  //    unprocessableCases.foreach { case (ninoPrefix, (errorCode, _)) =>
  //      Scenario(s"returns 422 with code $errorCode") {
  //        ninoWithPrefix(ninoPrefix)
  //        val response = apiService.postNotification(
  //          validHeaders,
  //          uclPayload("LCW/LCWRA", "Terminate", "2025-01-04", endDate = Some("2025-01-04"), nino = "AA057680")
  //        )
  //        response.status mustBe UNPROCESSABLE_ENTITY
  //      }
  //
  //    }
  //  }

  Feature("422 Unprocessable Entity Scenarios") {

    val cases: Seq[(String, NinoPrefix)] = Seq(
      (
        "UCL_TC_008_0.2: Conflict Liability with Terminate request",
        "BW130"
      )
    )

    cases.foreach { case (scenarioName, ninoPrefix) =>
      val (code, reason) = unprocessableCases(ninoPrefix)

      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A notification request is sent")

        val payload = terminatePayload(ninoWithPrefix(ninoPrefix))
        apiService.postNotification(validHeaders, payload)
      }
    }
  }

  Feature("422 Terminate Unprocessable Entity scenarios") {

    val cases: Seq[(String, JsValue, ErrorCode, Reason)] = Seq(
      (
        "UCL_TC_008_0.2: Conflict Liability with Terminate request",
        conflictingTerminateLiabilityRequest,
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UCL_TC_008_0.4: Start date before 16th Birthday with Terminate request",
        startDateBefore16thBirthdayTerminateRequest,
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_008_0.6: Start date after state pension age with Terminate request",
        startDateAfterStatePensionAgeTerminateRequest,
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UCL_TC_008_0.8: Start date After Death with Terminate request",
        startDateAfterDeathTerminateRequest,
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UCL_TC_008_0.10: Start date and End date after death with Terminate request",
        startAndEndDateAfterDeathTerminateRequest,
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UCL_TC_008_0.12: End Date After StatePensionAge with Terminate request",
        endDateAfterStatePensionAgeTerminateRequest,
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UCL_TC_008_0.14: End Date After Death with Terminate request",
        endDateAfterDeathTerminateRequest,
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UCL_TC_008_0.16: Not Within UC Period with Terminate request",
        notWithinUCPeriodTerminateRequest,
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UCL_TC_008_0.18: LCWLCWRA Override with Terminate request",
        lcwlcwraOverrideTerminateRequest,
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UCL_TC_008_0.20: NoMatchingLiability with Terminate request",
        noMatchingLiabilityTerminateRequest,
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UCL_TC_008_0.22: StartDateBefore29042013 with Terminate request",
        startDateBefore29042013TerminateRequest,
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UCL_TC_008_0.24: EndDateBeforeStartDate with Terminate request",
        endDateBeforeStartDateTerminateRequest,
        "65537",
        "End date before start date"
      ),
      (
        "UCL_TC_008_0.26: PseudoAccount with Terminate request",
        pseudoAccountTerminateRequest,
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UCL_TC_008_0.28: NonLiveAccount with Terminate request",
        nonLiveAccountTerminateRequest,
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UCL_TC_008_0.30: AccountTransferredIsleOfMan with Terminate request",
        accountTransferredIsleOfManTerminateRequest,
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UCL_TC_008_0.32: StartDateAfterDeath2 with Terminate request",
        startDateAfterDeath2TerminateRequest,
        "99999",
        "Start Date after Death"
      )
    )

    cases.foreach { case (scenarioName, payload, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postNotification(validHeaders, payload)

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
