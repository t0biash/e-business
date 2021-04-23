package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class PromotionsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(productId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create product $productId promotion")
  }

  def getByProductId(productId: Long): Action[AnyContent] = Action { implicit request =>
    if (productId == -1)
      Ok("All promotions")
    else
      Ok(s"All product $productId promotions")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Promotion $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update promotion $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete promotion $id")
  }
}
