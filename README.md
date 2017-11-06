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
 -- Every class in this package use Command pattern with a execute method.
 -- The name of class represents an action.
 -- This package shows everything that this project doing.
 -- Don't can use tecnologies in this class, ex: Mongo, RabbitMQ etc. Only pure java libraries.

- Package gateway
Classes in this package access database and others boards.

- Package domains:
Every domain tht represents a business is here.

# Technologies:

- Database: H2. It's easy to use for our purpose and it's transactional.
- Unit tests tools: JUnit and Mock MVC. Both are very good frameworks to unit tests.
- Integration tests: Spring integration. Easy, use Mockito and JUnit.
- Lombok: library that helps us to reduce code.
- Swagger: Documentation about RestAPI. 

# Run application

This is a spring boot application. Just run!
https://spring.io/guides/gs/spring-boot/

# Swagger

Every infomations about APIs
http://localhost:8080/swagger-ui.html#/account-controller

# Future - limitations

Include queues using AMQP.

Remove syncronized of controller method.

Controller should call a class that sends a message to a queue. Some listener class listens this message and calls other use case resposible for the calculate. This way, there will not problems with isolation and performance.


