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

class ErrorValidation_Forbidden extends BaseSpec with GuiceOneServerPerSuite with TestDataFile {

  // 403 Request
  Scenario("UCL_TC_001_0.9: Insert Invalid Headers details - UC") {
    Given("The Universal Credit API is up and running")
    When("A request is sent")

    val response =
      apiService.postNotificationWithValidToken(endpointUrl, invalidHeaders, validInsertUCLiabilityRequest)

    Then("403 No content should display")
    response.status mustBe Status.FORBIDDEN
  }
}
