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
import uk.gov.hmrc.api.client.HttpClient.post
import uk.gov.hmrc.api.conf.TestEnvironment
import uk.gov.hmrc.api.helpers.AuthHelper

class ApiService {

  private val apiHost: String = TestEnvironment.url("ucl")
  private val hipHost: String = TestEnvironment.url("hip")

  // API
  def postNotification(headers: Seq[(String, String)], requestBody: JsValue): StandaloneWSResponse = {
    val bearerToken: String               = AuthHelper.getAuthToken
    val endpointUrl: String               = s"$apiHost/notification"
    val authHeader: Seq[(String, String)] = Seq("Authorization" -> bearerToken)

    HttpClient.post(endpointUrl, headers ++ authHeader, requestBody.toString())
  }

  // HIP
  def postHipUcLiability(
    headers: Seq[(String, String)],
    nino: String,
    requestBody: JsValue
  ): StandaloneWSResponse = {
    val url: String = s"$hipHost/ni/person/$nino/liability/universal-credit"

    post(url, headers, requestBody.toString)
  }

  def postHipUcTermination(
    headers: Seq[(String, String)],
    nino: String,
    requestBody: JsValue
  ): StandaloneWSResponse = {
    val url: String = s"$hipHost/ni/person/$nino/liability/universal-credit/termination"

    post(url, headers, requestBody.toString)
  }

}
