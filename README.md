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

## Running the Application
* To run the application, type in from a command line window in the root folder 
the following command (The '/' character on the path may need to be changed):  
*NOTE* The test argument MUST be added so the application can run in memory. Otherwise the default profile is set to use a postgres db which is set up in a docker container. To run the application with Docker compose please see <HERE>HERE
```
java -jar -Dspring.profiles.active=test  target/camp-demo-0.0.1-SNAPSHOT.jar
```


* The springboot application will fire up, the command line will show a log with the text
```
"Started CampDemoApplication in *.** seconds"
```
The application is configured to use H2 in file database, therefore a new db file will be created when the application runs first and therefore the information (booking information)

  ## RESTAPI docs
   The URLS provided are assumed to come from the root of the server application, for demo purposes [http://localhost:8080](http://localhost:8080)
   The API documentation can also be seen via swagger 2. If the application is running locally, it can be accessed via [Swagger 2](http://localhost:8080/swagger-ui.html)
   
   ### How to use the application and Endpoints
   All API documentation along with examples with how to use it is here->  [REST API DOCS](CAMPDEMO.md)

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
 -Spring Localization
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
   The code is unit tested via [Spock](http://www.spockframework.org) library. This library uses groovy language and has a BDD (Behaviour-Driven Development) style. The tests are under `src/groovy` folder and can be executed by IntelliJ. Integrations tests are out of scope for now but can be added if for example the broker is moved to Rabbit MQ or the DB is switched over to a non-memory db, deployed in Docker for example.
     
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
   
   The source file for the CI build is located in the source project at:
   
   [Camp Demo Build yml file](https://github.com/jpcampos/camp-demo/blob/master/.github/workflows/maven.yml)
   
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
   
