package models

import play.api.libs.json.Json

case class Engine(id: Long, engine: String, carModelId: Long)

object Engine {
  implicit val engineFormat = Json.format[Engine]
}

