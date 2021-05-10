package controllers

import forms.{DeleteUserData, UpdateUserData, UserForms}
import play.api.mvc._
import repositories.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import models.User

@Singleton
class UsersController @Inject()(cc: MessagesControllerComponents, val userRepository: UserRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
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
