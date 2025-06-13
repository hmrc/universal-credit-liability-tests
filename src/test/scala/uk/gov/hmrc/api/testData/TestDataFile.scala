package uk.gov.hmrc.api.testData

import play.api.libs.json.{JsValue, Json}

import java.util.UUID
import scala.util.Random

trait TestDataFile {

  val randomNino: String = "AB%06d".format(Random.nextInt(999999))

  val baseUrl             = s"http://localhost:16107"
  val endpointUrl: String = s"$baseUrl/notification"

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
       |  "dateOfBirth": "2002-10-10",
       |  "liabilityStartDate": "2015-08-19",
       |  "liabilityEndDate": "2025-01-04"
       |}
       |""".stripMargin)

  val validTerminationUCLiabilityRequest: JsValue = Json.parse(s"""
        |{
        |     "nationalInsuranceNumber": "$randomNino",
        |     "universalCreditRecordType": "UC",
        |     "universalCreditAction": "Terminate",
        |     "dateOfBirth": "2002-10-10",
        |     "liabilityStartDate": "2015-08-19",
        |     "liabilityEndDate": "2025-01-04"
        |}
        |""".stripMargin)

  val invalidInsertLCWLCWRALiabilityRequest: JsValue = Json.parse(s"""
        |{
        |  "nationalInsuranceNumber": "$randomNino",
        |  "universalCreditRecordType": "LCW/LCWRA/NDJ",
        |  "universalCreditAction": "Insert",
        |  "dateOfBirth": "2002-10-10",
        |  "liabilityStartDate": "2015-08-19",
        |  "liabilityEndDate": "2025-01-04"
        |}
        |""".stripMargin)

  val invalidInsertUCLiabilityRequest: JsValue = Json.parse(s"""
        |{
        |  "nationalInsuranceNumber": "$randomNino",
        |  "universalCreditRecordType": "UC/NDJ",
        |  "universalCreditAction": "Insert",
        |  "dateOfBirth": "2002-10-10",
        |  "liabilityStartDate": "2015-08-19",
        |  "liabilityEndDate": "2025-01-04"
        |}
        |""".stripMargin)

  val inValidTerminationLCWLCWRALiabilityRequest: JsValue = Json.parse(s"""
        |{
        |  "nationalInsuranceNumber": "$randomNino",
        |  "universalCreditRecordType": "LCW/LCWRA/NDJ",
        |  "universalCreditAction": "Terminate",
        |  "dateOfBirth": "2002-10-10",
        |  "liabilityStartDate": "2015-08-19",
        |  "liabilityEndDate": "2025-01-04"
        |}
        |""".stripMargin)

  val inValidTerminationUCLiabilityRequest: JsValue = Json.parse(s"""
        |{
        |  "nationalInsuranceNumber": "$randomNino",
        |  "universalCreditRecordType": "UC/NDJ",
        |  "universalCreditAction": "Terminate",
        |  "dateOfBirth": "2002-10-10",
        |  "liabilityStartDate": "2015-08-19",
        |  "liabilityEndDate": "2025-01-04"
        |}
        |""".stripMargin)
}
