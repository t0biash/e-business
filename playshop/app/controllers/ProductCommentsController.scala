package controllers

import forms.{DeleteProductCommentData, ProductCommentForms, UpdateProductCommentData}
import models.ProductComment
import play.api.mvc._
import repositories.{ProductCommentRepository, ProductRepository, UserRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductCommentsController @Inject()(cc: MessagesControllerComponents, val productCommentRepository: ProductCommentRepository, val productRepository: ProductRepository, val userRepository: UserRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create(productId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create product $productId comment")
  }

  def getByProductId(productId: Long): Action[AnyContent] = Action { implicit request =>
    if (productId == -1)
      Ok("All products comments")
    else
      Ok(s"All product $productId comments")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Comment $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update comment $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete comment $id")
  }

  def createForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    userRepository.getAll().flatMap(users => {
      productRepository.getAll().map(products => Ok(views.html.productcomments.productCommentAdd(ProductCommentForms.CreateForm, users, products)))
    })
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    userRepository.getAll().flatMap(users => {
      productRepository.getAll().flatMap(products => {
        ProductCommentForms.CreateForm.bindFromRequest.fold(
          errorForm => {
            Future.successful(BadRequest(views.html.productcomments.productCommentAdd(errorForm, users, products)))
          },
          productCommentData => {
            productCommentRepository.create(productCommentData.rate, productCommentData.content, productCommentData.userId, productCommentData.productId).map { _ =>
              Redirect(routes.ProductCommentsController.createForm()).flashing("success" -> "Product comment created")
            }
          }
        )
      })
    })
  }

  def getByProductIdForm(productId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (productId == -1) {
      productCommentRepository.getAll().map(productComments => Ok(views.html.productcomments.productComments(productComments, null)))
    }
    else {
      productRepository.getById(productId).flatMap(product => {
        productCommentRepository.getAll().map(productComments => Ok(views.html.productcomments.productComments(productComments, product)))
      })
    }
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    productCommentRepository.getById(id).flatMap(productComment => {
      userRepository.getById(productComment.userId).flatMap(user => {
        productRepository.getById(productComment.productId).map(product => Ok(views.html.productcomments.productCommentDetails(productComment, user, product)))
      })
    })
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productCommentRepository.getById(id).map(productComment => {
      val updateForm = ProductCommentForms.UpdateForm.fill(UpdateProductCommentData(productComment.id, productComment.rate, productComment.content, productComment.userId, productComment.productId))
      Ok(views.html.productcomments.productCommentUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    ProductCommentForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.productcomments.productCommentUpdate(errorForm)))
      },
      productCommentData => {
        productCommentRepository.update(productCommentData.id, ProductComment(productCommentData.id, productCommentData.rate, productCommentData.content, productCommentData.userId, productCommentData.productId)).map { _ =>
          Redirect(routes.ProductCommentsController.updateForm(productCommentData.id)).flashing("success" -> "Product comment updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productCommentRepository.getById(id).map(productComment => {
      val deleteForm = ProductCommentForms.DeleteForm.fill(DeleteProductCommentData(productComment.id, productComment.rate, productComment.content))
      Ok(views.html.productcomments.productCommentDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    ProductCommentForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.productcomments.productCommentDelete(errorForm)))
      },
      productCommentData => {
        productCommentRepository.delete(productCommentData.id).map { _ =>
          Redirect(routes.ProductCommentsController.getByProductIdForm()).flashing("success" -> "Product comment deleted")
        }
      }
    )
  }
}