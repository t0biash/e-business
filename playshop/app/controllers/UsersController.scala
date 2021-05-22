package controllers

import forms.{CreateUserData, DeleteUserData, UpdateUserData, UserForms}
import play.api.mvc._
import repositories.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import models.User
import play.api.libs.json.{JsError, Json}

@Singleton
class UsersController @Inject()(cc: MessagesControllerComponents, val userRepository: UserRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create() = Action(parse.json) { request =>
    val result = request.body.validate[CreateUserData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      user => {
        userRepository.create(user.username, user.password)
        Ok(Json.obj("message" -> (s"User ${user.username} created")))
      }
    )
  }

  def getAll(username: String): Action[AnyContent] = Action.async { implicit request =>
    if (username == "")
      userRepository.getAll().map(users => Ok(Json.toJson(users)))
    else
      userRepository.getByUsernameOption(username).map(user => user match {
        case Some(u) => Ok(Json.toJson(u))
        case None => Redirect(routes.UsersController.getAll())
      })
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userRepository.getByIdOption(id).map(user => user match {
      case Some(u) => Ok(Json.toJson(u))
      case None => Redirect(routes.UsersController.getAll())
    })
  }

  def update(id: Long) = Action(parse.json) { request =>
    val result = request.body.validate[UpdateUserData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      user => {
        userRepository.update(id, User(id, user.username, user.password))
        Ok(Json.obj("message" -> (s"User updated")))
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userRepository.delete(id).map(_ => Ok(s"User $id deleted"))
  }

  def createForm(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.users.userAdd(UserForms.CreateForm))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    UserForms.CreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.users.userAdd(errorForm)))
      },
      userData => {
        userRepository.create(userData.username, userData.password).map { _ =>
          Redirect(routes.UsersController.createForm()).flashing("success" -> "User created")
        }
      }
    )
  }

  def getAllForm(): Action[AnyContent] = Action.async { implicit request =>
    userRepository.getAll().map(users => Ok(views.html.users.users(users)))
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userRepository.getById(id).map(user => Ok(views.html.users.userDetails(user)))
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    userRepository.getById(id).map(user => {
      val updateForm = UserForms.UpdateForm.fill(UpdateUserData(user.id, user.username, user.password))
      Ok(views.html.users.userUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    UserForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.users.userUpdate(errorForm)))
      },
      userData => {
        userRepository.update(userData.id, User(userData.id, userData.username, userData.password)).map { _ =>
          Redirect(routes.UsersController.updateForm(userData.id)).flashing("success" -> "User updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    userRepository.getById(id).map(user => {
      val deleteForm = UserForms.DeleteForm.fill(DeleteUserData(user.id, user.username))
      Ok(views.html.users.userDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    UserForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.users.userDelete(errorForm)))
      },
      userData => {
        userRepository.delete(userData.id).map { _ =>
          Redirect(routes.UsersController.getAllForm()).flashing("success" -> "User deleted")
        }
      }
    )
  }
}
