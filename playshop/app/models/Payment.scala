package models

import play.api.libs.json.Json

case class Payment(id: Long, provider: String, amount: BigDecimal, completed: Boolean)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}

