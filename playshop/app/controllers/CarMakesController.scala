package controllers

import forms.{CarMakeForms, CreateCarMakeData, DeleteCarMakeData, UpdateCarMakeData}
import models.CarMake
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import repositories.CarMakeRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarMakesController @Inject()(cc: MessagesControllerComponents, val carMakeRepository: CarMakeRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create() = Action(parse.json) { request =>
    val result = request.body.validate[CreateCarMakeData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      carMake => {
        carMakeRepository.create(carMake.name)
        Ok(Json.obj("message" -> (s"Car make ${carMake.name} created")))
      }
    )
  }

  def getAll(): Action[AnyContent] = Action.async { implicit request =>
    carMakeRepository.getAll().map(carMakes => Ok(Json.toJson(carMakes)))
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    carMakeRepository.getByIdOption(id).map(carMake => carMake match {
      case Some(cm) => Ok(Json.toJson(cm))
      case None => Redirect(routes.CarMakesController.getAll())
    })
  }

  def update(id: Long) = Action(parse.json) { request =>
    val result = request.body.validate[UpdateCarMakeData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      carMake => {
        carMakeRepository.update(id, CarMake(id, carMake.name))
        Ok(Json.obj("message" -> (s"Car make updated")))
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    carMakeRepository.delete(id).map(_ => Ok(s"Car make $id deleted"))
  }

  def createForm(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.carmakes.carMakeAdd(CarMakeForms.CreateForm))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CarMakeForms.CreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.carmakes.carMakeAdd(errorForm)))
      },
      carMakeData => {
        carMakeRepository.create(carMakeData.name).map { _ =>
          Redirect(routes.CarMakesController.createForm()).flashing("success" -> "Car make created")
        }
      }
    )
  }

  def getAllForm(): Action[AnyContent] = Action.async { implicit request =>
    val carMakes = carMakeRepository.getAll()
    carMakes.map(carMakes => Ok(views.html.carmakes.carMakes(carMakes)))
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val carMake = carMakeRepository.getByIdOption(id)
    carMake.map(carMake => carMake match {
      case Some(cm) => Ok(views.html.carmakes.carMakeDetails(cm))
      case None => Redirect(routes.CarMakesController.getAllForm())
    })
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val carMake = carMakeRepository.getById(id)
    carMake.map(carMake => {
      val updateForm = CarMakeForms.UpdateForm.fill(UpdateCarMakeData(carMake.id, carMake.name))
      Ok(views.html.carmakes.carMakeUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CarMakeForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.carmakes.carMakeUpdate(errorForm)))
      },
      carMakeData => {
        carMakeRepository.update(carMakeData.id, CarMake(carMakeData.id, carMakeData.name)).map { _ =>
          Redirect(routes.CarMakesController.updateForm(carMakeData.id)).flashing("success" -> "Car make updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val carMake = carMakeRepository.getById(id)
    carMake.map(carMake => {
      val deleteForm = CarMakeForms.DeleteForm.fill(DeleteCarMakeData(carMake.id, carMake.name))
      Ok(views.html.carmakes.carMakeDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CarMakeForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.carmakes.carMakeDelete(errorForm)))
      },
      carMakeData => {
        carMakeRepository.delete(carMakeData.id).map { _ =>
          Redirect(routes.CarMakesController.getAllForm()).flashing("success" -> "Car make deleted")
        }
      }
    )
  }
}