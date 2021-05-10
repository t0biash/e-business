package forms

import play.api.data.Form
import play.api.data.Forms.{bigDecimal, sqlDate, longNumber, mapping}

import java.sql.Date

case class CreatePromotionData(percentage: BigDecimal, fromDate: Date, toDate: Date, productId: Long)
case class UpdatePromotionData(id: Long, percentage: BigDecimal, fromDate: Date, toDate: Date, productId: Long)
case class DeletePromotionData(id: Long, percentage: BigDecimal, fromDate: Date, toDate: Date)

object PromotionForms {
  val CreateForm: Form[CreatePromotionData] = Form {
    mapping(
      "percentage" -> bigDecimal(4, 2),
      "fromDate" -> sqlDate,
      "toDate" -> sqlDate,
      "productId" -> longNumber
    )(CreatePromotionData.apply)(CreatePromotionData.unapply)
  }

  val UpdateForm: Form[UpdatePromotionData] = Form {
    mapping(
      "id" -> longNumber,
      "percentage" -> bigDecimal(4, 2),
      "fromDate" -> sqlDate,
      "toDate" -> sqlDate,
      "productId" -> longNumber
    )(UpdatePromotionData.apply)(UpdatePromotionData.unapply)
  }

  val DeleteForm: Form[DeletePromotionData] = Form {
    mapping(
      "id" -> longNumber,
      "percentage" -> bigDecimal(4, 2),
      "fromDate" -> sqlDate,
      "toDate" -> sqlDate
    )(DeletePromotionData.apply)(DeletePromotionData.unapply)
  }
}