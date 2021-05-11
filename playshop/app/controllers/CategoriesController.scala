package controllers

import forms.{CategoryForms, CreateCategoryData, DeleteCategoryData, UpdateCategoryData}
import models.Category
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import repositories.CategoryRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoriesController @Inject()(cc: MessagesControllerComponents, val categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create() = Action(parse.json) { request =>
    val result = request.body.validate[CreateCategoryData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      category => {
        categoryRepository.create(category.name)
        Ok(Json.obj("message" -> (s"Category ${category.name} created")))
      }
    )
  }

  def getAll(): Action[AnyContent] = Action.async { implicit request =>
    categoryRepository.getAll().map(categories => Ok(Json.toJson(categories)))
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    categoryRepository.getByIdOption(id).map(category => category match {
      case Some(c) => Ok(Json.toJson(c))
      case None => Redirect(routes.CategoriesController.getAll())
    })
  }

  def update(id: Long) = Action(parse.json) { request =>
    val result = request.body.validate[UpdateCategoryData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      category => {
        categoryRepository.update(id, Category(id, category.name))
        Ok(Json.obj("message" -> (s"Category updated")))
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    categoryRepository.delete(id).map(_ => Ok(s"Category $id deleted"))
  }

  def createForm(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.categories.categoryAdd(CategoryForms.CreateForm))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CategoryForms.CreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.categories.categoryAdd(errorForm)))
      },
      categoryData => {
        categoryRepository.create(categoryData.name).map { _ =>
          Redirect(routes.CategoriesController.createForm()).flashing("success" -> "Category created")
        }
      }
    )
  }

  def getAllForm(): Action[AnyContent] = Action.async { implicit request =>
    categoryRepository.getAll().map(categories => Ok(views.html.categories.categories(categories)))
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    categoryRepository.getById(id).map(category => Ok(views.html.categories.categoryDetails(category)))
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    categoryRepository.getById(id).map(category => {
      val updateForm = CategoryForms.UpdateForm.fill(UpdateCategoryData(category.id, category.name))
      Ok(views.html.categories.categoryUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CategoryForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.categories.categoryUpdate(errorForm)))
      },
      categoryData => {
        categoryRepository.update(categoryData.id, Category(categoryData.id, categoryData.name)).map { _ =>
          Redirect(routes.CategoriesController.updateForm(categoryData.id)).flashing("success" -> "Category updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    categoryRepository.getById(id).map(category => {
      val deleteForm = CategoryForms.DeleteForm.fill(DeleteCategoryData(category.id, category.name))
      Ok(views.html.categories.categoryDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    CategoryForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.categories.categoryDelete(errorForm)))
      },
      categoryData => {
        categoryRepository.delete(categoryData.id).map { _ =>
          Redirect(routes.CategoriesController.getAllForm()).flashing("success" -> "Category deleted")
        }
      }
    )
  }
}
