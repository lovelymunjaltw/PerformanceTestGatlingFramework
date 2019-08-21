import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class ChainExampleSimulation extends Simulation {

  before {
    println("ChainExampleSimulation is about to start!")
  }

  val baseURL: String = "http://api.jmeter.ninja/objects.json"
  val numberOfUsers = (System.getenv("NUMBER_OF_USERS")).toInt

  val httpProtocol = http
    .baseUrl(baseURL)

  val scn: ScenarioBuilder = scenario("Chain Test")
    .exec(http("Get all objects")
      .get("")
      .check(status.is(200))
      .check(jsonPath("$.objects[0]").saveAs("firstObject")))

    .exec(http("Get an object:" + "${firstObject}")
      .get("http://api.jmeter.ninja/objects/"+ "${firstObject}" + ".json")
      .check(status.is(200))
      .check(jsonPath("$.result").is("OK")) // this will pass
      //.check(jsonPath("$.result").is("OKK")) //this should fail
    )

    setUp(scn.inject(atOnceUsers(numberOfUsers))
    .protocols(httpProtocol))
    .assertions(global.successfulRequests.percent.gt(99))

  after {
    println("ChainExampleSimulation is finished!")
  }
}

// This type of Simulation is used in Performance test of a complete flow include multiple APIs.
// We can store and use variable in any further request (same as real life scenario)
// Can add multiple check (status code and/or response etc.)