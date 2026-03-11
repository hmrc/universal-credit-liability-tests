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
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.testData.*

class N009Mdtp500ErrorMappingScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N009 : API error handling for downstream errors 400, 401 and 403"
  ) {

    val cases: Seq[(String, JsValue)] = Seq(
      (
        "Error : Insert returns 500 to DWP when HIP returns 400",
        insertNotificationPayload(nino = ninoWithPrefix("XY400"))
      ),
      (
        "Error : Terminate returns 500 to DWP when HIP returns 401",
        terminateNotificationPayload(nino = ninoWithPrefix("XY401"))
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {

        Given("API receives a valid UCL notification request from DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("API returns HTTP status code 500 with no payload to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe INTERNAL_SERVER_ERROR
        }

        And("Error response body must be empty")
        apiResponse.body mustBe empty

        And("CorrelationId in the response header should match the request CorrelationId")
      }
    }
  }
}
