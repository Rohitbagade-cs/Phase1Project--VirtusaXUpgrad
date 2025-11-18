# 🏦 **Banking System Simulator – Spring Boot + MongoDB**

A full-featured **Banking Backend System** built using **Spring Boot (Monolith Architecture)** and **MongoDB**, supporting account management, transactions, and RESTful APIs with proper validation, exception handling, testing, and layered architecture.

---

## 📘 **Overview**

This project is an upgraded version of the Core Java Banking Simulator, transformed into a **Spring Boot Monolithic Application**.

It provides REST APIs to perform:

* Create account
* Fetch account details
* Deposit / Withdraw money
* Transfer funds
* View all transactions
* List all accounts

All data is persisted in **MongoDB**, with each account and transaction stored as separate documents.

---

## 🧱 **Tech Stack**

| Layer      | Technology                |
| ---------- | ------------------------- |
| Backend    | Spring Boot 3.x           |
| Database   | MongoDB                   |
| ORM        | Spring Data MongoDB       |
| Validation | Jakarta Bean Validation   |
| Build Tool | Maven                     |
| Tests      | JUnit 5, Mockito, MockMvc |
| Logging    | SLF4J + Logback           |

---

## 📁 **Project Structure**

```
src/
 ├── main/java/com/bankingsystem
 │    ├── controller/        # REST Controllers
 │    ├── service/           # Business Logic
 │    ├── repository/        # MongoDB Repositories
 │    ├── model/             # Document Models (Account, Transaction)
 │    ├── dto/               # Request DTOs
 │    ├── exception/         # Custom Exceptions + Global Handler
 │    └── config/            # (Optional) Config files
 │
 └── test/java/com/bankingsystem
      ├── service/           # Unit Tests with Mockito
      └── controller/        # MockMvc Tests
```

---

## 🗄️ **MongoDB Collections**

### **1. accounts**

Each document represents an account:

```json
{
  "_id": "674ff37dca123e48f9b2d5ab",
  "accountNumber": "ROH2871",
  "holderName": "Rohit",
  "balance": 12000.5,
  "status": "ACTIVE",
  "createdAt": "2025-11-07T09:30:00Z"
}
```

### **2. transactions**

```json
{
  "_id": "674ff3aeca123e48f9b2d5ac",
  "transactionId": "TXN-20251107-001",
  "type": "TRANSFER",
  "amount": 500,
  "timestamp": "2025-11-07T09:32:10Z",
  "status": "SUCCESS",
  "sourceAccount": "ROH2871",
  "destinationAccount": "ANN9810"
}
```

---

## 🔗 **API Endpoints**

### Account Operations

| Method | Endpoint                        | Description          |
| ------ | ------------------------------- | -------------------- |
| POST   | `/api/accounts`                 | Create a new account |
| GET    | `/api/accounts/{accountNumber}` | Get account details  |
| PUT    | `/api/accounts/{accountNumber}` | Update account       |
| DELETE | `/api/accounts/{accountNumber}` | Delete account       |
| GET    | `/api/accounts`                 | List all accounts    |

---

### Transaction Operations

| Method | Endpoint                                     | Description                |
| ------ | -------------------------------------------- | -------------------------- |
| PUT    | `/api/accounts/{accountNumber}/deposit`      | Deposit money              |
| PUT    | `/api/accounts/{accountNumber}/withdraw`     | Withdraw money             |
| POST   | `/api/accounts/transfer`                     | Transfer funds             |
| GET    | `/api/accounts/{accountNumber}/transactions` | Get account’s transactions |

---

## 🧪 **Testing**

This project includes:

### ✔ Unit tests

* Service layer using **Mockito**
* Repository layer using **@DataMongoTest**
* Exception tests

### ✔ Controller tests

* MockMvc tests
* JSON validation
* Status code checks

Test execution:

```
mvn test
```

### Generate coverage report:

```
mvn jacoco:report
```

Open:

```
target/site/jacoco/index.html
---

## ⚠️ **Exception Handling**

Project includes custom exceptions:

* `AccountNotFoundException`
* `InvalidAmountException`
* `InsufficientBalanceException`

Handled globally using:

```
@ControllerAdvice
public class GlobalExceptionHandler { ... }
```

---

## ▶️ **How to Run**

### 1. Start MongoDB

(Compass or local server)

```
mongodb://localhost:27017
```

### 2. Run application

```
mvn spring-boot:run
```

App will start at:

```
http://localhost:8080
```

---

## 🧪 **Test API using Postman**

### Example Create Account request:

```
POST /api/accounts
Content-Type: application/json
{
  "holderName": "Rohit"
}
```

### Example Deposit request:

```
PUT /api/accounts/RB1234/deposit
{
  "amount": 500
}
```

---

## 📦 **Build JAR**

```
mvn clean package
```

Run:

```
java -jar target/banking-system-0.0.1-SNAPSHOT.jar
```

---

## ⭐ **Features Implemented**

* REST APIs
* DTO validations
* Centralized exception handling
* MongoDB persistence
* Transaction history
* Logging
* Unit + Controller tests
* Clean layering (Controller → Service → Repository)

---
