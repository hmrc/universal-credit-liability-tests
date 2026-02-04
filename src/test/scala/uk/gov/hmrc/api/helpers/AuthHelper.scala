/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.api.helpers

import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.api.client.HttpClient
import uk.gov.hmrc.api.conf.TestEnvironment

import java.util.UUID

object AuthHelper {

  def getAuthToken: String = TestEnvironment.environment match {
    case "local"  => getLocalAuthToken
    // case "dev"     => getOAuthToken
    // case "qa"      => getOAuthToken
    case otherEnv => throw IllegalStateException(s"Unsupported environment: $otherEnv")
  }

  // https://github.com/hmrc/auth-login-api?tab=readme-ov-file#privileged-and-standard-application-login
  private def getLocalAuthToken: String = {
    val authApiUrl: String = s"${TestEnvironment.url("auth")}/application/session/login"
    val guid: String       = UUID.randomUUID().toString

    val payload: JsObject = Json.obj(
      "clientId"        -> guid,
      "authProvider"    -> "PrivilegedApplication",
      "applicationId"   -> guid,
      "applicationName" -> "universal-credit-liability-api",
      "enrolments"      -> Json.arr(),
      "ttl"             -> 5000
    )

    val response = HttpClient.post(
      url = authApiUrl,
      headers = Seq("Content-Type" -> "application/json"),
      body = Json.stringify(payload)
    )

    require(
      response.status == 201,
      s"Local auth failed: ${response.status} - ${response.body}"
    )

    response.header("Authorization").getOrElse(throw new RuntimeException("Authorization header missing"))
  }

}
