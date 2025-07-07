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

import play.api.libs.json.JsValue
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.api.client.HttpClient
import uk.gov.hmrc.api.conf.TestEnvironment
import uk.gov.hmrc.api.helpers.AuthHelper

import scala.concurrent.Await
import scala.concurrent.duration.*

class ApiService extends HttpClient {

  val host: String = TestEnvironment.url("ucl")
  val authHelper   = new AuthHelper

  def postNotificationWithValidToken(
    url: String,
    headers: Seq[(String, String)],
    requestBody: JsValue
  ): StandaloneWSResponse = {

    val token = authHelper.retrieveAuthBearerToken()
    makeRequest(headers, requestBody, token)
  }

  def makeRequest(headers: Seq[(String, String)], requestBody: JsValue, token: String): StandaloneWSResponse = {
    val url: String = s"$host/notification"
    Await.result(
      post(
        url,
        requestBody.toString,
        headers :+ ("authorization" -> s"$token")
      ),
      10.seconds
    )
  }
}
