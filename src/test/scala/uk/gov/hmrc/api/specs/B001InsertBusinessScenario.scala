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

package uk.gov.hmrc.api.specs

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.UNPROCESSABLE_ENTITY
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.api.testData.*

class B001InsertBusinessScenario
    extends BaseSpec
    with GuiceOneServerPerSuite
    with TestDataNotification
    with TestDataHip {

  Feature("UCL_TC_B001 : Insert handle and cascade 422 and business error code from HIP to DWP") {

    val cases: Seq[(String, NinoPrefix, BusinessErrorCode, BusinessErrorMessage)] = Seq(
      (
        "Error : API return 422 and cascade business error 55006 from HIP to DWP",
        "BW130",
        "55006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "Error : API return 422 and cascade business error 55008 from HIP to DWP",
        "EZ200",
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "Error : API return 422 and cascade business error 55027 from HIP to DWP",
        "BK190",
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "Error : API return 422 and cascade business error 55029 from HIP to DWP",
        "ET060",
        "55029",
        "Start Date later than SPA"
      ),
      (
        "Error : API return 422 and cascade business error 55038 from HIP to DWP",
        "GE100",
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "Error : API return 422 and cascade business error 55039 from HIP to DWP",
        "GP050",
        "55039",
        "NO corresponding liability found"
      ),
      (
        "Error : API return 422 and cascade business error 64996 from HIP to DWP",
        "EK310",
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "Error : API return 422 and cascade business error 64997 from HIP to DWP",
        "HS260",
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "Error : API return 422 and cascade business error 64998 from HIP to DWP",
        "CE150",
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "Error : API return 422 and cascade business error 65026 from HIP to DWP",
        "HC210",
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "Error : API return 422 and cascade business error 65536 from HIP to DWP",
        "GX240",
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "Error : API return 422 and cascade business error 65537 from HIP to DWP",
        "HT230",
        "65537",
        "End date before start date"
      ),
      (
        "Error : API return 422 and cascade business error 65537 from HIP to DWP",
        "EA040",
        "65538",
        "End date missing but the input was a Termination"
      ),
      (
        "Error : API return 422 and cascade business error 65541 from HIP to DWP",
        "BX100",
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "Error : API return 422 and cascade business error 65542 from HIP to DWP",
        "HZ310",
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "Error : API return 422 and cascade business error 65543 from HIP to DWP",
        "BZ230",
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "Error : API return 422 and cascade business error 65544 from HIP to DWP",
        "HG200",
        "65544",
        "Account held on NPS, but has not gone through adult registration."
      ),
      (
        "Error : API return 422 and cascade business error 99999 from HIP to DWP",
        "AB150",
        "99999",
        "Start Date after Death"
      )
    )

    cases.foreach { case (scenarioName, ninoPrefix, businessErrorCode, businessErrorMessage) =>
      Scenario(scenarioName) {

        Given("API receives a valid UCL notification request from DWP")
        val payload: JsObject                 = insertNotificationPayload(nino = ninoWithPrefix(ninoPrefix))
        val apiResponse: StandaloneWSResponse = apiService.postNotification(validHeaders, payload)

        Then("API returns HTTP status code 422 UnprocessableEntity to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe UNPROCESSABLE_ENTITY
        }

        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe businessErrorCode
        (responseBody \ "message").as[String] mustBe businessErrorMessage

        And("CorrelationId in the response header should match the request CorrelationId")
      }
    }

  }
}
