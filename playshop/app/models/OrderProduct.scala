package models

import play.api.libs.json.Json

case class OrderProduct(id: Long, orderId: Long, productId: Long)

object OrderProduct {
  implicit val orderProductFormat = Json.format[OrderProduct]
}