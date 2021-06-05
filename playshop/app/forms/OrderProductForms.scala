package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping}
import play.api.libs.json.Json

case class CreateOrderProductData(orderId: Long, productId: Long)

object OrderProductForms {
  val CreateForm: Form[CreateOrderProductData] = Form {
    mapping(
      "orderId" -> longNumber,
      "productId" -> longNumber
    )(CreateOrderProductData.apply)(CreateOrderProductData.unapply)
  }
}

object CreateOrderProductData {
  implicit val jsonFormat = Json.format[CreateOrderProductData]
}