# Submission of the Back-end Tech Challenge by Juan Campos


This is my implementation for the camp site reservation system.

## Functional Assumptions
- Only 1 reservation per customer
- A Customer may make a reservation only if he does not have a current reservation within the next 30 days.
- The campsite is available for reservation right on the day of departure. So, for example, if a customer makes a reservation with arrival day of July 1st and departure date of July 2nd, another customer can have an arrival date of July 2nd.
- A customer can update the reservation values to any other valid values including dates. 

## Technical Assumptions
- All services are REST endpoints.
- The request/response mechanism is in JSON
- The arrival date has a unique constraint in the db. This way it leaves the transactional boundary to save a reservation to the db, the source of truth. The departure date is left alone to leave the transactional boundary to only one value.
- The application can run as just a springboot application from the command line or a spring boot application + postgres database in a docker compose deployment. The springboot containers can be spun into multiple containers. The db however is left in a single container to ensure consistency of data.
## Out of Scope
- SSL.  
- Security and Authentication 

### Prerequisites for build and running application

```
Java 8,  Maven 3.6 and a REST client (examples are shown using POSTMAN).
```
### Optional to view code
```
Intellij
```

## Application running on cloud

The application is build and deployed on a CI pipeline using github actions , running on a docker compose unit of springboot+postgres .
The application REST API docs that detail the REST endpoints can be accessed here : [Cloud Reservation System](http://66.42.67.233:8090/swagger-ui.html)
For more details on the CI pipeline see [below](README.md#continuous-integration-build).

### (Optional) Installing project on IntelliJ from the provided zip file

* Unzip the provided camp-demo.zip file into a clean folder.
* From IntelliJ , choose File -> New -> Project From Existing Sources
* Open the file "pom.xml"
* On the pop up screen, make sure the option to "Import Maven Projects automatically" is enabled
* Click "Next" and use the default options on all following screens

## Building the Application

* The application is build using Maven. 
  * If Maven is enabled from the command line, navigate to
the root folder of the application.
  * OR Navigate through IntelliJ to the Maven tab click on the button "Execute Maven Goal".
* Execute the following command through either method
```
mvn clean package
```

* The maven goal will  and build the jar file
```
camp-demo-0.0.1-SNAPSHOT.jar
```

## Running the Application Locally
* To run the application, type in from a command line window in the root folder 
the following command (The '/' character on the path may need to be changed):  
```
java -jar  target/camp-demo-0.0.1-SNAPSHOT.jar
```
This will fire up the application with the default profile. This default profile is set up to persist the information in an H2 *LOCAL FILE* database. This means that if the application is stopped the data will be available on restart from the file. 

The application is configured to use H2 in file database, therefore a new db file will be created when the application runs first and therefore the information (booking information) will remain persisted even if the application is brought down.

The application will then create two H2 files, testdb.mv.db testdb.trace.db in the root folder for the user. These can be removed manually later.

To access the application, type in a local browser `localhost:8080`. 
The port for when the application runs locally in default mode is `8080`.

The browser will display the API documentation. The user can navigate to the `BookingController` link to view the REST API for the application.  

<img width="1480" alt="Screen Shot 2020-08-02 at 12 02 10 PM" src="https://user-images.githubusercontent.com/876282/89129129-061f3180-d4b8-11ea-85b4-495479ba9ef1.png">


The user can then view the examples to construct REST operation in a REST client such as postman.


### Running with test profile 

*NOTE* The test argument MUST be added so the application can run in memory *INCLUDING* the database. Otherwise the default profile is used which will create a local file for the db.

To run in test mode:
```
java -jar -Dspring.profiles.active=test  target/camp-demo-0.0.1-SNAPSHOT.jar
```


* The springboot application will fire up, the command line will show a log with the text
```
"Started CampDemoApplication in *.** seconds"
```

The test mode is also used to run the unit tests locally. 

To see data persisted, the application can run with the default profile or if docker is installed in the host machine using the docker build and profile.

### Docker build and run

 To run the application with Docker compose please see [here](README.md#docker-build)
 
 
### How to use the application and Endpoints
All API documentation along with examples with how to use it is here->  [REST API DOCS](CAMPDEMO.md)

## RESTAPI docs
  The URLS provided are assumed to come from the root of the server application, for demo purposes [http://localhost:8080](http://localhost:8080)
  The API documentation can also be seen via swagger 2. If the application is running locally, it can be accessed via [Swagger 2](http://localhost:8080/swagger-ui.html)
  


# Design
 
 ## Libraries used:
 
 The application is implemented using:
 - Spring Boot for server side implementation
 - The server was changed from Tomcat to undertow. Undertow has a better performance due to using a higher number of threads during execution (13 as opposed to 5 according to some documentation)
 - H2 in memory database for the springboot profile "test"
 - Postgres database when using docker compose
 - Resource bundles for internationalization.
 - [Spock](http://spockframework.org/) library for unit testing.
 
 The spring boot application uses the following components for implementation:
 
 - Spring Controllers.
    - Booking Controller. Handles all the camp demo related endpoints.
    - Camp Exception Handler Controller. The common handler for any exception that is directly targetted in the application.
 - Spring JPA 
    - Entities: 
        - Booking
        - Repositories to handle data operations
        - Constraint classes for handle field validation
        - DTO classes for parameter mapping.
 - Spring Services
    - Reservation Service. To handle all the service operations for bookings between the controller and the domain layer.
    - Date Service. Handles service calculations and collections to be used by the reservation service.
 - Spring AOP
    - An aspect `CampLoggingAspect` is used in tandem with logging to log every entry and exit from methods, as well as any exception thrown.
 - Spring Localization
    - Resource bundles are used to switch from english to french via resource bundles. 
    
  ## DB model
  The following are the table used for persistence in the application:
  
  #### BOOKING table
  
  | Column       | Description                                             |
  |--------------|---------------------------------------------------------|
  | id           | primary key                                             |
  | first_name   | The first name for the guest       |
  | last_name    | The last name for the guest                    |
  | email        | Email for the guest |
  | arrival_dt   | The arrival date for the reservation                      |
  | departure_dt | The departure date for the reservation     |
  | status       | The status of the reservation                      |
  
  The table is build via liquibase, which is located in the file
    
  ```src/main/resources/db/changelog/changelog-master.xml```
  

   
   # Unit Testing
   The code is unit tested via [Spock](http://www.spockframework.org) library. This library uses groovy language and has a BDD (Behaviour-Driven Development) style. The tests are under `src/groovy` folder and can be executed by IntelliJ. 
     
   ## Running the tests
   The tests are executed as part of the Maven build 
   OR (for IntelliJ)
   
   Open the Project sidebar and navigate to the src/test/groovy folder
   Right click on the folder and choose the option:
   Run "All Tests"
   The test suite will run until about 135 tests have been executed.
   
   ## Continuous Integration Build
   
   A continuous integration build was set up using Github actions. 
   The build history can be viewed at:
   
   [Camp Demo CI Build History](https://github.com/jpcampos/camp-demo/actions?query=workflow%3A%22Java+CI+with+Maven%22)
     
     
   The CI build consists of:
   - A java maven build
   - Triggers to be executed on:
       - Merge to master
       - A pull request from a branch from master
       - A scheduled nightly build corresponding to 07:40 AM UTC time (around 7:40 AM Calgary time)
       - An upload of the jar file `camp-demo-0.0.1-SNAPSHOT.jar`
       - A docker build and push to the [Public Registry](https://hub.docker.com/r/jpcampos/bootprojects/tags)
   
   The source file for the CI build is located in the source project at:
   
   [Camp Demo Build yml file](https://github.com/jpcampos/camp-demo/blob/master/.github/workflows/maven.yml)
   
   The application is then deployed to a [vultr cloud server](http://66.42.67.233:8090/) via a chron job that performs a teardown, docker pull of latest build on registry and rebuild the application. *NOTE* The application will retain the persisted reservations in the system.
   
   ## Docker build
   
   The application has an option to be build as a docker container.
   To create a docker image `camp-demo` , simply run the following command:  
   
   ``` mvn clean install```  
   
   This will create the local docker image `camp-demo' . There is also a docker compose file called  
   
   ```docker-compose.yml```
   
   After running the install command, you can run the following command from the same location the `docker-compose.yml` file is located.
   
   ```docker-compose up```
   
   This will execute a docker image composition, there are three services:
     
   ```campdb-docker``` A postgres database. The database can be accessed on `localhost:5433/campdemodb` with username: `postgres` and  password `admin`
   
   ```campdemoapp``` The spring boot application. It uses the postgres database for persistence.
   
   ```nginx``` Load balancer using round robbin in case the user decides to scale the multiple spring boot app containers horizontally.
   
   The image will use the application-docker.yml file in the source code which is pointing to a different port, on 8090:
   
   ```localhost:8090/campdemo/booking```
   
   The containers can be stopped using ```docker-compose stop```
   
   ### Scaling spring boot campdemoapp service horizontally
   
   The application can also run using multiple containers for the spring boot app. The persistence system however will still be a bottle neck since the spring boot apps need to rely on it for source of data. One possible improvement could to add a in memory cache for the list of available dates however the implementation of such a coordinated cache in a cluster of spring boot applications is out of scope for this version of the application. The scaling of spring boot apps is just left then as a capability demonstration.
   
   To spin up more then one container the spring boot applications must be stopped with:
   
   ```docker-compose stop```
   
   To spin 3 docker containers use the following instruction:
   
   ```docker-compose up --scale campdemoapp=3 -d```
   
   The screen should then show the following output for the spring boot containers
   
   ```
   Starting camp-demo_campdemoapp_1 ... done
   Creating camp-demo_campdemoapp_2 ... done
   Creating camp-demo_campdemoapp_3 ... done
   ```

  These are runnning in detached mode. Type the following in the command line to see the logs:
  
  ```docker-compose logs campdemoapp -f```
  
  This will tail the logs of the application, the screen should print out the `Started CampDemoApplication` for the 3 containers mentioned above.
  
  *NOTE* The cloud application at `66.42.67.233:8090` is running 3 containers at the moment. 
   
   ### Performance Testing
   
   The following section does some performance testing using this container as opposed to the in memory database 
   
   Performance testing:
   
   The following is a screenshot of the application running on the remote cloud server.
   The simulation is to run a single reservation creation, followed by 5,000 GET requests for open dates and 5,000 updates to the reservation created previously.
   The total throughput is 42.7/sec for the GET request and 42.6/sec for the PUT request.  Details on the table below with a test executed via JMETER.
   
   <img width="1234" alt="Screen Shot 2020-07-26 at 11 49 44 AM" src="https://user-images.githubusercontent.com/876282/88485828-46216a00-cf36-11ea-9f0e-6f237c7c041b.png">

   
   ## Built With
   
   * [SpringBoot](https://spring.io/projects/spring-boot) - Used as the MVC framework to build the application, chosen for the speed of development and minimal configuration. The default built in capabilities for logging and localization were used.
   * [Maven](https://maven.apache.org/) - Dependency Management
   * [Spock](http://spockframework.org/) - A testing framework based on Groovy, choosing for the speed of unit and integration testing development as well as ease of adaption of TDD principles.
   * [Liquibase](https://www.liquibase.org/) - Track,version and deploy db changes
   * [Log4J2](https://logging.apache.org/log4j/2.x/) Apache log4j2 for logging capabilities.
   * [Postgres](https://www.postgresql.org/) - Open Source db.
   * [Docker](http://www.docker.org) - Managing containers
   
   ## Authors
   
   *** Juan Campos *** - *Initial work* - 
    
   ## Resources
   
   * Various web pages and resources to assist with liquibase, maven build and docker deploy.
   
