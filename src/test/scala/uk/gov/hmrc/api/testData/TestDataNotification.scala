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

import play.api.libs.json.{JsObject, Json}

trait TestDataNotification extends BaseTestData {

  def insertNotificationPayload(
    nino: String = randomNino,
    recordType: String = randomUniversalCreditRecordType,
    creditAction: String = "Insert",
    startDate: String = "2025-08-19"
  ): JsObject = Json.obj(
    "nationalInsuranceNumber"   -> nino,
    "universalCreditRecordType" -> recordType,
    "universalCreditAction"     -> creditAction,
    "liabilityStartDate"        -> startDate
  )

  def terminateNotificationPayload(
    nino: String = randomNino,
    recordType: String = randomUniversalCreditRecordType,
    creditAction: String = "Terminate",
    startDate: String = "2025-08-19",
    endDate: String = "2026-06-30"
  ): JsObject = Json.obj(
    "nationalInsuranceNumber"   -> nino,
    "universalCreditRecordType" -> recordType,
    "universalCreditAction"     -> creditAction,
    "liabilityStartDate"        -> startDate,
    "liabilityEndDate"          -> endDate
  )

  def insertNotificationPayloadMissing(parameterName: String): JsObject =
    insertNotificationPayload() - parameterName

  def terminateNotificationPayloadMissing(parameterName: String): JsObject =
    terminateNotificationPayload() - parameterName

  val getInvalidAuthToken: String = "Invalid token"
  val getNoAuthToken: String      = ""
  val getExpiredAuthToken: String =
    "GNAP dummy-2b5998dccb61446fa2fa9d0f7211e181,Bearer JDkThJhFPxDJlUABxXqtpxYmzIwWik1RYVp61xoEcLudlSq6higSU7OEQEqBlgTBQHTNWWRzZl4T8m8tfArtX2o7gA/qYAHhfHqWOxAp0flCPkSIhfyXuZyHTfLRXSQ8i6AejDV6nH4Td0KWtpjTSzP05ue6FHXdOIOSD7ZvHOwYgyplVeOQ7qHLUwzFwxEW/SsCJHiDyv5jJQREo7nuFQQ"
}
