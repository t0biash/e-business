package repositories

import models.Order
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val paymentRepository: PaymentRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import userRepository.UserTable
  import paymentRepository.PaymentTable

  private val order = TableQuery[OrderTable]
  private val user = TableQuery[UserTable]
  private val payment = TableQuery[PaymentTable]

  class OrderTable(tag: Tag) extends Table[Order](tag, "Order") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def date = column[String]("date")
    def userId = column[Long]("userId")
    def paymentId = column[Long]("paymentId")
    def userId_FK = foreignKey("userId_FK", userId, user)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetNull)
    def paymentId_FK = foreignKey("paymentId_FK", paymentId, payment)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetNull)
    def * = (id, date, userId, paymentId) <> ((Order.apply _).tupled, Order.unapply)
  }

  def create(date: String, userId: Long, paymentId: Long): Future[Order] = db.run {
    (order.map(o => (o.date, o.userId, o.paymentId))
      returning order.map(_.id)
      into { case ((date, userId, paymentId), id) => Order(id, date, userId, paymentId) }
    ) += (date, userId, paymentId)
  }

  def getAll(): Future[Seq[Order]] = db.run(order.result)

  def getByUserId(userId: Long): Future[Seq[Order]] = db.run(order.filter(_.userId === userId).result)

  def getById(id: Long): Future[Order] = db.run(order.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[Order]] = db.run(order.filter(_.id === id).result.headOption)

  def update(id: Long, updatedOrder: Order): Future[Unit] = {
    val orderToUpdate: Order = updatedOrder.copy(id)
    db.run(order.filter(_.id === id).update(orderToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(order.filter(_.id === id).delete.map(_ => ()))
}
