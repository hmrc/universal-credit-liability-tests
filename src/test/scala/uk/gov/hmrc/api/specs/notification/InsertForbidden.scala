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
import play.api.http.Status.FORBIDDEN
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class InsertForbidden extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature("403 Forbidden scenarios for 'Insert' record type") {

    val cases: Seq[(String, Seq[(String, String)], JsValue)] = Seq(
      (
        "???: Invalid GovUkOriginatorId (Special Chars)",
        headersInvalidCharsOriginatorId,
        validInsertUCLiabilityRequest
      ),
      (
        "???: Missing GovUkOriginatorId",
        headersMissingGovUkOriginatorId,
        validInsertUCLiabilityRequest
      ),
      (
        "???: Invalid GovUkOriginatorId (Long)",
        headersInvalidLongOriginatorId,
        validInsertUCLiabilityRequest
      ),
      (
        "???: Invalid GovUkOriginatorId (Short)",
        headersInvalidShortOriginatorId,
        validInsertUCLiabilityRequest
      )
    )

    cases.foreach { case (scenarioName, headers, payload) =>
      Scenario(scenarioName) {
        Given("The Universal Credit API is up and running")
        When("A request is sent")

        val apiResponse = apiService.postNotification(headers, payload)

        Then("403 Forbidden should be returned")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe FORBIDDEN
        }

        And("Response body should contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)

        (responseBody \ "code").as[String] mustBe ForbiddenCode
        (responseBody \ "message").as[String] mustBe "Forbidden"
      }
    }
  }
}
