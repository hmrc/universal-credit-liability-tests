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
import play.api.http.Status.UNPROCESSABLE_ENTITY
import play.api.libs.json.{JsArray, JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataHip

class TerminateUnprocessableEntity extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("422 UnprocessableEntity Terminate scenarios") {

    val cases: Seq[(String, NinoPrefix, ErrorCode, Reason)] = Seq(
      (
        "UC_TC_045: 55038 A conflicting or identical Liability is already recorded",
        "GE100",
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "UC_TC_039: Start date before 16th Birthday with terminate request",
        "HC210",
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "UCL_TC_040: 55029 Start Date later than SPA",
        "ET060",
        "55029",
        "Start Date later than SPA"
      ),
      (
        "UC_TC_041: 64996 Start Date is not before date of death",
        "EK310",
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "UC_TC_043: 55006 Start Date and End Date must be earlier than Date of Death",
        "BW130",
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "UC_TC_042: 55008 End Date must be earlier than State Pension Age",
        "EZ200",
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "UC_TC_044: 55027 End Date later than Date of Death",
        "BK190",
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "UC_TC_046: 64997 LCW/LCWRA not within a period of UC",
        "HS260",
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "UC_TC_047: 64998 LCW/LCWRA Override not within a period of LCW/LCWRA",
        "CE150",
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "UC_TC_048: 55039 NO corresponding liability found",
        "GP050",
        "55039",
        "NO corresponding liability found"
      ),
      (
        "UC_TC_049: 65536 Start date before 29/04/2013",
        "GX240",
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "UC_TC_050: 65537 End date before start date",
        "HT230",
        "65537",
        "End date before start date"
      ),
      (
        "UC_TC_036: 65541 The NINO input matches a Pseudo Account",
        "BX100",
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "UC_TC_037: 65542 The NINO input matches a non-live account",
        "HZ310",
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "UC_TC_038: 65543 The NINO input matches an account that has been transferred to the Isle of Man",
        "BZ230",
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "UC_TC_028: 99999 Start Date after Death",
        "AB150",
        "99999",
        "Start Date after Death"
      )
    )

    cases.foreach { case (scenarioName, ninoPrefix, expectedCode, expectedReason) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("a request is sent")

        val apiResponse = apiService.postHipUcTermination(
          validHeaders,
          ninoWithPrefix(ninoPrefix),
          terminateHipPayload()
        )

        Then("422 Unprocessable Entity should be returned")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe UNPROCESSABLE_ENTITY
        }

        And("The response body should contain the correct error code and reason")
        val responseBody = Json.parse(apiResponse.body)

        val failuresArray = (responseBody \ "failures").as[JsArray].value

        failuresArray must not be empty

        val firstFailure: JsValue = failuresArray.head

        (firstFailure \ "code").as[String] mustBe expectedCode
        (firstFailure \ "reason").as[String] mustBe expectedReason
      }
    }
  }
}
