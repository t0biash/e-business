package repositories

import models.CarMake
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarMakeRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val carMake = TableQuery[CarMakeTable]

  class CarMakeTable(tag: Tag) extends Table[CarMake](tag, "CarMake") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (id, name) <> ((CarMake.apply _).tupled, CarMake.unapply)
  }

  def create(name: String): Future[CarMake] = db.run {
    (carMake.map(cm => cm.name)
      returning carMake.map(_.id)
      into { case ((name), id) => CarMake(id, name) }
    ) += (name)
  }

  def getAll(): Future[Seq[CarMake]] = db.run(carMake.result)

  def getById(id: Long): Future[CarMake] = db.run(carMake.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[CarMake]] = db.run(carMake.filter(_.id === id).result.headOption)

  def update(id: Long, updatedCarMake: CarMake): Future[Unit] = {
    val carMakeToUpdate: CarMake = updatedCarMake.copy(id)
    db.run(carMake.filter(_.id === id).update(carMakeToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(carMake.filter(_.id === id).delete.map(_ => ()))
}
