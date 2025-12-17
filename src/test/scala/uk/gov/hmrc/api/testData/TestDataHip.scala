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

import play.api.libs.json.{JsObject, JsValue, Json}

import java.util.UUID
import scala.util.Random

trait TestDataHip {

  val randomNino: String = "AE%06d".format(Random.nextInt(999999))

  val originatorId: String  = ""
  val correlationId: String = UUID.randomUUID().toString

  val validHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"         -> "application/json",
      "correlationId"        -> correlationId,
      "gov-uk-originator-id" -> originatorId
    )

  val validHeadersWithOriginator: Seq[(String, String)] =
    validHeaders :+ ("gov-uk-originator-id" -> originatorId)

  def removeHeader(headers: Seq[(String, String)], key: String): Seq[(String, String)] =
    headers.filterNot(_._1.equalsIgnoreCase(key))

  def overrideHeader(headers: Seq[(String, String)], key: String, value: String): Seq[(String, String)] =
    removeHeader(headers, key) :+ (key -> value)

  private val dob                = "2002-10-10"
  private val liabilityStartDate = "2015-08-19"
  private val liabilityEndDate   = "2025-01-04"

  private def hipUcLiabilityPayload(
    recordType: String,
    startDate: String,
    endDate: Option[String] = None,
    dateOfBirth: Option[String] = None
  ): JsObject = {
    val baseDetails = Json.obj(
      "universalCreditRecordType" -> recordType,
      "liabilityStartDate"        -> startDate,
      "liabilityEndDate"          -> endDate
    )

    val dobObj = dateOfBirth.fold(Json.obj())(d => Json.obj("dateOfBirth" -> d))

    Json.obj(
      "universalCreditLiabilityDetails" -> (baseDetails ++ dobObj)
    )
  }

  private def hipUcTerminationPayload(recordType: String, startDate: String, endDate: String): JsObject =
    Json.obj(
      "ucLiabilityTerminationDetails" -> Json.obj(
        "universalCreditRecordType" -> recordType,
        "liabilityStartDate"        -> startDate,
        "liabilityEndDate"          -> endDate
      )
    )

  // ---- Valid payloads ----
  val validHipUcLiabilityRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some(liabilityEndDate), dateOfBirth = Some(dob))

  val validInsertLCWLCWRAHipRequest: JsValue =
    hipUcLiabilityPayload("LCW/LCWRA", liabilityStartDate, dateOfBirth = Some(dob))

  val validInsertLCWLCWRAWithEndDateHipRequest: JsValue =
    hipUcLiabilityPayload("LCW/LCWRA", liabilityStartDate, dateOfBirth = Some(dob), endDate = Some(liabilityEndDate))

  val validHipUcTerminationRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, liabilityEndDate)

  // ---- Invalid UC Liability payloads ----
  val invalidHipRecordTypeLiabilityRequest: JsValue =
    hipUcLiabilityPayload("UC/ABC", liabilityStartDate, endDate = Some(liabilityEndDate), dateOfBirth = Some(dob))

  val invalidHipDateOfBirthLiabilityRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some(liabilityEndDate), dateOfBirth = Some("20022-10-10"))

  val invalidHipStartDateLiabilityRequest: JsValue =
    hipUcLiabilityPayload("UC", "2015-88-19", endDate = Some(liabilityEndDate), dateOfBirth = Some(dob))

  val invalidHipEndDateLiabilityRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-99-04"), dateOfBirth = Some(dob))

  // ---- Invalid Termination payloads ----
  val invalidHipRecordTypeTerminationRequest: JsValue =
    hipUcTerminationPayload("UC/ABC", liabilityStartDate, liabilityEndDate)

  val invalidHipStartDateTerminationRequest: JsValue =
    hipUcTerminationPayload("UC", "2015-88-19", liabilityEndDate)

  val invalidHipEndDateTerminationRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, "2025-99-04")
}
