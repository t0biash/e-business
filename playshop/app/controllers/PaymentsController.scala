package controllers

import forms.{DeletePaymentData, PaymentForms, UpdatePaymentData}
import models.{Order, Payment}
import play.api.mvc._
import repositories.{OrderRepository, PaymentRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentsController @Inject()(cc: MessagesControllerComponents, val paymentRepository: PaymentRepository, val orderRepository: OrderRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  private val _paymentProviders: Seq[String] = List("PayPal", "Blik", "Western Union", "Bitcoin")

  def create(orderId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create order $orderId payment")
  }

  def getByOrderId(orderId: Long): Action[AnyContent] = Action { implicit request =>
    if (orderId == -1)
      Ok("All payments")
    else
      Ok(s"All order $orderId payments")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Payment $id")
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update payment $id")
  }

  def delete(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete payment $id")
  }

  def createForm(orderId: Long): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.payments.paymentAdd(PaymentForms.CreateForm, _paymentProviders, orderId))
  }

  def createFormHandle(orderId: Long): Action[AnyContent] = Action.async { implicit request =>
    PaymentForms.CreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.payments.paymentAdd(errorForm, _paymentProviders, orderId)))
      },
      paymentData => {
        paymentRepository.create(paymentData.provider, paymentData.amount, false).flatMap { payment =>
          orderRepository.getById(orderId).map(order => {
            orderRepository.update(order.id, Order(order.id, order.date, order.userId, payment.id))
            Redirect(routes.PaymentsController.createForm(order.id)).flashing("success" -> "Payment created")
          })
        }
      }
    )
  }

  def getByOrderIdForm(orderId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (orderId == -1) {
      paymentRepository.getAll().map(payments => Ok(views.html.payments.payments(payments)))
    }
    else {
      paymentRepository.getAll().map(payments => {
        val completedPayments = payments.filter(_.completed == true)
        Ok(views.html.payments.payments(completedPayments))
      })
    }
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    paymentRepository.getById(id).map(payment => Ok(views.html.payments.paymentDetails(payment)))
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.getById(id).map(payment => {
      val updateForm = PaymentForms.UpdateForm.fill(UpdatePaymentData(payment.id, payment.provider, payment.amount, payment.completed))
      Ok(views.html.payments.paymentUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    PaymentForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.payments.paymentUpdate(errorForm)))
      },
      paymentData => {
        paymentRepository.update(paymentData.id, Payment(paymentData.id, paymentData.provider, paymentData.amount, paymentData.completed)).map { _ =>
          Redirect(routes.PaymentsController.updateForm(paymentData.id)).flashing("success" -> "Payment updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    paymentRepository.getById(id).map(payment => {
      val deleteForm = PaymentForms.DeleteForm.fill(DeletePaymentData(payment.id, payment.provider, payment.amount, payment.completed))
      Ok(views.html.payments.paymentDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    PaymentForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.payments.paymentDelete(errorForm)))
      },
      paymentData => {
        paymentRepository.delete(paymentData.id).map { _ =>
          Redirect(routes.PaymentsController.getByOrderIdForm()).flashing("success" -> "Payment deleted")
        }
      }
    )
  }
}
