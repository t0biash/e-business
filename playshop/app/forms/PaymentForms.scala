package forms

import play.api.data.Form
import play.api.data.Forms._

case class CreatePaymentData(provider: String, amount: BigDecimal)
case class UpdatePaymentData(id: Long, provider: String, amount: BigDecimal, completed: Boolean)
case class DeletePaymentData(id: Long, provider: String, amount: BigDecimal, completed: Boolean)

object PaymentForms {
  val CreateForm: Form[CreatePaymentData] = Form {
    mapping(
      "provider" -> nonEmptyText,
      "amount" -> bigDecimal(10, 2)
    )(CreatePaymentData.apply)(CreatePaymentData.unapply)
  }

  val UpdateForm: Form[UpdatePaymentData] = Form {
    mapping(
      "id" -> longNumber,
      "provider" -> nonEmptyText,
      "amount" -> bigDecimal(10, 2),
      "completed" -> boolean
    )(UpdatePaymentData.apply)(UpdatePaymentData.unapply)
  }

  val DeleteForm: Form[DeletePaymentData] = Form {
    mapping(
      "id" -> longNumber,
      "provider" -> nonEmptyText,
      "amount" -> bigDecimal(10, 2),
      "completed" -> boolean
    )(DeletePaymentData.apply)(DeletePaymentData.unapply)
  }
}