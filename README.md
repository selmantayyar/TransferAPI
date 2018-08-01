# Ingenico Transfer API Assignment

Spring Boot Application serving as a REST interface to create accounts and transfer money between them. Below is the 
technology stack used:

*  Persistence:H2 in memory database
*  ORM:Hibernate
*  JSON parsing/rendering:Jackson
*  REST Support:Spring REST 

Spring Boot includes most of these libraries bundled and it is optimal choice to develop Rest API solutions.It is 
based on Spring framework,so easy to adapt. Moreover,it  is scalable as self-running units.Therefore I have chosen it 
for this project.

Application in general offers 2 endpoints for creating account and transfering money among them.I have also added one
endpoint to check balance of a current account.

## Prerequisites
* Maven
* Java 8


## Installation

`mvn clean install`

To run the application,just execute `java -jar IngenicoTransferService.jar` and application will be started on 
localhost port 8080.

## Testing
Added postman test scripts which simply provide invocation of 3 URL's.

[Click here for Postman Tests](https://gist.github.com/selmantayyar/b6c8b068fe6919478dd251f986f9be49)

In any case,there are 3 URL's.

* http://localhost:8080/ingenico/accounts to create accounts.POST request.
* http://localhost:8080/ingenico/transfer to transfer money. POST request.
* http://localhost:8080/ingenico/accounts/{id} to check an existing account. GET request.

Also TransferControllerTest class includes enough testing,including sending concurrent transfer requests to the API.

## Limitations
* To facilictate development and testing,application keeps data in-memory database.This should be of course changed.
* Currently no security implemented due to time constraints.Token-based security should be implemented.One endpoint 
to log in and to get the sort-lived token,another endpoint for refreshing the token should be created.Users along with
their token should be kept in a proper database.
