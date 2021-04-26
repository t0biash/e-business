package models

import play.api.libs.json.Json

case class ProductComment(id: Long, rate: Int, content: String, userId: Long, productId: Long)

object ProductComment {
  implicit val productCommentFormat = Json.format[ProductComment]
}

