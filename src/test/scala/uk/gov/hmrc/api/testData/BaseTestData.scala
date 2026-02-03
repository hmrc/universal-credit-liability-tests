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
import scala.annotation.tailrec
import scala.util.Random
import scala.util.matching.Regex

trait BaseTestData {

  private val NinoPattern: Regex =
    "^([ACEHJLMOPRSWXY][A-CEGHJ-NPR-TW-Z]|B[A-CEHJ-NPR-TW-Z]|G[ACEGHJ-NPR-TW-Z]|[KT][A-CEGHJ-MPR-TW-Z]|N[A-CEGHJL-NPR-SW-Z]|Z[A-CEGHJ-NPR-TW-Y])[0-9]{6}$".r

  /** Generates a random NINO that does not collide with any of the fixed prefixes used for 422 Unprocessable Entities
    */
  def randomNino: String = {
    val restrictedPrefixes: Set[String] = unprocessableCases.keys.map(_.take(2)).toSet
    val digits: String                  = "%06d".format(Random.nextInt(999999))

    @tailrec
    def generatePrefix: String = {
      val firstChar: Char         = Random.shuffle("ABGKTNZACEHJLMOPRSWXY".toList).head
      val secondChar: Char        = Random.shuffle("ABCEGHJKLMNPRSTWXYZ".toList).head
      val prefixCandidate: String = s"$firstChar$secondChar"

      val isAllowed = !restrictedPrefixes.contains(prefixCandidate)
      val isValid   = NinoPattern.matches(s"${prefixCandidate}000000")

      if (isAllowed && isValid) prefixCandidate
      else generatePrefix
    }

    s"$generatePrefix$digits"
  }

  def randomUniversalCreditAction: String = {
    val actions: List[String] = List("Insert", "Terminate")
    Random.shuffle(actions).head
  }

  def randomUniversalCreditRecordType: String = {
    val recordTypes: List[Nino] = List("UC", "LCW/LCWRA")
    Random.shuffle(recordTypes).head
  }

  def ninoWithPrefix(prefix: String): String = {
    val suffix = "%03d".format(Random.nextInt(1000))
    s"$prefix$suffix"
  }

  // Headers
  val jsonContentType: String = "application/json"

  def randomCorrelationId: String = UUID.randomUUID().toString

  def randomGovUkOriginatorId: String = {
    val length = 3 + Random.nextInt(38) // Generates a length between 3 and 40
    Random.alphanumeric.take(length).mkString
  }

  def baseHeaders: Seq[(String, String)] = Seq(
    "Content-Type"         -> jsonContentType,
    "Authorization"        -> "Bearer FIXME", // TODO: implement bearer token
    "correlationId"        -> randomCorrelationId,
    "gov-uk-originator-id" -> randomGovUkOriginatorId
  )

  def invalidHeaders: Seq[(String, String)] = Seq(
    "Content-Type"         -> "INVALID",
    "Authorization"        -> "Bearer INVALID",
    "correlationId"        -> "INVALID",
    "gov-uk-originator-id" -> "!NV@L!D"
  )

  // TODO: need to add `Authorization -> basicAuth,`
  val validHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"         -> jsonContentType,
      "correlationId"        -> randomCorrelationId,
      "gov-uk-originator-id" -> randomGovUkOriginatorId
    )

  def removeHeader(headers: Seq[(String, String)], key: String): Seq[(String, String)] =
    headers.filterNot(_._1.equalsIgnoreCase(key))

  def overrideHeader(headers: Seq[(String, String)], key: String, value: String): Seq[(String, String)] =
    removeHeader(headers, key) :+ (key -> value)

  // Headers with missing
  def headersMissingContentType: Seq[(String, String)] =
    removeHeader(baseHeaders, "Content-Type")

  def headersMissingAuthorization: Seq[(String, String)] =
    removeHeader(baseHeaders, "Authorization")

  def headersMissingCorrelationId: Seq[(String, String)] =
    removeHeader(baseHeaders, "correlationId")

  def headersWithoutGovUkOriginatorId: Seq[(String, String)] =
    removeHeader(baseHeaders, "gov-uk-originator-id")

  // Headers with invalid values
  def headersInvalidContentType: Seq[(String, String)] =
    overrideHeader(baseHeaders, "Content-Type", "INVALID")

  def headersInvalidAuth: Seq[(String, String)] =
    overrideHeader(baseHeaders, "Authorization", "INVALID")

  def headersInvalidCorrelationId: Seq[(String, String)] =
    overrideHeader(baseHeaders, "correlationId", "INVALID")

  def headersInvalidShortOriginatorId: Seq[(String, String)] =
    overrideHeader(baseHeaders, "gov-uk-originator-id", "A" * 2)

  def headersInvalidLongOriginatorId: Seq[(String, String)] =
    overrideHeader(baseHeaders, "gov-uk-originator-id", "A" * 41)

  // Headers with empty values
  def headersEmptyContentType: Seq[(String, String)] =
    overrideHeader(baseHeaders, "Content-Type", "")

  def headersEmptyAuth: Seq[(String, String)] =
    overrideHeader(baseHeaders, "Authorization", "")

  def headersEmptyCorrelationId: Seq[(String, String)] =
    overrideHeader(baseHeaders, "correlationId", "")

  def headersEmptyOriginatorId: Seq[(String, String)] =
    overrideHeader(baseHeaders, "gov-uk-originator-id", "")

  // Types
  type Nino       = String
  type NinoPrefix = String
  type ErrorCode  = String
  type Reason     = String

  // object ErrorCodes {
  val InvalidInput: ErrorCode  = "400.1"
  val ForbiddenCode: ErrorCode = "403.2"
  // }

  val unprocessableCases: Map[Nino, (ErrorCode, Reason)] = Map(
    "BW130" -> ("Start Date and End Date must be earlier than Date of Death", "55006"),
    "EZ200" -> ("End Date must be earlier than State Pension Age", "55008"),
    "BK190" -> ("End Date later than Date of Death", "55027"),
    "ET060" -> ("Start Date later than SPA", "55029"),
    "GE100" -> ("A conflicting or identical Liability is already recorded", "55038"),
    "GP050" -> ("NO corresponding liability found", "55039"),
    "EK310" -> ("Start Date is not before date of death", "64996"),
    "HS260" -> ("LCW/LCWRA not within a period of UC", "64997"),
    "CE150" -> ("LCW/LCWRA Override not within a period of LCW/LCWRA", "64998"),
    "HC210" -> ("Start date must not be before 16th birthday", "65026"),
    "GX240" -> ("Start date before 29/04/2013", "65536"),
    "HT230" -> ("End date before start date", "65537"),
    "EA040" -> ("End date missing but the input was a Termination", "65538"),
    "BX100" -> ("The NINO input matches a Pseudo Account", "65541"),
    "HZ310" -> (
      "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)",
      "65542"
    ),
    "BZ230" -> ("The NINO input matches an account that has been transferred to the Isle of Man", "65543"),
    "AB150" -> ("Start Date after Death", "99999")
  )

  val map422ErrorResponses: Map[ErrorCode, Reason] =
    Map(
      "55006" -> "Start Date and End Date must be earlier than Date of Death",
      "55008" -> "End Date must be earlier than State Pension Age",
      "55027" -> "End Date later than Date of Death",
      "55029" -> "Start Date later than SPA",
      "55038" -> "A conflicting or identical Liability is already recorded",
      "55039" -> "NO corresponding liability found",
      "64996" -> "Start Date is not before date of death",
      "64997" -> "LCW/LCWRA not within a period of UC",
      "64998" -> "LCW/LCWRA Override not within a period of LCW/LCWRA",
      "65026" -> "Start date must not be before 16th birthday",
      "65536" -> "Start date before 29/04/2013",
      "65537" -> "End date before start date",
      "65538" -> "End date missing but the input was a Termination",
      "65541" -> "The NINO input matches a Pseudo Account",
      "65542" -> "The NINO input matches a non-live account (including redundant, amalgamated and administrative account types)",
      "65543" -> "The NINO input matches an account that has been transferred to the Isle of Man",
      "99999" -> "Start Date after Death"
    )

  def constraintViolation(field: String): Reason = s"Constraint Violation - Invalid/Missing input parameter: $field"

  // -------------------

  val dateOfBirth: String        = "2002-04-27"
  val liabilityStartDate: String = "2025-08-19"
  val liabilityEndDate: String   = "2026-06-30"

  val insertStartDate: String    = "2025-08-19"
  val terminateStartDate: String = "2015-08-19"
  val terminateEndDate: String   = "2025-01-04"

}
