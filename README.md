# Card Management System

## Overview

A Spring Boot REST API for managing banking entities: Accounts, Cards, and Transactions.
Supports two currencies (USD, LBP) and enforces business rules such as card-account binding and balance checks.

## Features

* CRUD operations for accounts, cards, and transactions.
* Process transactions with currency validation (assuming that a card is associated to a max of two accounts, one in each currency, as per the usual in Lebanon.)
* Prevent overdrafts based on available balance (for debit transactions.)
* Exception handling for missing resources and invalid input.

## Tech Stack
* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL (or other JPA-compatible DB)
* Maven

## Prerequisites
* Java 17
* Maven 3.8+
* PostgreSQL installed and running,

## Configuration

Update ```src/main/resources/application.properties```
```
spring.datasource.url=jdbc:postgresql://localhost:[port number]/[your database name]
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

## API Endpoints
### Accounts
* ```POST /accounts/new``` create a new account
* ```GET /accounts/all``` retrieve all saved accounts
* ```GET /accounts/{id}``` retrieve account details by ID
* ```PUT /accounts/{id}``` updates an account's fields.
### Cards
* ```POST /cards/new``` creates a new card
* ```GET /cards/all``` retrieves all saved cards
* ```GET /cards/{id}``` retrieves card details by ID
* ```PUT /cards/{id}``` updates a card's fields.
### Transactions
* ```POST /transactions/new``` creates a new transaction
* ```GET /transactions/all``` retrieves all saved transactions
* ```GET /transactions/{id}``` retrieves transaction details by ID
* ```PUT /transactions/{id}``` updates a transaction's fields.

### Example Request
```
POST /accounts/new
{
  "balance": 1000.00,
  "currency": "USD"
}

```

For more details on formatting, refer to the DTO fields in the classes in ```com.example.cms.dto```
