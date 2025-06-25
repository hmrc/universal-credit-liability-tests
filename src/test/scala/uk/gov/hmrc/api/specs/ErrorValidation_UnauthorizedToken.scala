package uk.gov.hmrc.api.specs

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import uk.gov.hmrc.api.service.ApiService
import uk.gov.hmrc.api.testData.TestDataFile

class ErrorValidation_UnauthorizedToken extends BaseSpec with GuiceOneServerPerSuite with TestDataFile {
  Feature("401 Unauthorized Token scenarios") {

    Scenario("UCL_TC_001_0.1: Invalid Token") {
      Given("The Universal Credit API is up and running")
      When("A Invalid token is sent")

      val response = ApiService()
        .postNotification(endpointUrl, validHeaders, validInsertLCWLCWRALiabilityRequest, getInvalidAuthToken)

      Then("401 Unauthorized received")
      response.status mustBe Status.UNAUTHORIZED
    }

    Scenario("UCL_TC_001_0.2: Empty Token") {
      Given("The Universal Credit API is up and running")
      When("A Empty token is sent")

      val response = ApiService()
        .postNotification(endpointUrl, validHeaders, validInsertLCWLCWRALiabilityRequest, getNoAuthToken)

      Then("401 Unauthorized received")
      response.status mustBe Status.UNAUTHORIZED
    }

    Scenario("UCL_TC_001_0.3: Expired Token") {
      Given("The Universal Credit API is up and running")
      When("A Empty token is sent")

      val response = ApiService()
        .postNotification(endpointUrl, validHeaders, validInsertLCWLCWRALiabilityRequest, getExpiredAuthToken)

      Then("401 Unauthorized received")
      response.status mustBe Status.UNAUTHORIZED
    }
  }
}
