package controllers

import javax.inject._

import models.{Workout, Workouts}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json.Json
import play.api.mvc._

import scala.sys.process._
import scala.util.{Failure, Success}

import scala.concurrent._
import ExecutionContext.Implicits.global


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class IronManDataController @Inject() extends Controller {



  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.IAmIronMan("I am Iron Man"))
  }

  def getCyclingDistanceTimeSeries = Action {

    val res = Seq("C:\\Program Files\\R\\R-3.2.4revised\\bin\\x64\\Rscript.exe", "C:\\git\\IAmIronMan\\iRironMan\\running-log.R").!!
    val jsonString = """{"name": "n name", "description": "new description"}"""
    val json = Json.parse(jsonString)
    Ok(res)
  }

  def getWorkoutCsvFromGoogleDocs = Action {
    val url = "https://docs.google.com/spreadsheets/d/1qyhjFF4CcjeSu__88_WOLjFumuguDmVWTHbOlrikEvI/pub?gid=0&single=true&output=csv"
    val result = scala.io.Source.fromURL(url).mkString
    val iter = result.split("\n").drop(1)
    println("Iterating over lines")
    val formatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
    iter.foreach((line: String) => {
      println(line);
      val tokens = line.split(",")

      val rawPace = tokens(3);

      val pace = calculateDuration(rawPace);

      val rawTime = tokens(2)
      val time = calculateDuration(rawTime);
      val distance = tokens(1).toDouble;
      val speed = distance * 1000 / time;
      //Because m/s makes more sense than km/s
      val maxSpeed = (Option(tokens(5)).filterNot(_.isEmpty).getOrElse(0)).toString.toDouble
      val workoutType = tokens(6)
      val timeOfDay = calculateDuration(Option(tokens(7)).filterNot(_.isEmpty).getOrElse(0).toString);
      val elevationGain = calculateDuration(Option(tokens(8)).filterNot(_.isEmpty).getOrElse(0).toString);
      val elevationLoss = calculateDuration(Option(tokens(9)).filterNot(_.isEmpty).getOrElse(0).toString);
      val exerciseDate = formatter.parseDateTime(tokens(0))
      val workout = Workout(0,
        exerciseDate,
        distance,
        time,
        pace,
        speed,
        maxSpeed,
        workoutType,
        timeOfDay,
        elevationGain,
        elevationLoss
      );

        Workouts.exists(exerciseDate, timeOfDay).onComplete( {
          case Success(exercisesExists) => {
            println(exercisesExists)
            if (!exercisesExists) {
              Workouts.add(workout);
            } else {
              print("Exercise exists, skipping")
            }
          }
          case Failure(ex) => print("Exception :o" + ex.getMessage())
        })
    })
    val output = iter.mkString


    Ok(output)
  }

  //This function turns a time, e.g 07:45 into a duration, e.g 445 seconds.
  //It can also turn a time of day into minutes from midnight 12:00 -> 720 minutes
  private def calculateDuration(rawTime: String): Int ={
    if (rawTime.isEmpty()) {
      return 0
    }
    //It is assumed that the raw pace is always in the range of a couple of minutes
    val splitComponents = rawTime.split(":");
    //We reverse the order so that we can deal with the case of a duration in minutes and a duration in hours
    val reverseOrderedList = splitComponents.reverse.toList;

    def calcTime(lst : List[String], time: Int, iteration: Int): Int = {
      if (lst.isEmpty) {
        return time
      }
      else {
        if (iteration > 0) {
          calcTime(lst.tail, time + lst.head.toString.toInt * scala.math.pow(60.toDouble, iteration.toDouble).toInt , iteration + 1)
        } else
        {
          calcTime(lst.tail, lst.head.toString.toInt, iteration + 1)
        }
      }
    }


    val totalSeconds = calcTime(reverseOrderedList, 0, 0)
    print(totalSeconds);
    totalSeconds
  }

}
