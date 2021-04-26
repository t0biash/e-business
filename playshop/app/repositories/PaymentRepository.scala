package repositories

import models.Payment
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import userRepository.UserTable
  import orderRepository.OrderTable

  private val payment = TableQuery[PaymentTable]
  private val user = TableQuery[UserTable]
  private val order = TableQuery[OrderTable]

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "Payment") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def provider = column[String]("provider")
    def amount = column[BigDecimal]("amount")
    def completed = column[Boolean]("completed")
    def userId = column[Long]("userId")
    def orderId = column[Long]("orderId")
    def user_FK = foreignKey("user_FK", userId, user)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetNull)
    def order_FK = foreignKey("order_FK", orderId, order)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetNull)
    def * = (id, provider, amount, completed, userId, orderId) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  def create(provider: String, amount: BigDecimal, completed: Boolean, userId: Long, orderId: Long): Future[Payment] = db.run {
    (payment.map(p => (p.provider, p.amount, p.completed, p.userId, p.orderId))
      returning payment.map(_.id)
      into { case ((provider, amount, completed, userId, orderId), id) => Payment(id, provider, amount, completed, userId, orderId) }
    ) += (provider, amount, completed, userId, orderId)
  }

  def getAll(): Future[Seq[Payment]] = db.run(payment.result)

  def getByUserId(userId: Long): Future[Seq[Payment]] = db.run(payment.filter(_.userId === userId).result)

  def getById(id: Long): Future[Payment] = db.run(payment.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[Payment]] = db.run(payment.filter(_.id === id).result.headOption)

  def update(id: Long, updatedPayment: Payment): Future[Unit] = {
    val paymentToUpdate: Payment = updatedPayment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(payment.filter(_.id === id).delete.map(_ => ()))
}
