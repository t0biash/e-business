package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class PaymentsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(userId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create user $userId payment")
  }

  def getByUserId(userId: Long): Action[AnyContent] = Action { implicit request =>
    if (userId == -1)
      Ok("All payments")
    else
      Ok(s"All user $userId payments")
  }

  def getById(paymentId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Payment $paymentId")
  }

  def update(paymentId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update payment $paymentId")
  }

  def delete(paymentId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete payment $paymentId")
  }
}
