package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, sqlDate}
import play.api.libs.json.Json

import java.sql.Date

case class CreateOrderData(date: Date, userId: Long)
case class UpdateOrderData(id: Long, date: Date, userId: Long, paymentId: Long)
case class DeleteOrderData(id: Long, date: Date)

object OrderForms {
  val CreateForm: Form[CreateOrderData] = Form {
    mapping(
      "date" -> sqlDate,
      "userId" -> longNumber
    )(CreateOrderData.apply)(CreateOrderData.unapply)
  }

  val UpdateForm: Form[UpdateOrderData] = Form {
    mapping(
      "id" -> longNumber,
      "date" -> sqlDate,
      "userId" -> longNumber,
      "paymentId" -> longNumber
    )(UpdateOrderData.apply)(UpdateOrderData.unapply)
  }

  val DeleteForm: Form[DeleteOrderData] = Form {
    mapping(
      "id" -> longNumber,
      "date" -> sqlDate,
    )(DeleteOrderData.apply)(DeleteOrderData.unapply)
  }
}

object CreateOrderData {
  implicit val jsonFormat = Json.format[CreateOrderData]
}

object UpdateOrderData {
  implicit val jsonFormat = Json.format[UpdateOrderData]
}