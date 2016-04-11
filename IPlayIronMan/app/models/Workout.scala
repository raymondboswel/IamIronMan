package models

import java.sql.Date

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by Ray on 4/7/2016.
 */



case class Workout (id: Long, date:Date,distance:Double ){

}

class WorkoutTableDef(tag:Tag) extends Table[Workout](tag, "workout") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def date = column[Date]("date")
  def distance = column[Double]("distance")
  //def time = column[Date]("time")
  //def pace = column[Date]("pace")
  //def speed = column[Double]("speed")
  //def maxSpeed = column[Double]("maxSpeed")
  //def workoutType = column[String]("type")
  //def timeOfDay = column[Date]("time_of_day")
  //def elevationGain = column[Int]("elevation_gain")
  //def elevationLoss = column[Int]("elevation_loss")
  //def maxHeartRate = column[Int]("max_heart_rate")
  //def averageHeartRate = column[Double]("average_heart_rate")

  override def * =
    (id,distance) <> (Workout.tupled, Workout.unapply) /*pace, speed, maxSpeed, workoutType,
      timeOfDay, elevationGain, elevationLoss, maxHeartRate, averageHeartRate*/
}

object Workouts {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val workouts = TableQuery[WorkoutTableDef]

  def add(workout: Workout): Future[String] = {
    dbConfig.db.run(workouts += workout).map(res => "User successfully added").recover{
      case ex: Exception => ex.getCause().getMessage()
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(workouts.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Workout]] = {
    dbConfig.db.run(workouts.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Workout]] = {
    dbConfig.db.run(workouts.result)
  }

}