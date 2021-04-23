package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class PartsManufacturersController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(): Action[AnyContent] = Action { implicit request =>
    Ok("Create parts manufacturer")
  }

  def getAll(): Action[AnyContent] = Action { implicit request =>
    Ok("All parts manufacturers")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Parts manufacturer $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update parts manufacturer $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete parts manufacturer $id")
  }
}
