package controllers

import forms.{CarModelForms, CreateCarModelData, DeleteCarModelData, UpdateCarModelData}
import models.CarModel
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import repositories.{CarMakeRepository, CarModelRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarModelsController @Inject()(cc: MessagesControllerComponents, val carModelRepository: CarModelRepository, val carMakeRepository: CarMakeRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create(carMakeId: Long) = Action(parse.json) { request =>
    val result = request.body.validate[CreateCarModelData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      carModel => {
        carModelRepository.create(carModel.name, carModel.year, carMakeId)
        Ok(Json.obj("message" -> (s"Car model ${carModel.name} created")))
      }
    )
  }

  def getByCarMake(carMakeId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (carMakeId == -1)
      carModelRepository.getAll().map(carModels => Ok(Json.toJson(carModels)))
    else {
      carModelRepository.getByCarMakeId(carMakeId).map(carModels => Ok(Json.toJson(carModels)))
    }
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    carModelRepository.getByIdOption(id).map(carModel => carModel match {
      case Some(cm) => Ok(Json.toJson(cm))
      case None => Redirect(routes.CarModelsController.getByCarMake())
    })
  }

  def update(id: Long) = Action(parse.json) { request =>
    val result = request.body.validate[UpdateCarModelData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      carModel => {
        carModelRepository.update(id, CarModel(id, carModel.name, carModel.year, carModel.carMakeId))
        Ok(Json.obj("message" -> (s"Car model updated")))
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    carModelRepository.delete(id).map(_ => Ok(s"Car model $id deleted"))
  }

  def createForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    carMakeRepository.getAll().map(carMakes => Ok(views.html.carmodels.carModelAdd(CarModelForms.CreateForm, carMakes)))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    carMakeRepository.getAll().flatMap(carMakes =>
      CarModelForms.CreateForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(BadRequest(views.html.carmodels.carModelAdd(errorForm, carMakes)))
        },
        carModelData => {
          carModelRepository.create(carModelData.name, carModelData.year, carModelData.carMakeId).map { _ =>
            Redirect(routes.CarModelsController.createForm()).flashing("success" -> "Car model created")
          }
        }
      )
    )
  }

  def getByCarMakeForm(carMakeId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (carMakeId == -1) {
      carModelRepository.getAll().map(carModels => Ok(views.html.carmodels.carModels(carModels, null)))
    }
    else {
      carModelRepository.getByCarMakeId(carMakeId).flatMap(carModels => {
        carMakeRepository.getById(carMakeId).map(carMake => Ok(views.html.carmodels.carModels(carModels, carMake)))
      })
    }
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    carModelRepository.getById(id).flatMap(carModel => {
      carMakeRepository.getById(carModel.carMakeId).map(carMake => Ok(views.html.carmodels.carModelDetails(carModel, carMake)))
    })
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    carModelRepository.getById(id).map(carModel => {
      val updateForm = CarModelForms.UpdateForm.fill(UpdateCarModelData(carModel.id, carModel.name, carModel.year, carModel.carMakeId))
      Ok(views.html.carmodels.carModelUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CarModelForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.carmodels.carModelUpdate(errorForm)))
      },
      carModelData => {
        carModelRepository.update(carModelData.id, CarModel(carModelData.id, carModelData.name, carModelData.year, carModelData.carMakeId)).map { _ =>
          Redirect(routes.CarModelsController.updateForm(carModelData.id)).flashing("success" -> "Car model updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    carModelRepository.getById(id).map(carModel => {
      val deleteForm = CarModelForms.DeleteForm.fill(DeleteCarModelData(carModel.id, carModel.name, carModel.year, carModel.carMakeId))
      Ok(views.html.carmodels.carModelDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CarModelForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.carmodels.carModelDelete(errorForm)))
      },
      carModelData => {
        carModelRepository.delete(carModelData.id).map { _ =>
          Redirect(routes.CarModelsController.getByCarMakeForm()).flashing("success" -> "Car model deleted")
        }
      }
    )
  }
}