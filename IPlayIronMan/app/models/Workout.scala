package models
import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * Created by Ray on 4/7/2016.
 */



case class Workout (id: Long, date:DateTime,distance:Double, time: Int, pace: Int, speed:Double, maxSpeed: Double, workoutType: String, timeOfDay:Int, elevationGain: Int, elevationLoss: Int ){

}

class WorkoutTableDef(tag:Tag) extends Table[Workout](tag, "workout") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def date = column[DateTime]("date")
  def distance = column[Double]("distance")
  def time = column[Int]("time")
  def pace = column[Int]("pace")
  def speed = column[Double]("speed")
  def maxSpeed = column[Double]("maxSpeed")
  def workoutType = column[String]("type")
  def timeOfDay = column[Int]("time_of_day")
  def elevationGain = column[Int]("elevation_gain")
  def elevationLoss = column[Int]("elevation_loss")
  //def maxHeartRate = column[Int]("max_heart_rate")
  //def averageHeartRate = column[Double]("average_heart_rate")

  override def * =
    (id,date,distance, time, pace, speed, maxSpeed, workoutType, timeOfDay, elevationGain, elevationLoss) <> (Workout.tupled, Workout.unapply) /*pace, speed, maxSpeed, workoutType,
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

  def exists(date: DateTime, timeOfDay: Int) : Future[Boolean] = {
    dbConfig.db.run(workouts.filter(x => (x.date === date && x.timeOfDay === timeOfDay) ).exists.result);
  }

  def listAll: Future[Seq[Workout]] = {
    dbConfig.db.run(workouts.result)
  }

}