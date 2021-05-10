package controllers

import forms.{DeletePromotionData, PromotionForms, UpdatePromotionData}
import models.Promotion
import play.api.mvc._
import repositories.{ProductRepository, PromotionRepository}

import java.sql.Date
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PromotionsController @Inject()(cc: MessagesControllerComponents, val promotionRepository: PromotionRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create(productId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create product $productId promotion")
  }

  def getByProductId(productId: Long): Action[AnyContent] = Action { implicit request =>
    if (productId == -1)
      Ok("All promotions")
    else
      Ok(s"All product $productId promotions")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Promotion $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update promotion $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete promotion $id")
  }

  def createForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getAll().map(products => Ok(views.html.promotions.promotionAdd(PromotionForms.CreateForm, products)))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    productRepository.getAll().flatMap(products =>
      PromotionForms.CreateForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(BadRequest(views.html.promotions.promotionAdd(errorForm, products)))
        },
        promotionData => {
          promotionRepository.create(promotionData.percentage.floatValue(), promotionData.fromDate.toString, promotionData.toDate.toString, promotionData.productId).map { _ =>
            Redirect(routes.PromotionsController.createForm()).flashing("success" -> "Promotion created")
          }
        }
      )
    )
  }

  def getByProductIdForm(productId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (productId == -1) {
      promotionRepository.getAll().map(promotions => Ok(views.html.promotions.promotions(promotions, null)))
    }
    else {
      promotionRepository.getByProductId(productId).flatMap(promotions => {
        productRepository.getById(productId).map(product => Ok(views.html.promotions.promotions(promotions, product)))
      })
    }
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    promotionRepository.getById(id).flatMap(promotion => {
      productRepository.getById(promotion.productId).map(product => Ok(views.html.promotions.promotionDetails(promotion, product)))
    })
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    promotionRepository.getById(id).map(promotion => {
      val updateForm = PromotionForms.UpdateForm.fill(UpdatePromotionData(promotion.id, BigDecimal.valueOf(promotion.percentage), Date.valueOf(promotion.fromDate), Date.valueOf(promotion.toDate), promotion.productId))
      Ok(views.html.promotions.promotionUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    PromotionForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.promotions.promotionUpdate(errorForm)))
      },
      promotionData => {
        promotionRepository.update(promotionData.id, Promotion(promotionData.id, promotionData.percentage.floatValue(), promotionData.fromDate.toString, promotionData.toDate.toString, promotionData.productId)).map { _ =>
          Redirect(routes.PromotionsController.updateForm(promotionData.id)).flashing("success" -> "Promotion updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    promotionRepository.getById(id).map(promotion => {
      val deleteForm = PromotionForms.DeleteForm.fill(DeletePromotionData(promotion.id, BigDecimal.valueOf(promotion.percentage), Date.valueOf(promotion.fromDate), Date.valueOf(promotion.toDate)))
      Ok(views.html.promotions.promotionDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    PromotionForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.promotions.promotionDelete(errorForm)))
      },
      promotionData => {
        promotionRepository.delete(promotionData.id).map { _ =>
          Redirect(routes.PromotionsController.getByProductIdForm()).flashing("success" -> "Promotion deleted")
        }
      }
    )
  }
}
