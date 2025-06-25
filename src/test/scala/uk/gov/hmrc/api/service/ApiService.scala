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

package uk.gov.hmrc.api.service

import org.apache.pekko.actor.ActorSystem
import play.api.libs.json.JsValue
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{StandaloneWSResponse, writeableOf_JsValue}

import java.net.URI
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.{HttpClient, HttpRequest}
import java.time.Duration.ofSeconds
import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.duration.*
import scala.jdk.OptionConverters.RichOptional

class ApiService {
  implicit val actorSystem: ActorSystem = ActorSystem()
  val wsClient: StandaloneAhcWSClient   = StandaloneAhcWSClient()
  val http: HttpClient                  = HttpClient.newHttpClient()

  val authApiUrl = s"http://localhost:8585/application/session/login"

  def postNotification(
    url: String,
    headers: Seq[(String, String)],
    requestBody: JsValue,
    authToken: String
  ): StandaloneWSResponse = {

    val updatedHeaders = headers :+ ("authorization" -> s"$authToken")
    val response       = wsClient
      .url(url)
      .withHttpHeaders(updatedHeaders: _*)
      .withBody(requestBody)
      .execute("POST")

    Await.result(response, 10.seconds)
  }

  def postNotificationWithValidToken(
    url: String,
    headers: Seq[(String, String)],
    requestBody: JsValue
  ): StandaloneWSResponse = {
    val authToken = getAuthToken
    postNotification(url, headers, requestBody, authToken)
  }

  def getAuthToken: String = {
    val request = HttpRequest
      .newBuilder()
      .uri(new URI(authApiUrl))
      .header("content-type", "application/json")
      .POST(
        BodyPublishers.ofString(
          s"""
             |{
             | "clientId": "${UUID.randomUUID().toString}",
             | "authProvider": "PrivilegedApplication",
             | "applicationId": "${UUID.randomUUID().toString}",
             | "applicationName": "universal-credit-liability-api",
             | "enrolments": [],
             | "ttl": 60000
             |}
             |""".stripMargin
        )
      )
      .timeout(ofSeconds(5, 0))
      .build()

    val response = http.send(request, BodyHandlers.ofString())

    require(response.statusCode() == 201, "Unable to create auth token")

    response.headers().firstValue("Authorization").toScala.get
  }
}
