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
import play.api.http.Status.NO_CONTENT
import play.api.libs.json.JsObject
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataHip

class InsertNoContent extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("204 NoContent HIP Insert scenarios") {

    val cases: Seq[(NinoPrefix, JsObject)] = Seq(
      (
        "UC_TC_001_0.1: Insert - LCW/LCWRA",
        insertHipPayload(recordType = "LCW/LCWRA")
      ),
      (
        "UC_TC_001_0.2: Insert - LCW/LCWRA with End date",
        insertHipPayload(recordType = "UC", endDate = Some("2026-06-30"))
      ),
      (
        "UC_TC_001_???: Insert - LCW/LCWRA without End Date",
        insertHipPayload(recordType = "UC", endDate = None)
      ),
      (
        "UC_TC_003_0.1: Insert - UC",
        insertHipPayload(recordType = "LCW/LCWRA")
      ),
      (
        "UC_TC_003_0.2: Insert - UC with End date",
        insertHipPayload(recordType = "UC")
      ),
      (
        "UC_TC_003_???: Insert - UC without End date",
        insertHipPayload(recordType = "UC", endDate = None)
      )
    )

    cases.foreach { case (scenarioName, payload) =>
      Scenario(scenarioName) {
        Given("the Universal Credit API is up and running")
        When("a request is sent")

        val apiResponse = apiService.postHipUcLiability(validHeaders, randomNino, payload)

        Then("204 NoContent must be returned")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe NO_CONTENT
        }

        And("response body must be empty")
        apiResponse.body mustBe empty
      }
    }
  }

}
