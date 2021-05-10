package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}

case class CreatePartsManufacturerData(name: String, description: String)
case class UpdatePartsManufacturerData(id: Long, name: String, description: String)
case class DeletePartsManufacturerData(id: Long, name: String)

object PartsManufacturerForms {
  val CreateForm: Form[CreatePartsManufacturerData] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(CreatePartsManufacturerData.apply)(CreatePartsManufacturerData.unapply)
  }

  val UpdateForm: Form[UpdatePartsManufacturerData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(UpdatePartsManufacturerData.apply)(UpdatePartsManufacturerData.unapply)
  }

  val DeleteForm: Form[DeletePartsManufacturerData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(DeletePartsManufacturerData.apply)(DeletePartsManufacturerData.unapply)
  }
}