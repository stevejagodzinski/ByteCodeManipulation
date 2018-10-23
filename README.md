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