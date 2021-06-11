package controllers

import play.api.libs.json.Json
import play.api.mvc._
import repositories.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class UsersController @Inject()(cc: MessagesControllerComponents, val userRepository: UserRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def getEmailById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    userRepository.getById(id).map(user => Ok(Json.toJson(user.email)))
  }
}
