package controllers

import forms.{DeleteOrderData, OrderForms, UpdateOrderData}
import models.Order
import play.api.mvc._
import repositories.{OrderRepository, PaymentRepository, UserRepository}

import java.sql.Date
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrdersController @Inject()(cc: MessagesControllerComponents, val orderRepository: OrderRepository, val userRepository: UserRepository, val paymentRepository: PaymentRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create(userId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Create user $userId orderDetails")
  }

  def getByUserId(userId: Long): Action[AnyContent] = Action { implicit request =>
    if (userId == -1)
      Ok("All orders")
    else
      Ok(s"All user $userId orders")
  }

  def getById(id: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Order $id")
  }

  def update(orderId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Update orderDetails $orderId")
  }

  def delete(orderId: Long): Action[AnyContent] = Action { implicit request =>
    Ok(s"Delete orderDetails $orderId")
  }

  def createForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    userRepository.getAll().map(users => Ok(views.html.orders.orderAdd(OrderForms.CreateForm, users)))
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    userRepository.getAll().flatMap(users => {
      OrderForms.CreateForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(BadRequest(views.html.orders.orderAdd(errorForm, users)))
        },
        orderData => {
          orderRepository.create(orderData.date.toString, orderData.userId, -1).map { _ =>
            Redirect(routes.OrdersController.createForm()).flashing("success" -> "Order created")
          }
        })
      })
  }

  def getByUserIdForm(userId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (userId == -1) {
      orderRepository.getAll().map(orders => Ok(views.html.orders.orders(orders, null)))
    }
    else {
      orderRepository.getAll().flatMap(orders => {
        userRepository.getById(userId).map(user => Ok(views.html.orders.orders(orders, user)))
      })
    }
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    orderRepository.getById(id).flatMap(order => {
      paymentRepository.getByIdOption(order.paymentId).flatMap(payment => {
        userRepository.getById(order.userId).map(user => Ok(views.html.orders.orderDetails(order, user, payment.getOrElse(null))))
      })
    })
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.getById(id).map(order => {
      val updateForm = OrderForms.UpdateForm.fill(UpdateOrderData(order.id, Date.valueOf(order.date), order.userId, order.paymentId))
      Ok(views.html.orders.orderUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    OrderForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.orders.orderUpdate(errorForm)))
      },
      orderData => {
        orderRepository.update(orderData.id, Order(orderData.id, orderData.date.toString, orderData.userId, orderData.paymentId)).map { _ =>
          Redirect(routes.OrdersController.updateForm(orderData.id)).flashing("success" -> "Order updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    orderRepository.getById(id).map(order => {
      val deleteForm = OrderForms.DeleteForm.fill(DeleteOrderData(order.id, Date.valueOf(order.date)))
      Ok(views.html.orders.orderDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    OrderForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.orders.orderDelete(errorForm)))
      },
      orderData => {
        orderRepository.delete(orderData.id).map { _ =>
          Redirect(routes.OrdersController.getByUserIdForm()).flashing("success" -> "Order deleted")
        }
      }
    )
  }
}
