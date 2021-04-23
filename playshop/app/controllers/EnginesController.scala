package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class EnginesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(carModelId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create car model $carModelId engine")
  }

  def getByCarModel(carModelId: Long): Action[AnyContent] = Action { implicit request =>
    if (carModelId == -1)
      Ok("All engines")
    else
      Ok(s"All car model $carModelId engines")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Engine $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update engine $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete engine $id")
  }
}
