package repositories

import models.Product
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val categoryRepository: CategoryRepository, val partsManufacturerRepository: PartsManufacturerRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import categoryRepository.CategoryTable
  import partsManufacturerRepository.PartsManufacturerTable

  private val product = TableQuery[ProductTable]
  private val category = TableQuery[CategoryTable]
  private val partsManufacturer = TableQuery[PartsManufacturerTable]

  class ProductTable(tag: Tag) extends Table[Product](tag, "Product") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")
    def price = column[BigDecimal]("price")
    def partsManufacturerId = column[Long]("partsManufacturerId")
    def categoryId = column[Long]("categoryId")
    def partsManufacturer_FK = foreignKey("partsManufacturer_FK", partsManufacturerId, partsManufacturer)(_.id, onUpdate=ForeignKeyAction.SetNull, onDelete=ForeignKeyAction.SetNull)
    def category_FK = foreignKey("category_FK", categoryId, category)(_.id, onUpdate=ForeignKeyAction.SetNull, onDelete=ForeignKeyAction.SetNull)
    def * = (id, name, description, price, partsManufacturerId, categoryId) <> ((Product.apply _).tupled, Product.unapply)
  }

  def create(name: String, description: String, price: BigDecimal, partsManufacturerId: Long, categoryId: Long): Future[Product] = db.run {
    (product.map(p => (p.name, p.description, p.price, p.partsManufacturerId, p.categoryId))
      returning product.map(_.id)
      into { case ((name, description, price, partsManufacturerId, categoryId), id) => Product(id, name, description, price, partsManufacturerId, categoryId) }
    ) += (name, description, price, partsManufacturerId, categoryId)
  }

  def getAll(): Future[Seq[Product]] = db.run(product.result)

  def getByCategoryId(categoryId: Long): Future[Seq[Product]] = db.run(product.filter(_.categoryId === categoryId).result)

  def getById(id: Long): Future[Product] = db.run(product.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[Product]] = db.run(product.filter(_.id === id).result.headOption)

  def update(id: Long, updatedProduct: Product): Future[Unit] = {
    val productToUpdate: Product = updatedProduct.copy(id)
    db.run(product.filter(_.id === id).update(productToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(product.filter(_.id === id).delete.map(_ => ()))
}
