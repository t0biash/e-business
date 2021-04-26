package models

import play.api.libs.json.Json

case class Product(id: Long, name: String, description: String, Price: BigDecimal, partsManufacturerId: Long, categoryId: Long)

object Product {
  implicit val productFormat = Json.format[Product]
}


