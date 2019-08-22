import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class MultipleScenarioSimulation extends Simulation {

  before {
    println("MultipleScenarioSimulation is about to start!")
  }

  val baseURL: String = "http://computer-database.gatling.io/computers"
  val baseURL2: String = "http://computer-database.gatling.io/computers/new"
  val numberOfUsers = (System.getenv("NUMBER_OF_USERS")).toInt
  val runDuration = (System.getenv("RUN_DURATION")).toInt

  val httpProtocol = http
    .baseUrl(baseURL)

  val httpProtocol2 = http
    .baseUrl(baseURL2)

  val feeder = csv("ComputerName.csv").circular
  val feederId = csv("Id.csv").random


  val scn1: ScenarioBuilder = scenario("Get Computers")
    .exec(http("Get Computers")
      .get("")
      .check(status.is(200)))

  val scn2: ScenarioBuilder = scenario("Get a Computer")
    .feed(feederId)
    .exec (http("Get Computer by id:" + "${Id}")
      .get("/" + "${Id}" )
      .check(status.is(200)))

  val scn3: ScenarioBuilder = scenario("Search Computer")
    .feed(feeder)
    .exec (http("Search Computer:" + "${Name}")
      .get("?f=" + "${Name}")
      .check(status.is(200)))

  val scn4: ScenarioBuilder = scenario("Goto next page")
    .feed(feederId)
    .exec (http("Goto page:" + "${Id}")
      .get("?p=" + "${Id}")
      .check(status.is(200)))

  val scn5: ScenarioBuilder = scenario("Click Add new Computer")
    .exec (http("Click Add new Computer")
      .get("")
      .check(status.is(200)))

    setUp(scn1.inject(atOnceUsers(numberOfUsers)) //For E2E scenario,we can use same or different httpProtocol for each scenario.
      .protocols(httpProtocol),
      scn2.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
        .protocols(httpProtocol),
      scn3.inject(rampUsers(numberOfUsers) during (runDuration))
        .protocols(httpProtocol),
      scn4.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
        .protocols(httpProtocol),
      scn5.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
        .protocols(httpProtocol2)) // here using different httpProtocol2
    .assertions(global.successfulRequests.percent.gt(99))

  after {
    println("MultipleScenarioSimulation is finished!")
  }
}

// This type of Simulation is used in Performance test include different APIs hit by different clients.
// Note: Scenario will not execute in sequence.
// We can have different ways to inject users (like atOnceUsers, rampUpUser etc.) for different scenario.