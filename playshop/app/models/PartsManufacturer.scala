package models

import play.api.libs.json.Json

case class PartsManufacturer(id: Long, name: String, description: String)

object PartsManufacturer {
  implicit val partsManufacturerFormat = Json.format[PartsManufacturer]
}

