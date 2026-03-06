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
import play.api.http.Status.FORBIDDEN
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.testData.TestDataNotification

class N010OriginatorIdWithUnicodeScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N010 : HIP successfully process the request from API when originatorId has unicode characters and API cascades the response to DWP"
  ) {

    val cases: Seq[(String, Seq[(String, String)], JsValue)] = Seq(
      (
        "Error : GovUkOriginatorId contains currency symbol (£) in API's request header",
        headersOriginatorIdContainsCurrencySymbol,
        insertNotificationPayload()
      ),
      (
        "Error : GovUkOriginatorId contains emoji in API's request header",
        headersOriginatorIdContainsEmoji,
        insertNotificationPayload()
      ),
      (
        "Error : GovUkOriginatorId contains accented letter in API's request header",
        headersOriginatorIdContainsAccentedLetter,
        terminateNotificationPayload()
      ),
      (
        "Error : GovUkOriginatorId contains special symbols in API's request header",
        headersOriginatorIdContainsSpecialSymbols,
        terminateNotificationPayload()
      ),
      (
        "Error : GovUkOriginatorId contains special character in API's request header",
        headersOriginatorIdContainsSpecialChar,
        insertNotificationPayload()
      ),
      (
        "Error : GovUkOriginatorId contains unicodeLetter in API's request header",
        headersOriginatorIdContainsUnicodeLetter,
        terminateNotificationPayload()
      ),
      (
        "Error : GovUkOriginatorId contains punctuation symbol iin API's request header",
        headersOriginatorIdContainsPunctuationSymbol,
        insertNotificationPayload()
      ),
      (
        "Error : GovUkOriginatorId contains space in API's request header",
        headersOriginatorIdContainsSpace,
        insertNotificationPayload()
      )
    )

    cases.foreach { case (scenarioName, headers, payload) =>
      Scenario(scenarioName) {

        Given("API receives a request with invalid/missing/empty GovUkOriginatorId header from DWP")
        val apiResponse = apiService.postNotification(headers, payload)

        Then("API returns HTTP status code 403 Forbidden to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe FORBIDDEN
        }
        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe ForbiddenCode
        (responseBody \ "message").as[String] mustBe "Forbidden"

        And("CorrelationId in the response header should match the request CorrelationId")
      }
    }
  }
}
