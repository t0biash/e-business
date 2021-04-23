package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class ProductsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(): Action[AnyContent] = Action { implicit request =>
    Ok("Create product")
  }

  def getByCategoryId(categoryId: Long): Action[AnyContent] = Action { implicit request =>
    if (categoryId == -1)
      Ok("All products")
    else
      Ok(s"Category $categoryId products")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Product $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update product $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete product $id")
  }
}
