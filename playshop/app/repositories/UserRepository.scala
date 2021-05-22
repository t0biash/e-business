package repositories

import models.User
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val user = TableQuery[UserTable]

  class UserTable(tag: Tag) extends Table[User](tag, "User") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")
    def * = (id, username, password) <> ((User.apply _).tupled, User.unapply)
  }

  def create(username: String, password: String): Future[User] = db.run {
    (user.map(u => (u.username, u.password))
      returning user.map(_.id)
      into { case ((username, password), id) => User(id, username, password) }
    ) += (username, password)
  }

  def getAll(): Future[Seq[User]] = db.run(user.result)

  def getById(id: Long): Future[User] = db.run(user.filter(_.id === id).result.head)

  def getByIdOption(id: Long): Future[Option[User]] = db.run(user.filter(_.id === id).result.headOption)

  def getByUsernameOption(username: String): Future[Option[User]] = db.run(user.filter(_.username === username).result.headOption)

  def update(id: Long, updatedUser: User): Future[Unit] = {
    val userToUpdate: User = updatedUser.copy(id)
    db.run(user.filter(_.id === id).update(userToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = db.run(user.filter(_.id === id).delete.map(_ => ()))
}
