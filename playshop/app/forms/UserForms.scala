package forms

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}

case class CreateUserData(username: String, password: String)
case class UpdateUserData(id: Long, username: String, password: String)
case class DeleteUserData(id: Long, username: String)

object UserForms {
  val CreateForm: Form[CreateUserData] = Form {
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(CreateUserData.apply)(CreateUserData.unapply)
  }

  val UpdateForm: Form[UpdateUserData] = Form {
    mapping(
      "id" -> longNumber,
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UpdateUserData.apply)(UpdateUserData.unapply)
  }

  val DeleteForm: Form[DeleteUserData] = Form {
    mapping(
      "id" -> longNumber,
      "username" -> nonEmptyText
    )(DeleteUserData.apply)(DeleteUserData.unapply)
  }
}