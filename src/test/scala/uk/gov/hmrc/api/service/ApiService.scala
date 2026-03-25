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

class ApiService {

  private val apiHost: String = TestEnvironment.url("ucl")

  // API
  def postNotification(
    headers: Seq[(String, String)],
    requestBody: JsValue,
    path: String = "notification"
  ): StandaloneWSResponse = {
    val authHeader          = Seq("Authorization" -> AuthHelper.getAuthToken)
    val endpointUrl: String = s"$apiHost/$path"

    HttpClient.post(endpointUrl, headers ++ authHeader, requestBody.toString())
  }

  // Used for testing unauthorised scenarios
  def postNotificationWithoutAuth(headers: Seq[(String, String)], requestBody: JsValue): StandaloneWSResponse = {
    val endpointUrl: String = s"$apiHost/notification"
    HttpClient.post(endpointUrl, headers, requestBody.toString())
  }
}
