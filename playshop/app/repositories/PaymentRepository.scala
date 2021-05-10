package repositories

import models.Payment
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val payment = TableQuery[PaymentTable]

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "Payment") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def provider = column[String]("provider")
    def amount = column[BigDecimal]("amount")
    def completed = column[Boolean]("completed")
    def * = (id, provider, amount, completed) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  def create(provider: String, amount: BigDecimal, completed: Boolean): Future[Payment] = db.run {
    (payment.map(p => (p.provider, p.amount, p.completed))
      returning payment.map(_.id)
      into { case ((provider, amount, completed), id) => Payment(id, provider, amount, completed) }
    ) += (provider, amount, completed)
  }

  def getAll(): Future[Seq[Payment]] = db.run(payment.result)

  def getById(id: Long): Future[Payment] = db.run(payment.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[Payment]] = db.run(payment.filter(_.id === id).result.headOption)

  def update(id: Long, updatedPayment: Payment): Future[Unit] = {
    val paymentToUpdate: Payment = updatedPayment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(payment.filter(_.id === id).delete.map(_ => ()))
}
