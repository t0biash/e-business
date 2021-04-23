package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class CarMakesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(): Action[AnyContent] = Action { implicit request =>
    Ok("Create car make")
  }

  def getAll(): Action[AnyContent] = Action { implicit request =>
    Ok("All car makes")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Car make $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update car make $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete car make $id")
  }
}