package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class CategoriesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(): Action[AnyContent] = Action { implicit request =>
    Ok("Create category")
  }

  def getAll(): Action[AnyContent] = Action { implicit request =>
    Ok("All categories")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Category $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update category $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete category $id")
  }
}
