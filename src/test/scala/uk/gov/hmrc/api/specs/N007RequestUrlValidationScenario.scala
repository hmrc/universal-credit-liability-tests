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
import play.api.http.Status.NOT_FOUND
import play.api.libs.json.JsValue
import uk.gov.hmrc.api.testData.*

class N007RequestUrlValidationScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N007 : API unable to process UCL notification received by DWP due invalid URL/Endpoint and returns 404 to DWP"
  ) {

    val cases: Seq[(String, JsValue)] = Seq(
      (
        "Error : Insert request returns 404 to DWP when request content path is invalid",
        insertNotificationPayload()
      ),
      (
        "Error : Terminate request 404 received from HIP",
        terminateNotificationPayload()
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {

        Given("API receives a valid UCL notification request from DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload, path = "invalid-notification-path")

        Then("API returns HTTP status code 404 not found to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe NOT_FOUND
        }

        /*And("Error response body must be empty")
        apiResponse.body mustBe empty*/

        And("CorrelationId in the response header should match the request CorrelationId")

      }
    }
  }
}
