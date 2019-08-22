import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class Base extends Simulation {

  before {
    println("Base is about to start!")
  }

  val baseURL: String = "http://computer-database.gatling.io/computers"
  val baseURL2: String = "http://computer-database.gatling.io/computers/new"
  val numberOfUsers = (System.getenv("NUMBER_OF_USERS")).toInt
  //val numberOfUsers2 = (System.getenv("NUMBER_OF_USERS_2")).toInt
  val runDuration = (System.getenv("RUN_DURATION")).toInt

  val httpProtocol = http
    .baseUrl(baseURL)

  val httpProtocol2 = http
    .baseUrl(baseURL2)

  val feeder = csv("ComputerName.csv").circular
  val feederId = csv("Id.csv").random

  //Reusable Objects. Just extend BusinessProcessSimulation in any other scala file

  object GetComputer {
    val getComputer = exec(http("GET Computers") // let's give proper names, as they are displayed in the reports
      .get("").check(status.is(200)))
  }

  object Search {
    val search = exec(http("Search Computer")
      .get("?f=Dell").check(status.is(200)))
  }

  val scn1: ScenarioBuilder = scenario("Get and Search Computers Test")
    .exec(Search.search, GetComputer.getComputer) // As per business scenario we can have multiple request (comma separated)

  after {
    println("Base is finished!")
  }
}