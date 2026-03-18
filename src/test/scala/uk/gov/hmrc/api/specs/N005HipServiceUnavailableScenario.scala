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
import play.api.http.Status.SERVICE_UNAVAILABLE
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.testData.*

class N005HipServiceUnavailableScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N005 : API successfully processes a valid UCL Notification received from DWP but returns 503 when HIP server is unavailable"
  ) {

    val cases: Seq[(String, JsValue)] = Seq(
      (
        "Error : Insert returns 503 to DWP when HIP server is unavailable",
        insertNotificationPayload(nino = ninoWithPrefix("XY503"))
      ),
      (
        "Error : Terminate handles HIP server unavailable error",
        terminateNotificationPayload(nino = ninoWithPrefix("XY503"))
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {

        When("API receives a valid UCL notification request from DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("API returns HTTP status code 503 Service unavailable to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe SERVICE_UNAVAILABLE
        }

        And("Error response body must contain correct error details")
        val responseBody = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe "SERVER_ERROR"
        (responseBody \ "message")
          .as[String] mustBe "The 'misc/universal-credit/liability' API is currently unavailable"

        And("CorrelationId in the response header should match the request CorrelationId")
        val requestCorrelationId: Option[String]  = validHeaders.toMap.get("correlationId")
        val responseCorrelationId: Option[String] = apiResponse.header("correlationId")
        responseCorrelationId mustBe requestCorrelationId

      }
    }
  }
}
