# 🚀 SmartTask

**SmartTask** is a highly secure, scalable **RESTful API** built with **Spring Boot** and **PostgreSQL**, designed for modern team collaboration. It provides fine‑grained control over **projects**, **tasks**, and **members**, while ensuring **data integrity**, **security**, and **authorization** at every level.

---

Key Features

 Robust Security

* Custom **JWT (JSON Web Token)** authentication
* Secure request filtering with Spring Security
* Stateless, token‑based authorization

 Granular Access Control

 Authorization enforced at the **service layer**
 Examples:

  * Only **Project Owners** can add or remove members
  * Only **Owners or Assignees** can modify tasks

### ⚠️ Advanced Error Handling

* Global exception handling using `@ControllerAdvice`
* Clean, professional HTTP responses:

  * `401 Unauthorized`
  * `403 Forbidden`
  * `404 Not Found`

### 🗄️ Database Management

* **PostgreSQL** for reliable relational data storage
* **Spring Data JPA** & **Hibernate** for ORM and persistence

---

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot 3.3.4**
* **Spring Security (JWT)**
* **PostgreSQL**
* **Spring Data JPA / Hibernate**

---

## ⚙️ Setup & Installation

Follow the steps below to run **SmartTask** locally.

### ✅ Prerequisites

* Java **17+**
* Maven **3.8+**
* PostgreSQL (running instance)

---

### 📥 1. Clone the Repository

```bash
git clone https://github.com/olexpopov/SmartTask.git
cd SmartTask
```

---

### 🗄️ 2. Configure PostgreSQL

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smarttaskdb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

3. Build and Run

Compile and start the application using Maven:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The API will start on the default port: **[http://localhost:8080](http://localhost:8080)**


---

Maintainer

Maintained by Olex Popov.


