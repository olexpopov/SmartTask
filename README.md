SmartTask

A highly secure, scalable RESTful API built with Spring Boot and PostgreSQL for modern team collaboration. SmartTask provides granular control over projects, tasks, and members, ensuring data integrity and authorization at every level.

Features

Robust Security

Implemented custom JWT (JSON Web Token) authentication and authorization checks.

Granular Access Control

Authorization is enforced at the service layer for all critical operations (e.g., only Project Owners can add members; only Owners/Assignees can modify tasks).

Advanced Error Handling

Implements a @ControllerAdvice global exception handler to map service-layer exceptions to professional HTTP status codes (401, 403, 404).

Unit Testing Focus

Core business logic is validated using JUnit 5 and Mockito to ensure stability without relying on a live database.

Database Management

Uses PostgreSQL via Spring Data JPA and Hibernate for persistent, relational data management.

Built With

Java 17

Spring Boot 3

Spring Security (JWT)

PostgreSQL

Spring Data JPA / Hibernate

JUnit 5 & Mockito

Setup and Installation

Follow the steps below to set up and run SmartTask on your local machine.

Prerequisites

Java 17 or higher

Maven 3.8 or higher

PostgreSQL (A running instance for the database)

1. Clone the Repository

git clone [https://github.com/olexpopov/SmartTask.git](https://github.com/olexpopov/SmartTask.git)
cd SmartTask


2. Configure PostgreSQL

Update src/main/resources/application.properties with your PostgreSQL credentials:

spring.datasource.url=jdbc:postgresql://localhost:5432/smarttaskdb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update


3. Build and Run

To compile the project and run it:

./mvnw clean install
./mvnw spring-boot:run


Authentication Flow

Register

Use POST /api/auth/register to create a new account.

Login

Use POST /api/auth/login to receive a JWT token.

API Access

Include the token in the Authorization header for all protected requests:
Authorization: Bearer <YOUR_JWT_TOKEN>

👨‍💻 Contributing

This project is maintained by [Your Name/Team]. Feedback and suggestions are welcome!
