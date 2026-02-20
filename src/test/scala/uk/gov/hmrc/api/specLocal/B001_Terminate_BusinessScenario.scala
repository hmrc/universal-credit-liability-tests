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

package uk.gov.hmrc.api.specLocal

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.UNPROCESSABLE_ENTITY
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.*

class B001_Terminate_BusinessScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_B001:Terminate Request_MDTP handle and cascade 422 and business error code from HIP to DWP"
  ) {

    val cases: Seq[(String, ErrorResponseCode, ErrorResponseMessage)] = Seq(
      (
        "Success:MDTP return 422 and cascade business error 550006 from HIP to DWP",
        "550006",
        "Start Date and End Date must be earlier than Date of Death"
      ),
      (
        "Success:MDTP return 422 and cascade business error 55008 from HIP to DWP",
        "55008",
        "End Date must be earlier than State Pension Age"
      ),
      (
        "Success:MDTP return 422 and cascade business error 55027 from HIP to DWP",
        "55027",
        "End Date later than Date of Death"
      ),
      (
        "Success:MDTP return 422 and cascade business error 55029 from HIP to DWP",
        "55029",
        "Start Date later than SPA"
      ),
      (
        "Success:MDTP return 422 and cascade business error 55038 from HIP to DWP",
        "55038",
        "A conflicting or identical Liability is already recorded"
      ),
      (
        "Success:MDTP return 422 and cascade business error 55039 from HIP to DWP",
        "55039",
        "NO corresponding liability found"
      ),
      (
        "Success:MDTP return 422 and cascade business error 64996 from HIP to DWP",
        "64996",
        "Start Date is not before date of death"
      ),
      (
        "Success:MDTP return 422 and cascade business error 64997 from HIP to DWP",
        "64997",
        "LCW/LCWRA not within a period of UC"
      ),
      (
        "Success:MDTP return 422 and cascade business error 64998 from HIP to DWP",
        "64998",
        "LCW/LCWRA Override not within a period of LCW/LCWRA"
      ),
      (
        "Success:MDTP return 422 and cascade business error 65026 from HIP to DWP",
        "65026",
        "Start date must not be before 16th birthday"
      ),
      (
        "Success:MDTP return 422 and cascade business error 65536 from HIP to DWP",
        "65536",
        "Start date before 29/04/2013"
      ),
      (
        "Success:MDTP return 422 and cascade business error 65537 from HIP to DWP",
        "65537",
        "End date before start date"
      ),
      (
        "Success:MDTP return 422 and cascade business error 65541 from HIP to DWP",
        "65541",
        "The NINO input matches a Pseudo Account"
      ),
      (
        "Success:MDTP return 422 and cascade business error 65542 from HIP to DWP",
        "65542",
        "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)"
      ),
      (
        "Success:MDTP return 422 and cascade business error 65543 from HIP to DWP",
        "65543",
        "The NINO input matches an account that has been transferred to the Isle of Man"
      ),
      (
        "Success:MDTP return 422 and cascade business error 99999 from HIP to DWP",
        "99999",
        "Start Date after Death"
      )
    )

    cases.foreach { case (scenarioName, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("Universal Credit Liability Notification API is up and running")
        // need to add code

        When("a valid UCL notification is sent by DWP")
        val apiResponse = apiService.postNotification(validHeaders, terminateNotificationPayload())
        System.out.println(
          "For Scenario " + scenarioName + " Business Error  Response Body ==> " + Json.parse(apiResponse.body)
        )

        Then("MDTP returns HTTP status code 422 No Content to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe UNPROCESSABLE_ENTITY
        }

        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe expCode
        (responseBody \ "message").as[String] mustBe expMessage

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
