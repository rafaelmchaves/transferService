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
  
  Can't use tecnologies in this class, ex: Mongo, RabbitMQ etc. Only pure java libraries.

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
- RabbitMQ:  is an amazing open source message broker software that implements the Advanced Message Queuing Protocol (AMQP).
- Docker: Docker is an open platform for developers and sysadmins to build, ship, and run distributed applications, whether on laptops, data center VMs, or the cloud.

# RabbitMQ and Docker
In project, there is a rabbitMQ in docker file.

Just run the following command in docker field:
sudo docker-compose up

user rabbit: guest

password rabbit:quest

# Run application

Run up RabbitMQ.

This is a spring boot application. Just run up it!
https://spring.io/guides/gs/spring-boot/

# Swagger

All infomations about APIs
http://localhost:8080/swagger-ui.html#/account-controller

# H2

Console: http://localhost:8080/h2/

# Running integration tests

Just running tests. The application need to stopped to running the tests.
RabbitMQ need to be running while tests are running.

One of tests show us that the application is isolated.

# Future - limitations

- Create more integration tests scenarius.
- There are not profiles in this project. In the future, we can create profiles to production, to integration tests etc.



