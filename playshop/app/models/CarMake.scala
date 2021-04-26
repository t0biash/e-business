package models

import play.api.libs.json.Json

case class CarMake(id: Long, name: String)

object CarMake {
  implicit val carMakeFormat = Json.format[CarMake]
}
