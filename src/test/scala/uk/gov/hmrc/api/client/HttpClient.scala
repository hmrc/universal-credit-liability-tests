/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.api.client

import org.apache.pekko.actor.ActorSystem
import play.api.libs.ws.DefaultBodyWritables.*
import play.api.libs.ws.StandaloneWSResponse
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.Await
import scala.concurrent.duration.{DurationInt, FiniteDuration}

object HttpClient {

  private given actorSystem: ActorSystem      = ActorSystem("universal-credit-liability-tests")
  private val wsClient: StandaloneAhcWSClient = StandaloneAhcWSClient()
  private val timeout: FiniteDuration         = 10.seconds

  def post(url: String, body: String, headers: Seq[(String, String)]): StandaloneWSResponse =
    Await.result(
      wsClient
        .url(url)
        .withHttpHeaders(headers*)
        .post(body),
      timeout
    )

}
