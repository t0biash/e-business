package models

import play.api.libs.json.Json

case class CarModel(id: Long, name: String, year: Int, carMakeId: Long)

object CarModel {
  implicit val carModelFormat = Json.format[CarModel]
}

