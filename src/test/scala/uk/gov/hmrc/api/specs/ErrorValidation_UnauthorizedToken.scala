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
import uk.gov.hmrc.api.testData.TestDataFile

class ErrorValidation_UnauthorizedToken extends BaseSpec with GuiceOneServerPerSuite with TestDataFile {

  Feature("401 Unauthorized Token scenarios") {

    Scenario("UCL_TC_001_0.1: Invalid Token") {
      Given("The Universal Credit API is up and running")
      When("A Invalid token is sent")

      val response = apiService
        .makeRequest(validHeaders, validInsertLCWLCWRALiabilityRequest, getInvalidAuthToken)

      Then("401 Unauthorized received")
      response.status mustBe Status.UNAUTHORIZED
    }

    Scenario("UCL_TC_001_0.2: Empty Token") {
      Given("The Universal Credit API is up and running")
      When("A Empty token is sent")

      val response = apiService
        .makeRequest(validHeaders, validInsertLCWLCWRALiabilityRequest, getNoAuthToken)

      Then("401 Unauthorized received")
      response.status mustBe Status.UNAUTHORIZED
    }

    Scenario("UCL_TC_001_0.3: Expired Token") {
      Given("The Universal Credit API is up and running")
      When("A Empty token is sent")

      val response = apiService
        .makeRequest(validHeaders, validInsertLCWLCWRALiabilityRequest, getExpiredAuthToken)

      Then("401 Unauthorized received")
      response.status mustBe Status.UNAUTHORIZED
    }
  }
}
