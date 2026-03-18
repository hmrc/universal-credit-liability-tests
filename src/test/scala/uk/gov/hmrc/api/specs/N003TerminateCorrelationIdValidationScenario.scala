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
import play.api.http.Status.BAD_REQUEST
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.testData.TestDataNotification

class N003TerminateCorrelationIdValidationScenario
    extends BaseSpec
    with GuiceOneServerPerSuite
    with TestDataNotification {

  Feature(
    "UCL_TC_N003 : Terminate returns 400 with error response body to DWP on request header - 'correlation Id' validation failure"
  ) {

    val cases: Seq[(String, Seq[(String, String)])] = Seq(
      (
        "Error : correlationId is invalid in request header",
        headersInvalidCorrelationId
      ),
      (
        "Error : correlationId is missing in request header",
        headersMissingCorrelationId
      ),
      (
        "Error : correlationId is empty in request header",
        headersEmptyCorrelationId
      )
    )

    cases.foreach { case (scenarioName, headers) =>
      Scenario(scenarioName) {

        Given("API receives a request with invalid/missing/empty CorrelationId header from DWP")
        val apiResponse = apiService.postNotification(headers, terminateNotificationPayload())

        Then("API returns HTTP status code 400 Bad Request to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe BAD_REQUEST
        }
        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe InvalidInputCode
        (responseBody \ "message").as[String] mustBe constraintViolation("correlationId")

        And("CorrelationId in the response header should match the request CorrelationId")
        val requestCorrelationId: Option[String]  = headers.toMap.get("correlationId")
        val responseCorrelationId: Option[String] = apiResponse.header("correlationId")
        responseCorrelationId mustBe requestCorrelationId

      }
    }
  }
}
