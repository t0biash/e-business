package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class UsersController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(): Action[AnyContent] = Action { implicit request =>
    Ok("Create user")
  }

  def getAll(): Action[AnyContent] = Action { implicit request =>
    Ok("All users")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"User $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update user $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete user $id")
  }
}
