package controllers

import forms.CreateOrderProductData
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import repositories.OrderProductRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class OrderProductsController @Inject()(cc: MessagesControllerComponents, val orderProductRepository: OrderProductRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create(orderId: Long) = Action(parse.json) { request =>
    val result = request.body.validate[CreateOrderProductData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      product => {
        orderProductRepository.create(orderId, product.productId)
        Ok(Json.obj("message" -> (s"Added product ${product.productId} to order ${orderId}")))
      }
    )
  }

  def getByOrderId(orderId: Long): Action[AnyContent] = Action.async { implicit request =>
    orderProductRepository.getByOrderId(orderId).map(orderedProducts => {
      Ok(Json.toJson(orderedProducts))
    })
  }
}