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
import play.api.http.Status.NOT_FOUND
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.testData.*

class N008NinoNotFoundInHipScenario extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "UCL_TC_N008 : HIP fails to process the request from API when NINO is not found and returns 404 to API and API cascades the response to DWP"
  ) {

    val cases: Seq[(String, JsValue, ResponseErrorCode, ResponseErrorMessage)] = Seq(
      (
        "Error : Insert cascades the HTTP 404 status with error payload from HIP to DWP when NINO is not found in HIP",
        insertNotificationPayload(nino = ninoWithPrefix("CM110")),
        "404",
        "Resource not found"
      ),
      (
        "Error : Terminate cascades the HTTP 404 to DWP when NINO is not found in HIP",
        terminateNotificationPayload(nino = ninoWithPrefix("CM110")),
        "404",
        "Resource not found"
      )
    )

    cases.foreach { case (scenarioName, payload,errorCode, errorMessage) =>
      Scenario(scenarioName) {

        Given("API receives a valid UCL notification request from DWP")
        val apiResponse = apiService.postNotification(validHeaders, payload)

        Then("API returns HTTP status code 404 Not Found to DWP")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe NOT_FOUND
          System.out.println("---------- " + Json.parse(apiResponse.body))
        }


        And("Error response body must contain correct error details")
        val responseBody: JsValue = Json.parse(apiResponse.body)
        (responseBody \ "code").as[String] mustBe errorCode
        (responseBody \ "message").as[String] mustBe errorMessage

        And("CorrelationId in the response header should match the request CorrelationId")

      }
    }
  }
}
