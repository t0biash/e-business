package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}
import play.api.libs.json.Json

case class CreateCarMakeData(name: String)
case class UpdateCarMakeData(id: Long, name: String)
case class DeleteCarMakeData(id: Long, name: String)

object CarMakeForms {
  val CreateForm: Form[CreateCarMakeData] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCarMakeData.apply)(CreateCarMakeData.unapply)
  }

  val UpdateForm: Form[UpdateCarMakeData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(UpdateCarMakeData.apply)(UpdateCarMakeData.unapply)
  }

  val DeleteForm: Form[DeleteCarMakeData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(DeleteCarMakeData.apply)(DeleteCarMakeData.unapply)
  }
}

object CreateCarMakeData {
  implicit val jsonFormat = Json.format[CreateCarMakeData]
}

object UpdateCarMakeData {
  implicit val jsonFormat = Json.format[UpdateCarMakeData]
}