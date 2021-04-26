package repositories

import models.Category
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val category = TableQuery[CategoryTable]

  class CategoryTable(tag: Tag) extends Table[Category](tag, "Category") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  def create(name: String): Future[Category] = db.run {
    (category.map(c => c.name)
      returning category.map(_.id)
      into { case ((name), id) => Category(id, name) }
    ) += (name)
  }

  def getAll(): Future[Seq[Category]] = db.run(category.result)

  def getById(id: Long): Future[Category] = db.run(category.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[Category]] = db.run(category.filter(_.id === id).result.headOption)

  def update(id: Long, updatedCategory: Category): Future[Unit] = {
    val categoryToUpdate: Category = updatedCategory.copy(id)
    db.run(category.filter(_.id === id).update(categoryToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(category.filter(_.id === id).delete.map(_ => ()))
}
