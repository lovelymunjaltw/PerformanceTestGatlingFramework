import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class CreateComputerSimulation extends Simulation {

  before {
    println("CreateComputerSimulation is about to start!")
  }

  val baseURL: String = "http://computer-database.gatling.io"
  val numberOfUsers = (System.getenv("NUMBER_OF_USERS")).toInt
  val runDuration = (System.getenv("RUN_DURATION")).toInt
  val jsonContentType = "application/json"

  val httpProtocol = http
    .baseUrl(baseURL)
    .inferHtmlResources() //Gatling will automatically parse HTML to find embedded resources and load them
    .contentTypeHeader(jsonContentType)

  val payLoad : String = "{\"name\":\"ABC\",\"introduced\":\"2019-09-08\",\"discontinued\":\"2019-09-09\", \"company\":\"3\"}"

  val scn: ScenarioBuilder = scenario("Create Computer Test")
      .exec(http("Create a computer")
      .post("/computers")
      .body(StringBody(payLoad))
      .check(status.is(200)))

    setUp(scn.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
    .protocols(httpProtocol))
    .assertions(global.successfulRequests.percent.gt(99))

  after {
    println("CreateComputerSimulation is finished!")
  }
}