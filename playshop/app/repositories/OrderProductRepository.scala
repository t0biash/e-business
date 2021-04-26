package repositories

import models.OrderProduct
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val productRepository: ProductRepository, val orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import productRepository.ProductTable
  import orderRepository.OrderTable

  private val orderProduct = TableQuery[OrderProductTable]
  private val product = TableQuery[ProductTable]
  private val order = TableQuery[OrderTable]

  class OrderProductTable(tag: Tag) extends Table[OrderProduct](tag, "OrderProduct") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def quantity = column[Int]("quantity")
    def productId = column[Long]("productId")
    def orderId = column[Long]("orderId")
    def productId_FK = foreignKey("productId_FK", productId, product)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetNull)
    def orderId_FK = foreignKey("orderId_FK", orderId, order)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetNull)
    def * = (id, quantity, productId, orderId) <> ((OrderProduct.apply _).tupled, OrderProduct.unapply)
  }

  def create(quantity: Int, productId: Long, orderId: Long): Future[OrderProduct] = db.run {
    (orderProduct.map(op => (op.quantity, op.productId, op.orderId))
      returning orderProduct.map(_.id)
      into { case ((quantity, productId, orderId), id) => OrderProduct(id, quantity, productId, orderId) }
    ) += (quantity, productId, orderId)
  }

  def getAll(): Future[Seq[OrderProduct]] = db.run(orderProduct.result)

  def getById(id: Long): Future[OrderProduct] = db.run(orderProduct.filter(_.id === id).result.head)

  def update(id: Long, updatedOrderProduct: OrderProduct): Future[Unit] = {
    val orderProductToUpdate: OrderProduct = updatedOrderProduct.copy(id)
    db.run(orderProduct.filter(_.id === id).update(orderProductToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(orderProduct.filter(_.id === id).delete.map(_ => ()))
}
