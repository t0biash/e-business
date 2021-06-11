package repositories

import models.{Promotion}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PromotionRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import productRepository.ProductTable

  private val promotion = TableQuery[PromotionTable]
  private val product = TableQuery[ProductTable]

  class PromotionTable(tag: Tag) extends Table[Promotion](tag, "Promotion") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def percentage = column[Float]("percentage")
    def fromDate = column[String]("fromDate")
    def toDate = column[String]("toDate")
    def productId = column[Long]("productId")
    def productFK = foreignKey("product_FK", productId, product)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetNull)
    def * = (id, percentage, fromDate, toDate, productId) <> ((Promotion.apply _).tupled, Promotion.unapply)
  }

  def create(percentage: Float, fromDate: String, toDate: String, productId: Long): Future[Promotion] = db.run {
    (promotion.map(p => (p.percentage, p.fromDate, p.toDate, p.productId))
      returning promotion.map(_.id)
      into { case ((percentage, fromDate, toDate, productId), id) => Promotion(id, percentage, fromDate, toDate, productId) }
    ) += (percentage, fromDate, toDate, productId)
  }

  def getAll(): Future[Seq[Promotion]] = db.run(promotion.result)

  def getByProductId(productId: Long): Future[Seq[Promotion]] = db.run(promotion.filter(_.productId === productId).result)

  def getById(id: Long): Future[Promotion] = db.run(promotion.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[Promotion]] = db.run(promotion.filter(_.id === id).result.headOption)

  def update(id: Long, updatedPromotion: Promotion): Future[Unit] = {
    val promotionToUpdate: Promotion = updatedPromotion.copy(id)
    db.run(promotion.filter(_.id === id).update(promotionToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(promotion.filter(_.id === id).delete.map(_ => ()))
}
