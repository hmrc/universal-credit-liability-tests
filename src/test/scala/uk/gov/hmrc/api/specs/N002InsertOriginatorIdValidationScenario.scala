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

class N002InsertOriginatorIdValidationScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N002 : Insert_HIP fails to process the request from API when originatorId validation fails and returns 403 to API and API cascades the response to DWP"
  ) {

    val cases: Seq[(String, Seq[(String, String)])] = Seq(
      (
        "Error : GovUkOriginatorId (Special Chars) is invalid in API's request header",
        headersInvalidCharsOriginatorId
      ),
      (
        "Error : GovUkOriginatorId is missing in API's request header",
        headersMissingGovUkOriginatorId
      ),
      (
        "Error : GovUkOriginatorId (Long) is invalid in API's request header",
        headersInvalidLongOriginatorId
      ),
      (
        "Error : GovUkOriginatorId (Short) is invalid in API's request header",
        headersInvalidShortOriginatorId
      ),
      (
        "Error : GovUkOriginatorId (Short) is empty in API's request header",
        headersEmptyOriginatorId
      ),
      (
        "Error : GovUkOriginatorId is not found in HIP",
        headerNotFoundInHipOriginatorId
      )
    )

    cases.foreach { case (scenarioName, headers) =>
      Scenario(scenarioName) {

        Given("API receives a request with invalid/missing/empty GovUkOriginatorId header from DWP")
        val apiResponse = apiService.postNotification(headers, insertNotificationPayload())

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
