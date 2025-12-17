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

import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class Terminate_BadRequests extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("400 Bad Request scenarios for Terminate Record Type") {

    val cases = Seq(
      (
        "UCL_TC_001_0.7: Termination Invalid LCW/LCWRA details",
        inValidTerminationLCWLCWRALiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      ),
      (
        "UCL_TC_001_0.8: Termination Invalid UC details",
        inValidTerminationUCLiabilityRequest,
        "400.1",
        "Constraint Violation - Invalid/Missing input parameter: universalCreditRecordType"
      )
    )

    cases.foreach { case (scenarioName, payload, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val response = apiService.postNotificationWithValidToken(validHeaders, payload)

        Then("400 Bad Request should be returned")
        assert(response.status == Status.BAD_REQUEST)

        Then("Response body should contain correct error details")
        val actualJson = Json.parse(response.body)
        val actualCode = (actualJson \ "code").as[String]
        val actualMsg  = (actualJson \ "message").as[String]

        assert(actualCode == expCode)
        assert(actualMsg == expMessage)
      }
    }
  }
}
