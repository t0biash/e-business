package forms

import play.api.data.Form
import play.api.data.Forms.{bigDecimal, longNumber, mapping, nonEmptyText}
import play.api.libs.json.Json

case class CreateProductData(name: String, description: String, price: BigDecimal, partsManufacturerId: Long, categoryId: Long, carModelId: Long)
case class UpdateProductData(id: Long, name: String, description: String, price: BigDecimal, partsManufacturerId: Long, categoryId: Long, carModelId: Long)
case class DeleteProductData(id: Long, name: String)

object ProductForms {
  val CreateForm: Form[CreateProductData] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> bigDecimal(10, 2),
      "partsManufacturerId" -> longNumber,
      "categoryId" -> longNumber,
      "carModelId" -> longNumber
    )(CreateProductData.apply)(CreateProductData.unapply)
  }

  val UpdateForm: Form[UpdateProductData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> bigDecimal(10, 2),
      "partsManufacturerId" -> longNumber,
      "categoryId" -> longNumber,
      "carModelId" -> longNumber
    )(UpdateProductData.apply)(UpdateProductData.unapply)
  }

  val DeleteForm: Form[DeleteProductData] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(DeleteProductData.apply)(DeleteProductData.unapply)
  }
}

object CreateProductData {
  implicit val jsonFormat = Json.format[CreateProductData]
}

object UpdateProductData {
  implicit val jsonFormat = Json.format[UpdateProductData]
}
