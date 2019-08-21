import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class SearchComputerSimulation extends Simulation {

  before {
    println("SearchComputerSimulation is about to start!")
  }

  val baseURL: String = "http://computer-database.gatling.io"
  val numberOfUsers = (System.getenv("NUMBER_OF_USERS")).toInt
  val runDuration = (System.getenv("RUN_DURATION")).toInt

  val httpProtocol = http
    .baseUrl(baseURL)

  val feeder = csv("ComputerName.csv").circular

  val scn: ScenarioBuilder = scenario("Search Computer Test")
    .feed(feeder)
    .exec(http("Search Computer:" + "${Name}")
      .get("/computers?f=" + "${Name}")
      .check(status.is(200)))

    setUp(scn.inject(rampUsers(numberOfUsers) during (runDuration))
    .protocols(httpProtocol))
    .assertions(global.successfulRequests.percent.gt(99))

  after {
    println("SearchComputerSimulation is finished!")
  }
}