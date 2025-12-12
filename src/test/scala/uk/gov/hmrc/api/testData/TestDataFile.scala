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

package uk.gov.hmrc.api.testData

import play.api.libs.json.{JsValue, Json}

import java.util.UUID
import scala.util.Random

trait TestDataFile {

  val randomNino: String = "AE%06d".format(Random.nextInt(999999))

  val originatorId: String  = ""
  val correlationId: String = UUID.randomUUID().toString

  val validHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"         -> "application/json",
      "correlationId"        -> correlationId,
      "gov-uk-originator-id" -> originatorId
    )

  val invalidHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"  -> "application/json",
      "correlationId" -> correlationId
    )

  val validInsertLCWLCWRALiabilityRequest: JsValue = Json.parse(s"""
      |{
      |  "nationalInsuranceNumber": "$randomNino",
      |  "universalCreditRecordType": "LCW/LCWRA",
      |  "universalCreditAction": "Insert",
      |  "dateOfBirth": "2002-10-10",
      |  "liabilityStartDate": "2025-08-19"
      |}
      |""".stripMargin)

  val validInsertUCLiabilityRequest: JsValue = Json.parse(s"""
      |{
      |  "nationalInsuranceNumber": "$randomNino",
      |  "universalCreditRecordType": "UC",
      |  "universalCreditAction": "Insert",
      |  "dateOfBirth": "2002-10-10",
      |  "liabilityStartDate": "2025-08-19"
      |}
      |""".stripMargin)

  val validTerminationLCWLCWRALiabilityRequest: JsValue = Json.parse(s"""
      |{
      |  "nationalInsuranceNumber": "$randomNino",
      |  "universalCreditRecordType": "LCW/LCWRA",
      |  "universalCreditAction": "Terminate",
      |  "liabilityStartDate": "2015-08-19",
      |  "liabilityEndDate": "2025-01-04"
      |}
      |""".stripMargin)

  val validTerminationUCLiabilityRequest: JsValue = Json.parse(s"""
      |{
      |     "nationalInsuranceNumber": "$randomNino",
      |     "universalCreditRecordType": "UC",
      |     "universalCreditAction": "Terminate",
      |     "liabilityStartDate": "2015-08-19",
      |     "liabilityEndDate": "2025-01-04"
      |}
      |""".stripMargin)
}
