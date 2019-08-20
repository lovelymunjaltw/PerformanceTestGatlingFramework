import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class GetComputersSimulation extends Simulation {

  before {
    println("GetComputersSimulation is about to start!")
  }

  //val baseURL = System.getenv("BASE_URL")
  val baseURL: String = "http://computer-database.gatling.io"
  val numberOfUsers = (System.getenv("NUMBER_OF_USERS")).toInt
  val runDuration = (System.getenv("RUN_DURATION")).toInt
  val jsonContentType = "application/json"

//  val headers = Map(
//    "Accept" -> "application/json, text/plain, */*"
//     )

  val httpProtocol = http
    .baseUrl(baseURL)
    .inferHtmlResources()
    .contentTypeHeader(jsonContentType)
    .userAgentHeader("curl/7.61.0")
    //.headers(headers)


  val scn: ScenarioBuilder = scenario("Get Computes Test")
    .exec(http("GET Computes Test")
      .get(baseURL + "/computers")
      .check(status.is(200)))

    setUp(scn.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
    .protocols(httpProtocol))
    .assertions(global.successfulRequests.percent.gt(99))

  after {
    println("GetComputersSimulation is finished!")
  }
}