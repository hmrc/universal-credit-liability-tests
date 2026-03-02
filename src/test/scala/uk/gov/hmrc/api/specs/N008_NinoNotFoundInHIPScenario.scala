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
import play.api.libs.ws.DefaultBodyReadables.readableAsByteArray
import uk.gov.hmrc.api.testData.*

class N008_NinoNotFoundInHIPScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N009:HIP fails to process the request from MDTP when nino is not found and returns 404 to MDTP and MDTP cascades the response to DWP"
  ) {

    val cases: Seq[(String, JsValue)] = Seq(
      (
        "Error:Insert Request_MDTP cascades the HTTP 404 status with error payload from HIP to DWP when nino is not found in HIP",
        insertNotificationPayload(nino = ninoWithPrefix("XY404"))
      ),
      (
        "Error: Terminate Request_MDTP cascades the HTTP 404 to DWP when nino is not found in HIP",
        terminateNotificationPayload(nino = ninoWithPrefix("XY404"))
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {

        Given("a valid UCL notification is sent by DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("MDTP returns HTTP status code 404 Not Found to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe NOT_FOUND
        }

        And("Error response body must be empty")
        apiResponse.body mustBe empty

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
