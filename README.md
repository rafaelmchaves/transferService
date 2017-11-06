# TRANSFER SERVICE

Implementation of a transfer service.

# Springboot 
This is a spring boot project. Advantages:
- Embedded server
- Remove boiler plate code.
- Profiling

# Clean architeture

The organization of the project is in accordance with the Clean Architeture. More about it:
https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html

- Package usecases:

  Every class in this package use Command pattern with a execute method.
  
  The name of class represents an action.
  
  This package shows everything that this project doing.
  
  Don't can use tecnologies in this class, ex: Mongo, RabbitMQ etc. Only pure java libraries.
 

- Package gateway:

Classes in this package access database and others boards.

- Package domains:

Every domain tht represents a business is here.

# Technologies:

- Database: H2. It's easy to use for our purpose and it's transactional.
- Unit tests tools: JUnit and Mock MVC. Both are very good frameworks to unit tests.
- Integration tests: Spring integration. Easy, it's use Mockito and JUnit.
- Lombok: library that helps us to reduce code.
- Swagger: generate documentation about RestAPI of application.
- Rest: Spring framework Rest library

# Run application

This is a spring boot application. Just run!
https://spring.io/guides/gs/spring-boot/

# Swagger

All infomation about APIs
http://localhost:8080/swagger-ui.html#/account-controller

# H2

Console: http://localhost:8080/h2/

# Running integration tests

Just running tests. The application need to stopped to running the tests.

# Future - limitations

The AccountController.transferMoney is syncronized and it's can be a big problem, because others applications will be waiting response of our microservice. So, we need to remove syncronized of controller method and our controller should call a class that sends the transfer request message to a queue. That way, the response of mircroservice will be faster and the processing will occour in other moment. We need to create a listener class that listens this message and calls other use case resposible for the calculate this transfer request. This way, there will not problems with isolation and performance.

Create more integration tests scenarius.


