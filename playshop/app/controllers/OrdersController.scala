package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class OrdersController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(userId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create user $userId order")
  }

  def getByUserId(userId: Long): Action[AnyContent] = Action { implicit request =>
    if (userId == -1)
      Ok("All orders")
    else
      Ok(s"All user $userId orders")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Order $id")
  }

  def update(orderId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update order $orderId")
  }

  def delete(orderId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete order $orderId")
  }
}
