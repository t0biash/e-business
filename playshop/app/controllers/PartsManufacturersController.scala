package controllers

import forms.{CreatePartsManufacturerData, DeletePartsManufacturerData, PartsManufacturerForms, UpdatePartsManufacturerData}
import models.PartsManufacturer
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import repositories.PartsManufacturerRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PartsManufacturersController @Inject()(cc: MessagesControllerComponents, val partsManufacturerRepository: PartsManufacturerRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create() = Action(parse.json) { request =>
    val result = request.body.validate[CreatePartsManufacturerData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      partsManufacturer => {
        partsManufacturerRepository.create(partsManufacturer.name, partsManufacturer.description)
        Ok(Json.obj("message" -> (s"Parts manufacturer ${partsManufacturer.name} created")))
      }
    )
  }

  def getAll(): Action[AnyContent] = Action.async { implicit request =>
    partsManufacturerRepository.getAll().map(partsManufacturers => Ok(Json.toJson(partsManufacturers)))
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    partsManufacturerRepository.getByIdOption(id).map(partsManufacturer => partsManufacturer match {
      case Some(pm) => Ok(Json.toJson(pm))
      case None => Redirect(routes.PartsManufacturersController.getAll())
    })
  }

  def update(id: Long) = Action(parse.json) { request =>
    val result = request.body.validate[UpdatePartsManufacturerData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      partsManufacturer => {
        partsManufacturerRepository.update(id, PartsManufacturer(id, partsManufacturer.name, partsManufacturer.description))
        Ok(Json.obj("message" -> (s"Parts manufacturer updated")))
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    partsManufacturerRepository.delete(id).map(_ => Ok(s"Parts manufacturer $id deleted"))
  }

  def createForm(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.partsmanufacturers.partsManufacturerAdd(PartsManufacturerForms.CreateForm))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    PartsManufacturerForms.CreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.partsmanufacturers.partsManufacturerAdd(errorForm)))
      },
      partsManufacturerData => {
        partsManufacturerRepository.create(partsManufacturerData.name, partsManufacturerData.description).map { _ =>
          Redirect(routes.PartsManufacturersController.createForm()).flashing("success" -> "Parts manufacturer created")
        }
      }
    )
  }

  def getAllForm(): Action[AnyContent] = Action.async { implicit request =>
    partsManufacturerRepository.getAll().map(partsManufacturers => Ok(views.html.partsmanufacturers.partsManufacturers(partsManufacturers)))
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    partsManufacturerRepository.getById(id).map(partsManufacturer => Ok(views.html.partsmanufacturers.partsManufacturerDetails(partsManufacturer)))
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    partsManufacturerRepository.getById(id).map(partsManufacturer => {
      val updateForm = PartsManufacturerForms.UpdateForm.fill(UpdatePartsManufacturerData(partsManufacturer.id, partsManufacturer.name, partsManufacturer.description))
      Ok(views.html.partsmanufacturers.partsManufacturerUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    PartsManufacturerForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.partsmanufacturers.partsManufacturerUpdate(errorForm)))
      },
      partsManufacturerData => {
        partsManufacturerRepository.update(partsManufacturerData.id, PartsManufacturer(partsManufacturerData.id, partsManufacturerData.name, partsManufacturerData.description)).map { _ =>
          Redirect(routes.PartsManufacturersController.updateForm(partsManufacturerData.id)).flashing("success" -> "Parts manufacturer updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    partsManufacturerRepository.getById(id).map(partsManufacturer => {
      val deleteForm = PartsManufacturerForms.DeleteForm.fill(DeletePartsManufacturerData(partsManufacturer.id, partsManufacturer.name))
      Ok(views.html.partsmanufacturers.partsManufacturerDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    PartsManufacturerForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.partsmanufacturers.partsManufacturerDelete(errorForm)))
      },
      partsManufacturerData => {
        partsManufacturerRepository.delete(partsManufacturerData.id).map { _ =>
          Redirect(routes.PartsManufacturersController.getAllForm()).flashing("success" -> "Parts manufacturer deleted")
        }
      }
    )
  }
}
