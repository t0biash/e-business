package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}

case class CreateEngineData(engine: String, carModelId: Long)
case class UpdateEngineData(id: Long, engine: String, carModelId: Long)
case class DeleteEngineData(id: Long, engine: String, carModelId: Long)

object EngineForms {
  val CreateForm: Form[CreateEngineData] = Form {
    mapping(
      "engine" -> nonEmptyText,
      "carModelId" -> longNumber
    )(CreateEngineData.apply)(CreateEngineData.unapply)
  }

  val UpdateForm: Form[UpdateEngineData] = Form {
    mapping(
      "id" -> longNumber,
      "engine" -> nonEmptyText,
      "carModelId" -> longNumber
    )(UpdateEngineData.apply)(UpdateEngineData.unapply)
  }

  val DeleteForm: Form[DeleteEngineData] = Form {
    mapping(
      "id" -> longNumber,
      "engine" -> nonEmptyText,
      "carModelId" -> longNumber
    )(DeleteEngineData.apply)(DeleteEngineData.unapply)
  }
}
