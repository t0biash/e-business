package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import javax.inject.{Inject, Singleton}

@Singleton
class CarModelsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def create(carMakeId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create car make $carMakeId model")
  }

  def getByCarMake(carMakeId: Long): Action[AnyContent] = Action { implicit request =>
    if (carMakeId == -1)
      Ok("All car models")
    else
      Ok(s"All car make $carMakeId models")
  }

  def getById(carModelId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Car model $carModelId")
  }

  def update(carModelId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update car model $carModelId")
  }

  def delete(carModelId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete car model $carModelId")
  }
}