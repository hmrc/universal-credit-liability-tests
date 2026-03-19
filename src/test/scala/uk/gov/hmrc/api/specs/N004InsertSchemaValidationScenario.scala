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

class N004InsertSchemaValidationScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N004 : Insert returns 400 with error response body to DWP on request schema validation failure"
  ) {

    val cases: Seq[(String, JsValue, ResponseErrorMessage)] = Seq(
      (
        "Error : nationalInsuranceNumber is invalid in request body",
        insertNotificationPayload(nino = "WW2344Z"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error : nationalInsuranceNumber is missing in request body",
        insertNotificationPayloadMissing("nationalInsuranceNumber"),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error : nationalInsuranceNumber is empty in request body",
        insertNotificationPayload(nino = ""),
        constraintViolation("nationalInsuranceNumber")
      ),
      (
        "Error : universalCreditRecordType is invalid in request body",
        insertNotificationPayload(recordType = "ABCD"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error : universalCreditRecordType is empty in request body",
        insertNotificationPayload(recordType = ""),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error : universalCreditRecordType is missing in request body",
        insertNotificationPayloadMissing("universalCreditRecordType"),
        constraintViolation("universalCreditRecordType")
      ),
      (
        "Error : universalCreditAction is invalid in request body",
        insertNotificationPayload(creditAction = "Add"),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error : universalCreditAction is missing in request body",
        insertNotificationPayloadMissing("universalCreditAction"),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error : universalCreditAction is empty in request body",
        insertNotificationPayload(creditAction = ""),
        constraintViolation("universalCreditAction")
      ),
      (
        "Error : liabilityStartDate is missing in request body",
        insertNotificationPayloadMissing("liabilityStartDate"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "Error : liabilityStartDate has invalid format in request body",
        insertNotificationPayload(startDate = "01-01-1990"),
        constraintViolation("liabilityStartDate")
      ),
      (
        "Error : liabilityStartDate is empty in request body",
        insertNotificationPayload(startDate = ""),
        constraintViolation("liabilityStartDate")
      )
    )

    cases.foreach { case (scenarioName, payload, errorResponseMessage) =>
      Scenario(scenarioName) {

        Given("API receives a request with invalid request body from DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("API returns HTTP status code 400 Bad Request to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe BAD_REQUEST
        }
        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe InvalidInputCode
        (responseBody \ "message").as[String] mustBe errorResponseMessage

        And("CorrelationId in the response header should match the request CorrelationId")
        val requestCorrelationId: Option[String]  = validHeaders.toMap.get("correlationId")
        val responseCorrelationId: Option[String] = apiResponse.header("correlationId")
        responseCorrelationId mustBe requestCorrelationId

      }
    }
  }
}
