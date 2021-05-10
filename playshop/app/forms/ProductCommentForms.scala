package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}

case class CreateProductCommentData(rate: Int, content: String, userId: Long, productId: Long)
case class UpdateProductCommentData(id: Long, rate: Int, content: String, userId: Long, productId: Long)
case class DeleteProductCommentData(id: Long, rate: Int, content: String)

object ProductCommentForms {
  val CreateForm: Form[CreateProductCommentData] = Form {
    mapping(
      "rate" -> number(min = 0, max = 5),
      "content" -> text,
      "userId" -> longNumber,
      "productId" -> longNumber
    )(CreateProductCommentData.apply)(CreateProductCommentData.unapply)
  }

  val UpdateForm: Form[UpdateProductCommentData] = Form {
    mapping(
      "id" -> longNumber,
      "rate" -> number(min = 0, max = 5),
      "content" -> text,
      "userId" -> longNumber,
      "productId" -> longNumber
    )(UpdateProductCommentData.apply)(UpdateProductCommentData.unapply)
  }

  val DeleteForm: Form[DeleteProductCommentData] = Form {
    mapping(
      "id" -> longNumber,
      "rate" -> number(min = 0, max = 5),
      "content" -> text
    )(DeleteProductCommentData.apply)(DeleteProductCommentData.unapply)
  }
}