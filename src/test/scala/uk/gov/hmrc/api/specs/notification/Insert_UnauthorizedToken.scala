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

class Insert_UnauthorizedToken extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("401 Unauthorized Token scenarios") {

    val cases = Seq(
      (
        "UCL_TC_001_0.1: Invalid Token",
        () => getInvalidAuthToken,
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      ),
      (
        "UCL_TC_001_0.2: Empty Token",
        () => getNoAuthToken,
        "MISSING_CREDENTIALS",
        "Bearer token not supplied"
      ),
      (
        "UCL_TC_001_0.3: Expired Token",
        () => getExpiredAuthToken,
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      )
    )

    cases.foreach { case (scenarioName, tokenFn, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("An invalid/empty/expired token is sent")

        val response =
          apiService.makeRequest(validHeaders, validInsertLCWLCWRALiabilityRequest, tokenFn())

        Then("401 Unauthorized received")
        assert(response.status == Status.UNAUTHORIZED)

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
