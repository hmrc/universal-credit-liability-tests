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
import play.api.{Configuration, Environment}
import uk.gov.hmrc.api.client.HttpClient
import uk.gov.hmrc.api.conf.TestEnvironment
import uk.gov.hmrc.totp.TotpGenerator

import java.util.{Base64, UUID}

object AuthHelper {

  private val config: Configuration = Configuration.load(Environment.simple())

  private val hipClientId: String     = config.get[String]("hip.clientId")
  private val hipClientSecret: String = config.get[String]("hip.clientSecret")
  private val clientId: String        = config.get[String]("oauth.clientId")
  private val clientSecret: String    = config.get[String]("oauth.clientSecret")
  private val totpSecret: String      = config.get[String]("oauth.totpSecret")

  def getAuthToken: String = TestEnvironment.environment match {
    case "local"                                                  => getLocalAuthToken
    case env if Set("development", "qa", "staging").contains(env) => getOAuthToken(env)
    case "externaltest"                                           => getExternalTestOAuthToken
    case otherEnv                                                 => throw IllegalStateException(s"Unsupported environment: $otherEnv")
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

  private def getOAuthToken(environment: String): String = {
    val oauthTokenUrl: String = s"https://api.$environment.tax.service.gov.uk/oauth/token"
    val totp: String          = TotpGenerator.getTotpCode(totpSecret)
    val body: String          = s"client_id=$clientId&client_secret=$totp$clientSecret&grant_type=client_credentials"

    val response = HttpClient.post(
      url = oauthTokenUrl,
      headers = Seq("Content-Type" -> "application/x-www-form-urlencoded"),
      body = body
    )

    require(
      response.status == 200,
      s"OAuth ($environment) failed: ${response.status} - ${response.body}"
    )

    val accessToken = (Json.parse(response.body) \ "access_token").as[String]
    s"Bearer $accessToken"
  }

  private def getExternalTestOAuthToken: String = {
    val extTestOauthTokenUrl: String = "https://test-api.service.hmrc.gov.uk/oauth/token"
    val totp: String                 = TotpGenerator.getTotpCode(totpSecret)
    val body: String                 = s"client_id=$clientId&client_secret=$totp$clientSecret&grant_type=client_credentials"

    val response = HttpClient.post(
      url = extTestOauthTokenUrl,
      headers = Seq("Content-Type" -> "application/x-www-form-urlencoded"),
      body = body
    )

    require(
      response.status == 200,
      s"OAuth (externaltest) failed: ${response.status} - ${response.body}"
    )

    val accessToken = (Json.parse(response.body) \ "access_token").as[String]
    s"Bearer $accessToken"
  }

  def getHipAuthToken: String = {
    val credentials = s"$hipClientId:$hipClientSecret"
    val encoded     = Base64.getEncoder.encodeToString(credentials.getBytes("UTF-8"))
    s"Basic $encoded"
  }

}
