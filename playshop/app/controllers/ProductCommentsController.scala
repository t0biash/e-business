package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class ProductCommentsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(productId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create product $productId comment")
  }

  def getByProductId(productId: Long): Action[AnyContent] = Action { implicit request =>
    if (productId == -1)
      Ok("All products comments")
    else
      Ok(s"All product $productId comments")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Comment $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update comment $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete comment $id")
  }
}