import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}

class GetComputersSimulation extends Simulation {

  val baseURL: String = "http://computer-database.gatling.io"
  val numberOfUsers = (System.getenv("NUMBER_OF_USERS")).toInt
  val runDuration = (System.getenv("RUN_DURATION")).toInt

//  We can pass header like below in any request:
//  val headers = Map(
//    "Accept" -> "application/json, text/plain, */*",
//    "Accept-Encoding" -> "gzip, deflate, br"
//     )

  val httpProtocol = http
    .baseUrl(baseURL)
    //.userAgentHeader("curl/7.61.0") //For this test, headers are not required
    //.headers(headers)

  //The User-Agent request header contains a characteristic string that allows the network protocol peers to identify the application
  // type, operating system, software vendor or software version of the requesting software user agent

  val scn: ScenarioBuilder = scenario("Get Computers Test")
    .repeat(3){
      exec (http("GET Computers")
      .get("/computers")
      .check(status.is(200)))
      .pause(10) // after each iteration wait for 10 sec
  }

    setUp(scn.inject(constantUsersPerSec(numberOfUsers) during (runDuration))
    .protocols(httpProtocol))
    .assertions(global.successfulRequests.percent.gt(99))
}