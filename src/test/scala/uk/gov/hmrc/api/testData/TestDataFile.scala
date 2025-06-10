package uk.gov.hmrc.api.testData

import play.api.libs.json.{JsValue, Json}

import scala.util.Random

trait TestDataFile {
  val baseUrl = s"http://localhost:16108"

  val randomNino: String = "AB%06d".format(Random.nextInt(999999))

  val insertURL: String =
    s"$baseUrl/universal-credit-liability-stubs/person/$randomNino/liability/universal-credit"

  val terminationURL: String =
    s"$baseUrl/universal-credit-liability-stubs/person/$randomNino/liability/universal-credit/termination"

  val validHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"         -> "application/json",
      "correlationId"        -> "3e8dae97-b586-4cef-8511-68ac12da9028",
      "gov-uk-originator-id" -> "gov-uk-originator-id"
    )

  val invalidHeaders: Seq[(String, String)] =
    Seq(
      "Content-Type"  -> "application/json",
      "correlationId" -> "3e8dae97-b586-4cef-8511-68ac12da9028"
    )

  val validInsertLCWLCWRALiabilityRequestNOEndDate: JsValue =
    Json.parse("""
        |{
        |  "universalCreditLiabilityDetails": {
        |    "universalCreditRecordType": "LCW/LCWRA",
        |    "dateOfBirth": "2002-10-10",
        |    "liabilityStartDate": "2015-08-19"
        |  }
        |}
        |""".stripMargin)

  val validInsertLCWLCWRALiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "universalCreditLiabilityDetails": {
        |    "universalCreditRecordType": "LCW/LCWRA",
        |    "dateOfBirth": "2002-10-10",
        |    "liabilityStartDate": "2015-08-19",
        |    "liabilityEndDate": "2025-01-04"
        |  }
        |}
        |""".stripMargin)

  val validInsertUCLiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "universalCreditLiabilityDetails": {
        |    "universalCreditRecordType": "UC",
        |    "dateOfBirth": "2002-10-10",
        |    "liabilityStartDate": "2015-08-19",
        |    "liabilityEndDate": "2025-01-04"
        |  }
        |}
        |""".stripMargin)

  val validTerminationLCWLCWRALiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "ucLiabilityTerminationDetails": {
        |    "universalCreditRecordType": "LCW/LCWRA",
        |    "liabilityStartDate": "2015-08-19",
        |    "liabilityEndDate": "2025-01-04"
        |  }
        |}
        |""".stripMargin)

  val validTerminationUCLiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "ucLiabilityTerminationDetails": {
        |    "universalCreditRecordType": "UC",
        |    "liabilityStartDate": "2015-08-19",
        |    "liabilityEndDate": "2025-01-04"
        |  }
        |}
        |""".stripMargin)

  val invalidInsertLCWLCWRALiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "universalCreditLiabilityDetails": {
        |    "universalCreditRecordType": "LCW/LCWRA",
        |    "dateOfBirth": "2002-10-10",
        |    "liabilityEndDate": "2025-01-04"
        |  }
        |}
        |""".stripMargin)

  val invalidInsertUCLiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "universalCreditLiabilityDetails": {
        |    "universalCreditRecordType": "UC",
        |    "dateOfBirth": "2002-10-10",
        |    "liabilityEndDate": "2025-01-04"
        |  }
        |}
        |""".stripMargin)

  val inValidTerminationLCWLCWRALiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "ucLiabilityTerminationDetails": {
        |    "universalCreditRecordType": "LCW/LCWRA",
        |    "liabilityStartDate": "2015-08-19"
        |  }
        |}
        |""".stripMargin)

  val inValidTerminationUCLiabilityRequest: JsValue =
    Json.parse("""
        |{
        |  "ucLiabilityTerminationDetails": {
        |    "universalCreditRecordType": "UC",
        |    "liabilityStartDate": "2015-08-19"
        |  }
        |}
        |""".stripMargin)
}
