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

trait TestDataHip extends BaseTestData {

  private def optionalField(key: String, value: Option[String]): JsObject =
    value.fold(Json.obj())(v => Json.obj(key -> v))

  def insertHipPayload(
    recordType: String = randomUniversalCreditRecordType,
    startDate: String = "2025-08-19",
    dateOfBirth: String = "2002-04-27",
    endDate: Option[String] = Some("2026-06-30")
  ): JsObject = {
    val details = Json.obj(
      "universalCreditRecordType" -> recordType,
      "liabilityStartDate"        -> startDate,
      "dateOfBirth"               -> dateOfBirth
    ) ++ optionalField("liabilityEndDate", endDate)

    Json.obj("universalCreditLiabilityDetails" -> details)
  }

  /*
  def insertHipPayloadOptionalDoB(
    recordType: String = randomUniversalCreditRecordType,
    startDate: String = "2025-08-19",
    dateOfBirth: Option[String] = Some("2002-04-27"),
    endDate: Option[String] = Some("2026-06-30")
  ): JsObject = {
    val details = Json.obj(
      "universalCreditRecordType" -> recordType,
      "liabilityStartDate"        -> startDate
    ) ++ optionalField("dateOfBirth", dateOfBirth) ++
      optionalField("liabilityEndDate", endDate)

    Json.obj("universalCreditLiabilityDetails" -> details)
  }
   */

  def terminateHipPayload(
    recordType: String = randomUniversalCreditRecordType,
    startDate: String = "2025-08-19",
    endDate: String = "2026-06-30"
  ): JsObject = Json.obj(
    "ucLiabilityTerminationDetails" -> Json.obj(
      "universalCreditRecordType" -> recordType,
      "liabilityStartDate"        -> startDate,
      "liabilityEndDate"          -> endDate
    )
  )

  def insertHipPayloadMissing(parameterName: String): JsObject = {
    val details = (insertHipPayload() \ "universalCreditLiabilityDetails").as[JsObject]
    Json.obj("universalCreditLiabilityDetails" -> (details - parameterName))
  }

  def terminateHipPayloadMissing(parameterName: String): JsObject = {
    val details = (terminateHipPayload() \ "universalCreditLiabilityDetails").as[JsObject]
    Json.obj("universalCreditLiabilityDetails" -> (details - parameterName))
  }

}
