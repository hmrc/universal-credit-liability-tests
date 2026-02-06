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

package uk.gov.hmrc.api.specs.hip

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.FORBIDDEN
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataHip

class InsertForbidden extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("403 Forbidden 'Insert' scenarios for HIP") {

    val cases: Seq[(String, Seq[(String, String)])] = Seq(
//      (
//        "UC_TC_016: Invalid Headers details - UC",
//        invalidHeaders,
//        validUCLiabilityRequest,
//        "Forbidden"
//      ),
      (
        "UC_TC_???: Missing header gov-uk-originator-id",
        headersMissingGovUkOriginatorId
      ),
      (
        "UC_TC_???: Invalid header - short gov-uk-originator-id",
        headersInvalidShortOriginatorId
      ),
      (
        "UC_TC_???: Invalid header - long missing gov-uk-originator-id",
        headersInvalidLongOriginatorId
      )
    )

    cases.foreach { case (scenarioName, header) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("a request is sent")

        val hipResponse = apiService.postHipUcLiability(header, randomNino, insertHipPayload())

        Then("403 Forbidden must be returned")
        withClue(s"Status=${hipResponse.status}, Body=${hipResponse.body}\n") {
          hipResponse.status mustBe FORBIDDEN
        }

        And("response body must contain correct 'error' and 'reason'")
        val responseBody: JsValue = Json.parse(hipResponse.body)

        (responseBody \ "code").as[String] mustBe "403.2"
        (responseBody \ "reason").as[String] mustBe "Forbidden"
      }
    }

  }
}
