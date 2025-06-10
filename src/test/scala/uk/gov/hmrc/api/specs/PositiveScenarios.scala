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
import play.api.http.Status
import uk.gov.hmrc.api.service.ApiService
import uk.gov.hmrc.api.testData.*

class PositiveScenarios extends BaseSpec with GuiceOneServerPerSuite with TestDataFile {
  Feature("Positive Scenario") {

    // 204 Request
    Scenario("UCL_TC_001_0.1: Insert - LCW/LCWRA") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response = ApiService().postNotification(insertURL, validHeaders, validInsertLCWLCWRALiabilityRequest)

      Then("204 No content should display")
      response.status mustBe Status.NO_CONTENT
    }

    Scenario("UCL_TC_001_0.0.1: Insert - LCW/LCWRA with No End Date") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response =
        ApiService().postNotification(insertURL, validHeaders, validInsertLCWLCWRALiabilityRequestNOEndDate)

      Then("204 No content should display")
      response.status mustBe Status.NO_CONTENT
    }

    Scenario("UCL_TC_001_0.2: Insert - UC") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response = ApiService().postNotification(insertURL, validHeaders, validInsertUCLiabilityRequest)

      Then("204 No content should display")
      response.status mustBe Status.NO_CONTENT
    }

    Scenario("UCL_TC_001_0.3: Terminate - LCW/LCWRA") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response =
        ApiService().postNotification(terminationURL, validHeaders, validTerminationLCWLCWRALiabilityRequest)

      Then("204 No content should display")
      response.status mustBe Status.NO_CONTENT
    }

    Scenario("UCL_TC_001_0.4: Terminate - UC") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response = ApiService().postNotification(terminationURL, validHeaders, validTerminationUCLiabilityRequest)

      Then("204 No content should display")
      response.status mustBe Status.NO_CONTENT
    }

    // 400 Request
    Scenario("UCL_TC_001_0.5: Insert Invalid details - LCW/LCWRA") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response = ApiService().postNotification(insertURL, validHeaders, invalidInsertLCWLCWRALiabilityRequest)

      Then("400 No content should display")
      response.status mustBe Status.BAD_REQUEST
    }

    Scenario("UCL_TC_001_0.6: Insert Invalid details - UC") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response = ApiService().postNotification(insertURL, validHeaders, invalidInsertUCLiabilityRequest)

      Then("400 No content should display")
      response.status mustBe Status.BAD_REQUEST
    }

    Scenario("UCL_TC_001_0.7: Termination Invalid details - LCW/LCWRA") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response =
        ApiService().postNotification(terminationURL, validHeaders, inValidTerminationLCWLCWRALiabilityRequest)

      Then("400 No content should display")
      response.status mustBe Status.BAD_REQUEST
    }

    Scenario("UCL_TC_001_0.8: Termination Invalid details - UC") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response = ApiService().postNotification(terminationURL, validHeaders, inValidTerminationUCLiabilityRequest)

      Then("400 No content should display")
      response.status mustBe Status.BAD_REQUEST
    }

    // 403 Request
    Scenario("UCL_TC_001_0.9: Insert Invalid Headers details - UC") {
      Given("The Universal Credit API is up and running")
      When("A request is sent")

      val response = ApiService().postNotification(insertURL, invalidHeaders, invalidInsertUCLiabilityRequest)

      Then("403 No content should display")
      response.status mustBe Status.FORBIDDEN
    }
  }
}
