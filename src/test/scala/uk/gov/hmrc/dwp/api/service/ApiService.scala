package uk.gov.hmrc.dwp.api.service

import org.apache.pekko.actor.ActorSystem
import play.api.libs.json.JsValue
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{StandaloneWSResponse, writeableOf_JsValue}

import scala.concurrent.Await
import scala.concurrent.duration.*

class ApiService {
  implicit val actorSystem: ActorSystem = ActorSystem()
  val wsClient: StandaloneAhcWSClient   = StandaloneAhcWSClient()

  def postNotification(url: String, headers: Seq[(String, String)], requestBody: JsValue): StandaloneWSResponse = {
    val response = wsClient
      .url(url)
      .withHttpHeaders(headers: _*)
      .withBody(requestBody)
      .execute("POST")

    Await.result(response, 10.seconds)
  }
}
