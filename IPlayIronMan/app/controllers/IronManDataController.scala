package controllers

import javax.inject._

import models.{Workouts, Workout}
import play.api.libs.json.Json
import play.api.mvc._

import scala.sys.process._


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
    iter.foreach((line: String) => {
      println(line);
      val workout = Workout(0, 20)
      Workouts.add(workout);

    })
    val output = iter.mkString


    Ok(output)
  }

}
