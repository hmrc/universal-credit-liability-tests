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

package uk.gov.hmrc.api.testData

import java.util.UUID
import scala.util.Random

trait BaseTestData {

  val randomNino: String = "AE%06d".format(Random.nextInt(999999))

  // Headers
  val jsonContentType: String   = "application/json"
  val correlationId: String     = UUID.randomUUID().toString
  val govUkOriginatorId: String = "" // TODO: get the value of gov-uk-originator-id

  // TODO: govUkOriginatorId) [ 3 .. 40 ] characters - create edge cases?

  // TODO: need to add `Authorization -> basicAuth,`
  val validHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"         -> jsonContentType,
      "correlationId"        -> correlationId,
      "gov-uk-originator-id" -> govUkOriginatorId
    )

  // Types

  type Reason = String

  def constraintViolation(field: String): Reason = s"Constraint Violation - Invalid/Missing input parameter: $field"

}
