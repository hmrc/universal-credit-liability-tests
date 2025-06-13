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

//package uk.gov.hmrc.api.helper
//
//import uk.gov.hmrc.api.conf.ServicesConfiguration
//
//import java.net.URI
//import java.net.http.HttpRequest.BodyPublishers
//import java.net.http.HttpResponse.BodyHandlers
//import java.net.http.{HttpClient, HttpRequest}
//import java.time.Duration.ofSeconds
//import java.util.UUID
//import scala.jdk.OptionConverters.RichOptional
//
//object AuthHelper {
//
//  val http: HttpClient = HttpClient.newHttpClient()
//
//  private val authApiBaseUrl: String = baseUrlFor("auth-login-api")
//  private val authApiUrl: String     = s"$authApiBaseUrl/application/session/login"
//
//  def getAuthToken: String = {
//    val request = HttpRequest
//      .newBuilder()
//      .uri(new URI(authApiUrl))
//      .header("content-type", "application/json")
//      .POST(
//        BodyPublishers.ofString(
//          s"""
//          |{
//          | "clientId": "${UUID.randomUUID().toString}",
//          | "authProvider": "PrivilegedApplication",
//          | "applicationId": "${UUID.randomUUID().toString}",
//          | "applicationName": "universal-credit-liability-api",
//          | "enrolments": [],
//          | "ttl": 60000
//          |}
//          |""".stripMargin
//        )
//      )
//      .timeout(ofSeconds(5, 0))
//      .build()
//
//    val response = http.send(request, BodyHandlers.ofString())
//
//    require(response.statusCode() == 201, "Unable to create auth token")
//
//    response.headers().firstValue("Authorization").toScala.get
//  }
//
//}
