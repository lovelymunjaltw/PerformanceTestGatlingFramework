# git repo: https://github.com/lovelymunjaltw/PerformanceTestGatlingFramework.git

# Run all gatling tests in src/gatling/simulations: 
NUMBER_OF_USERS=<any number> RUN_DURATION=<any number> ./gradlew gatlingRun

Note: 
1. Run above command in Project root directory (PerformanceTestGatling). 
2. NUMBER_OF_USERS and RUN_DURATION are Env variables defined in simulation scala script.
3. src/gatling/simulations is the default location set by plugin to compile and run scala scripts.
4. gatlingRun is the gradle task provided by this plugin only.
5. Scala file naming convention should be: "*Simulation*.scala". We can change the configuration. 

# Run a specific gatling test:
NUMBER_OF_USERS=<any number> RUN_DURATION=<any number> ./gradlew gatlingRun-SimulationName

Example: NUMBER_OF_USERS=5 RUN_DURATION=10 ./gradlew gatlingRun-GetComputersSimulation