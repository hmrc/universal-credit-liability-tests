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
import play.api.http.Status
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.api.testData.TestDataHip

class Insert_NoContent extends BaseSpec with GuiceOneServerPerSuite with TestDataHip {

  Feature("204 No Content HIP Insert scenarios") {

    Scenario("UC_TC_001_0.1: Insert - LCW/LCWRA") {
      Given("The HIP API is up and running")
      When("A request is sent to create LCW/LCWRA liability without end date")

      val response =
        apiService.postHipUcLiability(validHeaders, randomNino, validInsertLCWLCWRAWithoutEndDateHipRequest)
      Then("204 No Content should be returned")
      withClue(s"Status=${response.status}, Body=${response.body}\n") {
        response.status mustBe Status.NO_CONTENT
      }
    }

    Scenario("UC_TC_001_0.2: Insert - LCW/LCWRA with End date") {
      Given("The HIP API is up and running")
      When("A request is sent to create LCW/LCWRA liability with end date")

      val response = apiService.postHipUcLiability(validHeaders, randomNino, validInsertLCWLCWRAWithEndDateHipRequest)
      Then("204 No Content should be returned")
      withClue(s"Status=${response.status}, Body=${response.body}\n") {
        response.status mustBe Status.NO_CONTENT
      }
    }

    Scenario("UC_TC_003_0.1: Insert - UC") {
      Given("The HIP API is up and running")
      When("A request is sent to create UC liability without end date")

      val response = apiService.postHipUcLiability(validHeaders, randomNino, validInsertUCWithOutEndDateHipRequest)
      Then("204 No Content should be returned")
      withClue(s"Status=${response.status}, Body=${response.body}\n") {
        response.status mustBe Status.NO_CONTENT
      }
    }

    Scenario("UC_TC_003_0.2: Insert - UC with End date") {
      Given("The HIP API is up and running")
      When("A request is sent to create UC liability with end date")

      val response = apiService.postHipUcLiability(validHeaders, randomNino, validInsertUCWithEndDateHipRequest)
      Then("204 No Content should be returned")
      withClue(s"Status=${response.status}, Body=${response.body}\n") {

        response.status mustBe Status.NO_CONTENT
      }
    }
  }
}
