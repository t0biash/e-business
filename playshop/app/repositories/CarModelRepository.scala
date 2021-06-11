package repositories

import models.CarModel
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarModelRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val carMakeRepository: CarMakeRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import carMakeRepository.CarMakeTable

  private val carModel = TableQuery[CarModelTable]
  private val carMake = TableQuery[CarMakeTable]

  class CarModelTable(tag: Tag) extends Table[CarModel](tag, "CarModel") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def year = column[Int]("year")
    def carMakeId = column[Long]("carMakeId")
    def carMakeFK = foreignKey("carMake_FK", carMakeId, carMake)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    def * = (id, name, year, carMakeId) <> ((CarModel.apply _).tupled, CarModel.unapply)
  }

  def create(name: String, year: Int, carMakeId: Long): Future[CarModel] = db.run {
    (carModel.map(cm => (cm.name, cm.year, cm.carMakeId))
      returning carModel.map(_.id)
      into { case ((name, year, carMakeId), id) => CarModel(id, name, year, carMakeId) }
    ) += (name, year, carMakeId)
  }

  def getAll(): Future[Seq[CarModel]] = db.run(carModel.result)

  def getByCarMakeId(carMakeId: Long): Future[Seq[CarModel]] = db.run(carModel.filter(_.carMakeId === carMakeId).result)

  def getById(id: Long): Future[CarModel] = db.run(carModel.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[CarModel]] = db.run(carModel.filter(_.id === id).result.headOption)

  def update(id: Long, updatedCarModel: CarModel): Future[Unit] = {
    val carModelToUpdate: CarModel = updatedCarModel.copy(id)
    db.run(carModel.filter(_.id === id).update(carModelToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(carModel.filter(_.id === id).delete.map(_ => ()))
}
