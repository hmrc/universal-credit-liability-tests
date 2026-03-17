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
import play.api.http.Status.NO_CONTENT
import play.api.libs.json.JsValue
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.testData.*

class H001UclNotificationSuccessfulScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_H001 : API successfully processes a valid UCL Notification received from DWP and gets successful response HIP"
  ) {

    val cases: Seq[(String, JsValue)] = Seq(
      (
        "Success : Insert UCL Notification successfully with valid Credit Record type UC",
        insertNotificationPayload(recordType = "UC")
      ),
      (
        "Success : Insert UCL Notification successfully with valid Credit Record type LCW/LCWRA",
        insertNotificationPayload(recordType = "LCW/LCWRA")
      ),
      (
        "Success : Terminate UCL Notification successfully with valid Credit Record type UC",
        terminateNotificationPayload(recordType = "UC")
      ),
      (
        "Success : Terminate UCL Notification successfully with valid Credit Record type LCW/LCWRA",
        terminateNotificationPayload(recordType = "LCW/LCWRA")
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {

        Given("API receives a valid UCL notification request from DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("API returns HTTP status code 204 No Content to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe NO_CONTENT
          apiResponse.statusText mustBe "No Content"
        }

        And("Success response body must be empty")
        apiResponse.body mustBe empty

        And("CorrelationId in the response header should match the request CorrelationId")
        val resCorrId = apiResponse.headerValues("correlationId")
        val reqCorrId = validHeaders.toMap.get("correlationId")
        resCorrId.headOption mustBe reqCorrId

      }
    }
  }
}
