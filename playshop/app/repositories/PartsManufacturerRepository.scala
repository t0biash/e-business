package repositories

import models.PartsManufacturer
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PartsManufacturerRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val partsManufacturer = TableQuery[PartsManufacturerTable]

  class PartsManufacturerTable(tag: Tag) extends Table[PartsManufacturer](tag, "PartsManufacturer") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")
    def * = (id, name, description) <> ((PartsManufacturer.apply _).tupled, PartsManufacturer.unapply)
  }

  def create(name: String, description: String): Future[PartsManufacturer] = db.run {
    (partsManufacturer.map(pf => (pf.name, pf.description))
      returning partsManufacturer.map(_.id)
      into { case ((name, description), id) => PartsManufacturer(id, name, description) }
    ) += (name, description)
  }

  def getAll(): Future[Seq[PartsManufacturer]] = db.run(partsManufacturer.result)

  def getById(id: Long): Future[PartsManufacturer] = db.run(partsManufacturer.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[PartsManufacturer]] = db.run(partsManufacturer.filter(_.id === id).result.headOption)

  def update(id: Long, updatedPartsManufacturer: PartsManufacturer): Future[Unit] = {
    val partsManufacturerToUpdate = updatedPartsManufacturer.copy(id)
    db.run(partsManufacturer.filter(_.id === id).update(partsManufacturerToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(partsManufacturer.filter(_.id === id).delete.map(_ => ()))
}
