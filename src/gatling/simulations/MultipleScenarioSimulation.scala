import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class MultipleScenarioSimulation extends Base {

  before {
    println("MultipleScenarioSimulation is about to start!")
  }


  val scn2: ScenarioBuilder = scenario("Get a computer")
    .feed(feederId)
    .exec (http("Get computer by id:" + "${Id}")
      .get("/" + "${Id}" )
      .check(status.is(200)))

  val scn3: ScenarioBuilder = scenario("Goto next page Test")
    .feed(feederId)
    .exec (http("Goto page:" + "${Id}")
      .get("?p=" + "${Id}")
      .check(status.is(200)))

  val scn4: ScenarioBuilder = scenario("Click add new computer Test")
    .exec (http("Click add new computer")
      .get("")
      .check(status.is(200)))

    setUp(scn1.inject(atOnceUsers(numberOfUsers)) //For E2E scenario,we can use same or different httpProtocol for each scenario
      .protocols(httpProtocol),
      scn2.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
        .protocols(httpProtocol),
      scn3.inject(rampUsers(numberOfUsers) during (runDuration))
        .protocols(httpProtocol),
      scn4.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
        .protocols(httpProtocol2))                // here using different httpProtocol2
    .assertions(global.successfulRequests.percent.gt(99))

  after {
    println("MultipleScenarioSimulation is finished!")
  }
}

// This type of Simulation is used in Performance test include different APIs hit by different clients since
// Scenario will not execute in sequence.
// We can have different ways to inject users (like atOnceUsers, rampUpUser etc.) for different scenario.