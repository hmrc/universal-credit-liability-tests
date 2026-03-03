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
import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.libs.json.JsValue
import play.api.libs.ws.DefaultBodyReadables.readableAsByteArray
import uk.gov.hmrc.api.testData.*

class N006HIPInternalServerErrorScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N006:MDTP successfully process a valid UCL Notification received from DWP but returns 500 when unexpected internal error occur in HIP"
  ) {

    val cases: Seq[(String, JsValue)] = Seq(
      (
        "Error:Insert Request_MDTP returns 500 to DWP when internal error occur 500 in HIP",
        insertNotificationPayload(nino = ninoWithPrefix("XY500"))
      ),
      (
        "Error: Terminate Request_MDTP handles internal server error from HIP",
        terminateNotificationPayload(nino = ninoWithPrefix("XY500"))
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {

        Given("a valid UCL notification is sent by DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("MDTP returns HTTP status code 503 Service unavailable to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe INTERNAL_SERVER_ERROR
          apiResponse.statusText mustBe "Internal Server Error"
        }

        And("Error response body must be empty")
        apiResponse.body mustBe empty

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
