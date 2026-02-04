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

trait TestDataNotification {

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

  val invalidCorrelationIdHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"         -> "application/json",
      "correlationId"        -> "XYZ-ABCS-3e8dae97-b586-4cef-8511-68ac12da9028",
      "gov-uk-originator-id" -> originatorId
    )
  val missingCorrelationIdHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"         -> "application/json",
      "gov-uk-originator-id" -> originatorId
    )

  private val dob                = "2002-10-10"
  private val insertStartDate    = "2025-08-19"
  private val terminateStartDate = "2015-08-19"
  private val terminateEndDate   = "2025-01-04"

  private def uclPayload(
    recordType: String,
    action: String,
    startDate: String,
    dateOfBirth: Option[String] = None,
    endDate: Option[String] = None,
    nino: String = randomNino
  ): JsObject = {
    val base = Json.obj(
      "nationalInsuranceNumber"   -> nino,
      "universalCreditRecordType" -> recordType,
      "universalCreditAction"     -> action,
      "liabilityStartDate"        -> startDate
    )

    val dobObj = dateOfBirth.fold(Json.obj())(d => Json.obj("dateOfBirth" -> d))
    val endObj = endDate.fold(Json.obj())(d => Json.obj("liabilityEndDate" -> d))

    base ++ dobObj ++ endObj
  }

  // ---- Valid payloads ----
  val validInsertLCWLCWRALiabilityRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", insertStartDate, dateOfBirth = Some(dob))

  val validInsertUCLiabilityRequest: JsValue =
    uclPayload("UC", "Insert", insertStartDate, dateOfBirth = Some(dob))

  val validTerminationLCWLCWRALiabilityRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate))

  val validTerminationUCLiabilityRequest: JsValue =
    uclPayload("UC", "Terminate", terminateStartDate, endDate = Some(terminateEndDate))

  // ---- Invalid Insert payloads ----
  val invalidInsertLCWLCWRALiabilityRequest: JsValue =
    uclPayload("LCW/LCWRA/ABC", "Insert", terminateStartDate, dateOfBirth = Some(dob))

  val invalidInsertUCLiabilityRequest: JsValue =
    uclPayload("UC/ABC", "Insert", terminateStartDate, dateOfBirth = Some(dob))

  val invalidInsertCreditActionRequest: JsValue =
    uclPayload("UC", "insert", terminateStartDate, dateOfBirth = Some(dob))

  val invalidInsertDateOfBirthRequest: JsValue =
    uclPayload("UC", "Insert", insertStartDate, dateOfBirth = Some("202-10-10"))

  val invalidInsertStartDateRequest: JsValue =
    uclPayload("UC", "Insert", "20288-08-19", dateOfBirth = Some(dob))

  val invalidInsertNINORequest: JsValue =
    uclPayload("UC", "Insert", "2025-08-19", dateOfBirth = Some(dob), nino = "347654")

  val emptyInsertCreditRecordTypeRequest: JsValue =
    uclPayload("", "Insert", insertStartDate, dateOfBirth = Some(dob))

  val emptyInsertCreditActionRequest: JsValue =
    uclPayload("LCW/LCWRA", "", insertStartDate, dateOfBirth = Some(dob))

  val emptyInsertDateOfBirthRequest: JsValue =
    uclPayload("UC", "Insert", insertStartDate, dateOfBirth = Some(""))

  val emptyInsertStartDateRequest: JsValue =
    uclPayload("UC", "Insert", "", dateOfBirth = Some(dob))

  val emptyInsertNINORequest: JsValue =
    uclPayload("UC", "Insert", insertStartDate, dateOfBirth = Some(dob), nino = "")

  // ---- Invalid Terminate payloads ----

  val invalidTerminationLCWLCWRARequest: JsValue =
    uclPayload("LCW/LCWRA/ABC", "Terminate", terminateStartDate, endDate = Some(terminateEndDate))

  val invalidTerminationUCRequest: JsValue =
    uclPayload("UC/ABC", "Terminate", terminateStartDate, endDate = Some(terminateEndDate))

  val invalidTerminationActionRequest: JsValue =
    uclPayload("LCW/LCWRA", "ABC", terminateStartDate, endDate = Some(terminateEndDate))

  val invalidTerminateStartDate: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", "202-08-19", endDate = Some("2020-08-20"))

  val invalidTerminateEndDate: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", "2028-08-19", endDate = Some("2020-08-35"))

  val invalidNINOTerminationRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "5443456545 ")

  val emptyCreditRecordTypeTerminateRequest: JsValue =
    uclPayload(" ", "Terminate", terminateStartDate, endDate = Some(terminateEndDate))

  val emptyCreditActionTerminationRequest: JsValue =
    uclPayload("LCW/LCWRA", " ", terminateStartDate, endDate = Some(terminateEndDate))

  val emptyStartDateTerminationRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", " ", endDate = Some(terminateEndDate))

  val emptyNINOTerminationRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = " ")

  val emptyEndDateTerminationRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(" "))

  // ------Insert Unprocessable entity -------

  val conflictingInsertLiabilityRequest: JsValue =
    uclPayload("UC", "Insert", insertStartDate, dateOfBirth = Some(dob), nino = "GE100123")

  val startDateBefore16thBirthdayInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", startDate = "2023-04-05", dateOfBirth = Some("2009-10-10"), nino = "HC210123")

  val startDateAfterStatePensionAgeInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", startDate = "2023-04-05", dateOfBirth = Some("1957-04-14"), nino = "ET060123")

  val startDateAfterDeathInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", insertStartDate, dateOfBirth = Some("2009-10-10"), nino = "EK310123")

  val startAndEndDateAfterDeathInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "BW130123")

  val endDateAfterStatePensionAgeInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", insertStartDate, dateOfBirth = Some("2025-04-15"), nino = "EZ200500")

  val endDateAfterDeathInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", insertStartDate, dateOfBirth = Some(dob), nino = "BK190123")

  val notWithinUCPeriodInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "HS260000")

  val lcwLcwrOverrideInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "CE150123")

  val notMatchingLiabilityInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "GP050123")

  val startDateBefore29042013InsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", startDate = "2013-04-28", dateOfBirth = Some(dob), nino = "GX240000")

  val endDateBeforeStartDateInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", "2013-04-29", dateOfBirth = Some(dob), nino = "HT230123")

  val pseudoAccountInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "BX100123")

  val nonLiveAccountInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "HZ310123")

  val accountTransferredIsleOfManInsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "BZ230123")

  val startDateAfterDeath2InsertRequest: JsValue =
    uclPayload("LCW/LCWRA", "Insert", terminateStartDate, dateOfBirth = Some(dob), nino = "AB150123")

  // ------Terminate Unprocessable entity -------

  val conflictingTerminateLiabilityRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some("2025-01-04"), nino = "GE100123")

  val startDateBefore16thBirthdayTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", "2023-04-05", endDate = Some("2025-01-04"), nino = "HC210000")

  val startDateAfterStatePensionAgeTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", "2025-04-15", endDate = Some("2025-01-04"), nino = "ET060000")

  val startDateAfterDeathTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some("2025-01-04"), nino = "EK310123")

  val startAndEndDateAfterDeathTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some("2025-01-04"), nino = "BW130123")

  val endDateAfterStatePensionAgeTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", "2025-04-15", endDate = Some("2025-04-20"), nino = "EZ200500")

  val endDateAfterDeathTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "BK190123")

  val notWithinUCPeriodTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", "2015-08-19", endDate = Some(terminateEndDate), nino = "HS260123")

  val lcwlcwraOverrideTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "CE150000")

  val noMatchingLiabilityTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "GP050456")

  val startDateBefore29042013TerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "GX240000")

  val endDateBeforeStartDateTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", "2013-04-29", endDate = Some("2013-04-28"), nino = "HT230123")

  val pseudoAccountTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "BX100000")

  val nonLiveAccountTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "HZ310000")

  val accountTransferredIsleOfManTerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "BZ230123")

  val startDateAfterDeath2TerminateRequest: JsValue =
    uclPayload("LCW/LCWRA", "Terminate", terminateStartDate, endDate = Some(terminateEndDate), nino = "AB150123")

  val getInvalidAuthToken: String = "Invalid token"
  val getNoAuthToken: String      = ""
  val getExpiredAuthToken: String =
    "GNAP dummy-2b5998dccb61446fa2fa9d0f7211e181,Bearer JDkThJhFPxDJlUABxXqtpxYmzIwWik1RYVp61xoEcLudlSq6higSU7OEQEqBlgTBQHTNWWRzZl4T8m8tfArtX2o7gA/qYAHhfHqWOxAp0flCPkSIhfyXuZyHTfLRXSQ8i6AejDV6nH4Td0KWtpjTSzP05ue6FHXdOIOSD7ZvHOwYgyplVeOQ7qHLUwzFwxEW/SsCJHiDyv5jJQREo7nuFQQ"
}
