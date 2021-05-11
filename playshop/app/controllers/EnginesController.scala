package controllers

import forms.{CreateEngineData, DeleteEngineData, EngineForms, UpdateEngineData}
import models.Engine
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import repositories.{CarModelRepository, EngineRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EnginesController @Inject()(cc: MessagesControllerComponents, val engineRepository: EngineRepository, val carModelRepository: CarModelRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create(carModelId: Long) = Action(parse.json) { request =>
    val result = request.body.validate[CreateEngineData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      engine => {
        engineRepository.create(engine.engine, carModelId)
        Ok(Json.obj("message" -> (s"Engine ${engine.engine} created")))
      }
    )
  }

  def getByCarModel(carModelId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (carModelId == -1)
      engineRepository.getAll().map(engines => Ok(Json.toJson(engines)))
    else {
      engineRepository.getByCarModelId(carModelId).map(engines => Ok(Json.toJson(engines)))
    }
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    engineRepository.getByIdOption(id).map(engine => engine match {
      case Some(e) => Ok(Json.toJson(e))
      case None => Redirect(routes.EnginesController.getByCarModel())
    })
  }

  def update(id: Long) = Action(parse.json) { request =>
    val result = request.body.validate[UpdateEngineData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      engine => {
        engineRepository.update(id, Engine(id, engine.engine, engine.carModelId))
        Ok(Json.obj("message" -> (s"Engine updated")))
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    engineRepository.delete(id).map(_ => Ok(s"Engine $id deleted"))
  }

  def createForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    carModelRepository.getAll().map(carModels => Ok(views.html.engines.engineAdd(EngineForms.CreateForm, carModels)))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    carModelRepository.getAll().flatMap(carModels =>
      EngineForms.CreateForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(BadRequest(views.html.engines.engineAdd(errorForm, carModels)))
        },
        engineData => {
          engineRepository.create(engineData.engine, engineData.carModelId).map { _ =>
            Redirect(routes.EnginesController.createForm()).flashing("success" -> "Engine created")
          }
        }
      )
    )
  }

  def getByCarModelForm(carModelId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (carModelId == -1) {
      engineRepository.getAll().map(engines => Ok(views.html.engines.engines(engines, null)))
    }
    else {
      engineRepository.getByCarModelId(carModelId).flatMap(engines => {
        carModelRepository.getById(carModelId).map(carModel => Ok(views.html.engines.engines(engines, carModel)))
      })
    }
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    engineRepository.getById(id).flatMap(engine => {
      carModelRepository.getById(engine.carModelId).map(carModel => Ok(views.html.engines.engineDetails(engine, carModel)))
    })
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    engineRepository.getById(id).map(engine => {
      val updateForm = EngineForms.UpdateForm.fill(UpdateEngineData(engine.id, engine.engine, engine.carModelId))
      Ok(views.html.engines.engineUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    EngineForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.engines.engineUpdate(errorForm)))
      },
      engineData => {
        engineRepository.update(engineData.id, Engine(engineData.id, engineData.engine, engineData.carModelId)).map { _ =>
          Redirect(routes.EnginesController.updateForm(engineData.id)).flashing("success" -> "Car model updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    engineRepository.getById(id).map(engine => {
      val deleteForm = EngineForms.DeleteForm.fill(DeleteEngineData(engine.id, engine.engine, engine.carModelId))
      Ok(views.html.engines.engineDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    EngineForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.engines.engineDelete(errorForm)))
      },
      engineData => {
        engineRepository.delete(engineData.id).map { _ =>
          Redirect(routes.EnginesController.getByCarModelForm(engineData.carModelId)).flashing("success" -> "Car model deleted")
        }
      }
    )
  }
}
