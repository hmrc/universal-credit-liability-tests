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

package uk.gov.hmrc.api.specs.notification

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.NO_CONTENT
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.*

class TerminateNoContent extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("204 NoContent 'Terminate' scenarios") {

    val cases = Seq(
      (
        "UCL_Terminate_TC_001_0.1: Valid Credit Record type LCW/LCWRA",
        terminateNotificationPayload(recordType = "LCW/LCWRA")
      ),
      (
        "UCL_Terminate_TC_001_0.2: Valid Credit Record type UC",
        terminateNotificationPayload(recordType = "UC")
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("a request is sent")

        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("204 NoContent must be returned")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe NO_CONTENT
        }

        And("response body must be empty")
        apiResponse.body mustBe empty
      }
    }
  }
}
