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
    def orderId = column[Long]("orderId")
    def productId = column[Long]("productId")
    def orderFK = foreignKey("order_FK", orderId, order)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    def productFK = foreignKey("product_FK", productId, product)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.SetNull)
    def * = (id, orderId, productId) <> ((OrderProduct.apply _).tupled, OrderProduct.unapply)
  }

  def create(orderId: Long, productId: Long): Future[OrderProduct] = db.run {
    (orderProduct.map(op => (op.orderId, op.productId))
      returning orderProduct.map(_.id)
      into { case ((orderId, productId), id) => OrderProduct(id, orderId, productId) }
    ) += (orderId, productId)
  }

  def getAll(): Future[Seq[OrderProduct]] = db.run(orderProduct.result)

  def getById(id: Long): Future[OrderProduct] = db.run(orderProduct.filter(_.id === id).result.head)

  def getByOrderId(orderId: Long): Future[Seq[OrderProduct]] = db.run(orderProduct.filter(_.orderId === orderId).result)

  def update(id: Long, updatedOrderProduct: OrderProduct): Future[Unit] = {
    val orderProductToUpdate: OrderProduct = updatedOrderProduct.copy(id)
    db.run(orderProduct.filter(_.id === id).update(orderProductToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(orderProduct.filter(_.id === id).delete.map(_ => ()))
}
