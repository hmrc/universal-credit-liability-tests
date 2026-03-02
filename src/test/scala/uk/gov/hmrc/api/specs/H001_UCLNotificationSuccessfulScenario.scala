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
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.testData.*

class H001_UCLNotificationSuccessfulScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_H001:MDTP successfully processes a valid UCL Notification received from DWP and gets successful response HIP"
  ) {

    val cases: Seq[(String, Seq[(String, String)], JsValue)] = Seq(
      (
        "Success:Insert Request_UCL Notification process successfully with valid Credit Record type UC",
        validHeaders,
        insertNotificationPayload(recordType = "UC")
      ),
      (
        "Success: Insert Request_UCL Notification process successfully with valid Credit Record type LCW/LCWRA",
        validHeaders,
        insertNotificationPayload(recordType = "LCW/LCWRA")
      ),
      (
        "Success: Terminate Request_UCL Notification process successfully with valid Credit Record type UC",
        validHeaders,
        terminateNotificationPayload(recordType = "UC")
      ),
      (
        "Success: Terminate Request_UCL Notification process successfully with valid Credit Record type LCW/LCWRA",
        validHeaders,
        terminateNotificationPayload(recordType = "LCW/LCWRA")
      )
    )

    cases.foreach { case (scenarioName, headers, payload) =>
      Scenario(scenarioName) {
        
        Given("a valid UCL notification is sent by DWP")
        val apiResponse = apiService.postNotification(headers, payload)

        Then("MDTP returns HTTP status code 204 No Content to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe NO_CONTENT
          apiResponse.statusText mustBe "No Content"
        }

        And("Success response body must be empty")
        apiResponse.body mustBe empty

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
