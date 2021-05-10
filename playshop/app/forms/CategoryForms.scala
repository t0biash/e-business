package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}

case class CreateCategoryData(name: String)
case class UpdateCategoryData(id: Long, name: String)
case class DeleteCategoryData(id: Long, name: String)

object CategoryForms {
  val CreateForm: Form[CreateCategoryData] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryData.apply)(CreateCategoryData.unapply)
  }

  val UpdateForm: Form[UpdateCategoryData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(UpdateCategoryData.apply)(UpdateCategoryData.unapply)
  }

  val DeleteForm: Form[DeleteCategoryData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(DeleteCategoryData.apply)(DeleteCategoryData.unapply)
  }
}