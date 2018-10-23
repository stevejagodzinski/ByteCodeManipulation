# ByteCodeManipulation

[![Build Status](https://travis-ci.org/stevejagodzinski/ByteCodeManipulation.svg?branch=master)](https://travis-ci.org/stevejagodzinski/ByteCodeManipulation)

## What is here?

###### Done:
* A Java Agent
  * Performs the following instrumentations when an HTTP request occurs:
    * Instruments the response to include a unique ID
* Tests
  * Unit Tests
  * Integration Tests
* This AMAZING README  

###### TODO:
* Computes the following data points:
  * Counts how many string objects were created for a single page request or RESTful request
  * Times the request from start to finish
  * Counts how many classes are loaded during one request
* Provides an interface that instruments the application to provide the computed data points to:
  * An endpoint
  * The console
  * A log file
  
## How to build
Building with maven from the root project directory will build the agent and run all tests.

```
mvn clean install
```

The build will output the agent jar here:
```
./bytecodemanipulationz-agent/target/bytecodemanipulationz-agent-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## How to deploy
Attach the agent jar to your webapp when launching the webapp.
Use the -javaagent JVM flag, passing the full path to the agent jar.

#### For Example:

A small spring boot app exists as part of the bytecodemanipulationz-test-integration project.

You can find it here:
```
./bytecodemanipulationz-test/bytecodemanipulationz-test-integration/target/bytecodemanipulationz-test-integration-2.0.5.RELEASE.jar
```

Normally you would launch this webapp using java -jar:
```
java -jar ./bytecodemanipulationz-test/bytecodemanipulationz-test-integration/target/bytecodemanipulationz-test-integration-2.0.5.RELEASE.jar
```

Here is how you would launch the spring boot webapp with the agent attached (replacing $REPLACE_WITH_FULL_PATH with the full path to the agent jar):
```
java -javaagent:$REPLACE_WITH_FULL_PATH/bytecodemanipulationz-agent-1.0-SNAPSHOT-jar-with-dependencies.jar -jar ./bytecodemanipulationz-test/bytecodemanipulationz-test-integration/target/bytecodemanipulationz-test-integration-2.0.5.RELEASE.jar
```
