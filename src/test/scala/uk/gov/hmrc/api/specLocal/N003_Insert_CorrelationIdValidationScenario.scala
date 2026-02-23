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

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.BAD_REQUEST
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataNotification

class N003_Insert_CorrelationIdValidationScenario
    extends BaseSpec
    with GuiceOneServerPerSuite
    with TestDataNotification {

  Feature(
    "UCL_TC_N003 : Insert Request_MDTP returns 400 with error response body to DWP on request header - 'correlation Id' validation failure"
  ) {

    val cases: Seq[(String, Seq[(String, String)], ErrorResponseCode, ErrorResponseMessage)] = Seq(
      (
        "Error: Correlation Id is invalid in request header",
        headersInvalidCorrelationId,
        "400.1",
        constraintViolation("correlationId")
      ),
      (
        "Error: Correlation Id is missing in request header",
        headersMissingCorrelationId,
        "400.1",
        constraintViolation("correlationId")
      ),
      (
        "Error: Correlation Id is empty in request header",
        headersEmptyCorrelationId,
        "400.1",
        constraintViolation("correlationId")
      )
    )

    cases.foreach { case (scenarioName, headers, expCode, expMessage) =>
      Scenario(scenarioName) {
        Given("Universal Credit Liability Notification API is up and running")
        // need to add a code ???
        When("a request with invalid/missing/empty CorrelationId header is sent")
        val apiResponse = apiService.postNotification(headers, insertNotificationPayload())
        System.out.println("For Scenario " + scenarioName + " Error Response Body ==> " + Json.parse(apiResponse.body))

        Then("MDTP returns HTTP status code 400 Bad Request to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe BAD_REQUEST
        }
        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe expCode
        (responseBody \ "message").as[String] mustBe expMessage

        And("CorrelationId in the response header should match the request CorrelationId")
        // need to add code
      }
    }
  }
}
