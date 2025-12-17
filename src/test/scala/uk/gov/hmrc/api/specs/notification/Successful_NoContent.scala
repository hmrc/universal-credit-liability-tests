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
import play.api.http.Status
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.*

class Successful_NoContent extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("204 No Content scenarios") {

    val cases = Seq(
      ("UCL_TC_001_0.1: Insert - LCW/LCWRA", validInsertLCWLCWRALiabilityRequest),
      ("UCL_TC_001_0.2: Insert - UC", validInsertUCLiabilityRequest),
      ("UCL_TC_001_0.3: Terminate - LCW/LCWRA", validTerminationLCWLCWRALiabilityRequest),
      ("UCL_TC_001_0.4: Terminate - UC", validTerminationUCLiabilityRequest)
    )

    cases.foreach { case (name, req) =>
      Scenario(name) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postNotificationWithValidToken(validHeaders, req)

        Then("204 No content should display")
        withClue(s"Status=${response.status}, Body=${response.body}\n") {
          response.status mustBe Status.NO_CONTENT
        }
      }
    }
  }
}
