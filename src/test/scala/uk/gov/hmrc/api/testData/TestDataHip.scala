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

  val originatorId: String  = UUID.randomUUID().toString
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

  val validInsertLCWLCWRAWithoutEndDateHipRequest: JsValue =
    hipUcLiabilityPayload("LCW/LCWRA", liabilityStartDate, dateOfBirth = Some(dob))

  val validInsertLCWLCWRAWithEndDateHipRequest: JsValue =
    hipUcLiabilityPayload("LCW/LCWRA", liabilityStartDate, dateOfBirth = Some(dob), endDate = Some(liabilityEndDate))

  val validInsertUCWithOutEndDateHipRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, dateOfBirth = Some(dob))

  val validInsertUCWithEndDateHipRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some(liabilityEndDate), dateOfBirth = Some(dob))

  val validHipLCWLCWRATerminationRequest: JsValue =
    hipUcTerminationPayload("LCW/LCWRA", liabilityStartDate, liabilityEndDate)

  val validHipUCTerminationRequest: JsValue =
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

  // -----Insert Unprocessable Entity------

  val conflictingInsertLiabilityHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val startDateBefore16thBirthdayInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", startDate = "2023-04-05", dateOfBirth = Some("2009-10-10"))

  val startDateAfterStatePensionAgeInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", startDate = "2025-04-15", dateOfBirth = Some("1957-04-14"))

  val startDateAfterDeathInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val startAndEndDateAfterDeathInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val endDateAfterStatePensionAgeInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some("1957-04-14"))

  val endDateAfterDeathInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val notWithinUCPeriodInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val lcwLcwrOverrideInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val notMatchingLiabilityInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val startDateBefore29042013InsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", startDate = "2013-04-28", endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val endDateBeforeStartDateInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val pseudoAccountInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val nonLiveAccountInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val accountTransferredIsleOfManInsertHIPRequest: JsValue =
    hipUcLiabilityPayload("LCW/LCWRA", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  val startDateAfterDeath2InsertHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", liabilityStartDate, endDate = Some("2025-01-04"), dateOfBirth = Some(dob))

  // -----Terminate Unprocessable Entity------

  val conflictingTerminateLiabilityHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val startDateBefore16thBirthdayTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val startDateAfterStatePensionAgeTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", startDate = "2025-04-15", endDate = "2025-01-04")

  val startDateAfterDeathTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val startAndEndDateAfterDeathTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val endDateAfterStatePensionAgeTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val endDateAfterDeathTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val notWithinUCPeriodTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val lcwLcwrOverrideTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val notMatchingLiabilityTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val startDateBefore29042013TerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", startDate = "2013-04-28", endDate = "2025-01-04")

  val endDateBeforeStartDateTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val pseudoAccountTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val nonLiveAccountTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  val accountTransferredIsleOfManTerminateHIPRequest: JsValue =
    hipUcTerminationPayload("LCW/LCWRA", liabilityStartDate, endDate = "2025-01-04")

  val startDateAfterDeath2TerminateHIPRequest: JsValue =
    hipUcTerminationPayload("UC", liabilityStartDate, endDate = "2025-01-04")

  // ---- Invalid payloads ----
  val invalidCreditRecordTypeTTTRequest: JsValue =
    hipUcLiabilityPayload("TTT", "2025-08-19", dateOfBirth = Some(dob), endDate = Some("2025-08-20"))

  val invalidCreditRecordTypeTTTHIPRequest: JsValue =
    hipUcLiabilityPayload("TTT", "2025-08-19", dateOfBirth = Some(dob), endDate = Some("2025-08-20"))

  val emptyCreditRecordTypeRequest: JsValue =
    hipUcLiabilityPayload("", "2025-08-19", dateOfBirth = Some(dob), endDate = Some("2025-08-20"))

  val invalidCreditActionTypeRequest: JsValue =
    hipUcLiabilityPayload("Dummy", "2025-08-19", dateOfBirth = Some(dob), endDate = Some("2025-08-20"))

  val emptyCreditActionTypeRequest: JsValue =
    hipUcLiabilityPayload("", "2025-08-19", dateOfBirth = Some(dob), endDate = Some("2025-08-20"))

  val emptyCreditRecordTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("", "2025-08-19", dateOfBirth = Some(dob), endDate = Some("2025-08-20"))
  
  val invalidStartDateAfterEndDateActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2025-08-20"))

  val invalidStartDateAfterEndDateActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2025-08-20"))

  val invalidStartDateInFebActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-02-30", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateInFebActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-02-30", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateInvalidMonthActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-13-01", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateInvalidMonthActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-13-01", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateMonthZeroActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-00-01", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateMonthZeroActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-00-01", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateDayZeroActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-09-00", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateDayZeroActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-09-00", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateDayAprilActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-04-31", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateDayAprilActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-04-31", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateFormatIssueActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2028-9-11", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateWrongFormatActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "11-05-2025", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateWrongFormatActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "11-05-2025", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateZeroActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "0000-00-00", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateZeroActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "0000-00-00", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateEmptyActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateEmptyActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateMissingActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidStartDateMissingActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "", dateOfBirth = Some("2002-10-10"), endDate = Some("2030-08-20"))

  val invalidEndDateAfterEndDateActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2025-08-18"))

  val invalidEndDateAfterEndDateActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2025-08-18"))

  val invalidEndDateNotLeapYearActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2027-02-29"))

  val invalidEndDateNotLeapYearActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2027-02-29"))

  val invalidEndDateInvalidDayFebActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-02-30"))

  val invalidEndDateInvalidDayFebActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-02-30"))

  val invalidEndDateInvalidMonthActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-15-18"))

  val invalidEndDateInvalidMonthActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-15-18"))

  val invalidEndDateMonthZeroActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-00-18"))

  val invalidEndDateMonthZeroActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-00-18"))

  val invalidEndDateDayZeroActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-11-00"))

  val invalidEndDateDayZeroActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-11-00"))

  val invalidEndDateDayAprilActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-04-31"))

  val invalidEndDateDayAprilActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-04-31"))

  val invalidEndDateFormatIssueActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-4-20"))

  val invalidEndDateFormatIssueActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("2026-4-20"))

  val invalidEndDateWrongFormatActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("18-04-2027"))

  val invalidEndDateWrongFormatActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("18-04-2027"))

  val invalidEndDateZeroActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("0000-00-00"))

  val invalidEndDateZeroActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some("0000-00-00"))

  val invalidEndDateEmptyActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some(""))

  val invalidEndDateEmptyActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"), endDate = Some(""))

  val invalidEndDateMissingActionTypeRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"))

  val invalidEndDateMissingActionTypeHIPRequest: JsValue =
    hipUcLiabilityPayload("UC", "2025-08-19", dateOfBirth = Some("2002-10-10"))


  // ------Forbidden------

  val validUCLiabilityRequest: JsValue =
    hipUcLiabilityPayload("LCW/LCWRA", liabilityStartDate, dateOfBirth = Some(dob))

}
