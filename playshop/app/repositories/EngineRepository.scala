package repositories

import models.Engine
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EngineRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val carModelRepository: CarModelRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import carModelRepository.CarModelTable

  private val engine = TableQuery[EngineTable]
  private val carModel = TableQuery[CarModelTable]

  class EngineTable(tag: Tag) extends Table[Engine](tag, "Engine") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def carModelId = column[Long]("carModelId")
    def carModel_FK = foreignKey("carModel_FK", carModelId, carModel)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    def * = (id, name, carModelId) <> ((Engine.apply _).tupled, Engine.unapply)
  }

  def create(name: String, carModelId: Long): Future[Engine] = db.run {
    (engine.map(e => (e.name, e.carModelId))
      returning engine.map(_.id)
      into { case ((name, carModelId), id) => Engine(id, name, carModelId) }
    ) += (name, carModelId)
  }

  def getAll(): Future[Seq[Engine]] = db.run(engine.result)

  def getByCarModelId(carModelId: Long): Future[Seq[Engine]] = db.run(engine.filter(_.carModelId === carModelId).result)

  def getById(id: Long): Future[Engine] = db.run(engine.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[Engine]] = db.run(engine.filter(_.id === id).result.headOption)

  def update(id: Long, updatedEngine: Engine): Future[Unit] = {
    val engineToUpdate: Engine = updatedEngine.copy(id)
    db.run(engine.filter(_.id === id).update(engineToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(engine.filter(_.id === id).delete.map(_ => ()))
}
