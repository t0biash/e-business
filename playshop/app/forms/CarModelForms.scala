package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText, number}
import play.api.libs.json.Json

case class CreateCarModelData(name: String, year: Int, carMakeId: Long)
case class UpdateCarModelData(id: Long, name: String, year: Int, carMakeId: Long)
case class DeleteCarModelData(id: Long, name: String, year: Int, carMakeId: Long)

object CarModelForms {
  val CreateForm: Form[CreateCarModelData] = Form {
    mapping(
      "name" -> nonEmptyText,
      "year" -> number(min = 1900),
      "carMakeId" -> longNumber
    )(CreateCarModelData.apply)(CreateCarModelData.unapply)
  }

  val UpdateForm: Form[UpdateCarModelData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "year" -> number(min = 1900),
      "carMakeId" -> longNumber
    )(UpdateCarModelData.apply)(UpdateCarModelData.unapply)
  }

  val DeleteForm: Form[DeleteCarModelData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "year" -> number(min = 1900),
      "carMakeId" -> longNumber
    )(DeleteCarModelData.apply)(DeleteCarModelData.unapply)
  }
}

object CreateCarModelData {
  implicit val jsonFormat = Json.format[CreateCarModelData]
}

object UpdateCarModelData {
  implicit val jsonFormat = Json.format[UpdateCarModelData]
}
