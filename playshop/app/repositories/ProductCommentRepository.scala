package repositories

import models.ProductComment
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductCommentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import productRepository.ProductTable
  import userRepository.UserTable

  private val productComment = TableQuery[ProductCommentTable]
  private val user = TableQuery[UserTable]
  private val product = TableQuery[ProductTable]

  class ProductCommentTable(tag: Tag) extends Table[ProductComment](tag, "ProductComment") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def rate = column[Int]("rate")
    def content = column[String]("content")
    def userId = column[Long]("userId")
    def productId = column[Long]("productId")
    def userFK = foreignKey("user_FK", userId, user)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    def productFK = foreignKey("product_FK", productId, product)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    def * = (id, rate, content, userId, productId) <> ((ProductComment.apply _).tupled, ProductComment.unapply)
  }

  def create(rate: Int, content: String, userId: Long, productId: Long): Future[ProductComment] = db.run {
    (productComment.map(pc => (pc.rate, pc.content, pc.userId, pc.productId))
      returning productComment.map(_.id)
      into { case ((rate, content, userId, productId), id) => ProductComment(id, rate, content, userId, productId) }
    ) += (rate, content, userId, productId)
  }

  def getAll(): Future[Seq[ProductComment]] = db.run(productComment.result)

  def getByProductId(productId: Long): Future[Seq[ProductComment]] = db.run(productComment.filter(_.productId === productId).result)

  def getById(id: Long): Future[ProductComment] = db.run(productComment.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[ProductComment]] = db.run(productComment.filter(_.id === id).result.headOption)

  def update(id: Long, updatedProductComment: ProductComment): Future[Unit] = {
    val productCommentToUpdate: ProductComment = updatedProductComment.copy(id)
    db.run(productComment.filter(_.id === id).update(productCommentToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(productComment.filter(_.id === id).delete.map(_ => ()))
}
