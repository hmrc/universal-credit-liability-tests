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

package uk.gov.hmrc.api.specLocal

import org.scalactic.Prettifier.default
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.UNAUTHORIZED
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class N001_Insert_AuthorisationValidationScenario
    extends BaseSpec
    with GuiceOneServerPerSuite
    with TestDataNotification {

  Feature(
    "UCL_TC_N001 : Insert Request_MDTP returns 401 with error response body to DWP on request header - 'Authorisation' validation failure"
  ) {

    val cases: Seq[(String, Seq[(String, String)], ErrorResponseCode, ErrorResponseMessage)] = Seq(
      (
        "Error: Authorisation is invalid in request header",
        headersInvalidAuth,
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      ),
      (
        "Error: Authorisation is missing in request header",
        headersMissingAuthorization,
        "MISSING_CREDENTIALS",
        "Bearer token not supplied"
      ),
      (
        "Error: Authorisation expired in request header",
        overrideHeader(baseHeaders, "Authorization", getExpiredAuthToken),
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      ),
      (
        "Error: Authorisation empty in request header",
        headersEmptyAuth,
        "INVALID_CREDENTIALS",
        "Invalid bearer token"
      )
    )

    cases.foreach { case (scenarioName, headers, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("Universal Credit Liability Notification API is up and running")
        // ????? Need to add code  ---
        When("a request with invalid/empty/expired authorisation header is sent")
        val apiResponse = apiService.postNotificationWithoutAuth(headers, insertNotificationPayload())
        System.out.println("For Scenario " + scenarioName + " Error Response Body ==> " + Json.parse(apiResponse.body))

        Then("MDTP returns HTTP status code 401 Unauthorized to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe UNAUTHORIZED
        }
        And("Error response body must contain correct error details")
        val responseBody = Json.parse(apiResponse.body)
        System.out.println("----- responseBody -----  " + responseBody)
        (responseBody \ "code").as[String] mustBe expCode
        (responseBody \ "message").as[String] mustBe expMessage
      }
    }
  }

}
