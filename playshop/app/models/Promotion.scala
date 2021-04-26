package models

import play.api.libs.json.Json

case class Promotion(id: Long, percentage: Float, fromDate: String, toDate: String, productId: Long)

object Promotion {
  implicit val promotionFormat = Json.format[Promotion]
}

