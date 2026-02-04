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
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class InsertUnprocessableEntity extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("422 UnprocessableEntity scenarios for 'Insert' record type") {

    val cases: Seq[(String, NinoPrefix, String, String)] = Seq(
      (
        "UCL_TC_???: 55006 Start Date and End Date must be earlier than Date of Death",
        "BW130",
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UCL_TC_???: 55008 End Date must be earlier than State Pension Age",
        "EZ200",
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UCL_TC_???: 55027 End Date later than Date of Death",
        "BK190",
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UCL_TC_???: 55029 Start Date later than SPA",
        "ET060",
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UCL_TC_???: 55038 A conflicting or identical Liability is already recorded",
        "GE100",
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UCL_TC_???: 55039 NO corresponding liability found",
        "GP050",
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UCL_TC_???: 64996 Start Date is not before date of death",
        "EK310",
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UCL_TC_???: 64997 LCW/LCWRA not within a period of UC",
        "HS260",
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UCL_TC_???: 64998 LCW/LCWRA Override not within a period of LCW/LCWRA",
        "CE150",
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UCL_TC_???: 65026 Start date must not be before 16th birthday",
        "HC210",
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_???: 65536 Start date before 29/04/2013",
        "GX240",
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UCL_TC_???: 65537 End date before start date",
        "HT230",
        "65537",
        "End date before start date"
      ),
      (
        "UCL_TC_???: 65538 End date missing but the input was a Termination",
        "EA040",
        "65538",
        "End date missing but the input was a Termination"
      ),
      (
        "UCL_TC_???: 65541 The NINO input matches a Pseudo Account",
        "BX100",
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UCL_TC_???: 65542 The NINO input matches a non-live account",
        "HZ310",
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UCL_TC_???: 65543 The NINO input matches an account that has been transferred to the Isle of Man",
        "BZ230",
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UCL_TC_???: 99999 Start Date after Death",
        "AB150",
        "99999",
        "Start Date after Death"
      )
      /*
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
       */
    )

    cases.foreach { case (scenarioName, ninoPrefix, expectedCode, expectedMessage) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("a request is sent")

        val payload: JsObject = insertNotificationPayload(nino = ninoWithPrefix(ninoPrefix))

        val apiResponse: StandaloneWSResponse = apiService.postNotification(validHeaders, payload)

        Then("422 UnprocessableEntity should be returned")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe UNPROCESSABLE_ENTITY
        }

        And("response body should contain correct error 'code' and 'message'")
        val responseBody: JsValue = Json.parse(apiResponse.body)

        (responseBody \ "code").as[String] mustBe expectedCode
        (responseBody \ "message").as[String] mustBe expectedMessage
      }
    }
  }
}
