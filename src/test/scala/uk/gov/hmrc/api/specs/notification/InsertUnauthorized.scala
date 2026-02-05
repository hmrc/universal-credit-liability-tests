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
import org.scalactic.Prettifier.default
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.UNAUTHORIZED
import play.api.libs.json.Json
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class InsertUnauthorized extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("401 Unauthorized scenarios") {

    val cases = Seq(
      (
        "UCL_TC_001_0.1: Invalid Token",
        headersInvalidAuth,
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      ),
      (
        "UCL_TC_001_0.2: Missing Auth",
        headersMissingAuthorization,
        "MISSING_CREDENTIALS",
        "Bearer token not supplied"
      ),
      (
        "UCL_TC_001_0.3: Expired Token",
        overrideHeader(baseHeaders, "Authorization", getExpiredAuthToken),
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      ),
      (
        "UCL_TC_???_??: Empty Token",
        headersEmptyAuth,
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      )
    )

    cases.foreach { case (scenarioName, headers, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("an invalid/empty/expired token is sent")

        val apiResponse = apiService.postNotificationWithoutAuth(headers, insertNotificationPayload())

        Then("401 Unauthorized received")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe UNAUTHORIZED
        }

        And("response body must contain correct error details")
        val responseBody = Json.parse(apiResponse.body)

        (responseBody \ "code").as[String] mustBe expCode
        (responseBody \ "message").as[String] mustBe expMessage
      }
    }
  }

}
