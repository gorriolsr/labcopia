# Wild West Outlaws - Individual Project Block 1

## Overview
Welcome to the **Wild West Outlaws** project! In this assignment, you will develop a **Spring Boot REST API** that simulates a sheriff managing a list of wanted outlaws in the Wild West.

### Features
The API should allow users to:
- Add new outlaws to the wanted list.
- Update an outlaw’s status (*free, captured, or eliminated*).
- Retrieve details of a specific outlaw.
- Increase an outlaw’s bounty.
- Collect rewards for captured outlaws.
- Identify the most dangerous outlaw.
- Remove outlaws from the wanted list.
- Manage sheriffs and their statistics.

This project provides hands-on experience in **RESTful API development using Spring Boot**, focusing on **HTTP methods, request validation, exception handling, and API documentation**.

---
## API Endpoints

### 1. Register a New Outlaw *(1.5 points)*
**Method:** `POST`
**Endpoint:** `/outlaws`

#### Request Body:
```json
{
  "name": "Jesse James",
  "bounty": 5000,
  "status": "free"
}
```
#### Response:
```json
{
  "id": "6227fb68-7c90-4530-967c-f76457723ffb",
  "name": "Jesse James",
  "bounty": 5000,
  "status": "free",
  "createdAt": "2024-03-03T14:00:00Z"
}
```
#### Validation Rules:
- The `name` field is required.
- The `bounty` must be a positive number.
- The `status` must be one of: **"free", "captured", or "eliminated"**.

---

### 2. Retrieve Outlaw Details *(1 point)*
**Method:** `GET`
**Endpoint:** `/outlaws/{id}`

#### Response:
```json
{
  "id": "6227fb68-7c90-4530-967c-f76457723ffb",
  "name": "Jesse James",
  "bounty": 5000,
  "status": "free",
  "createdAt": "2024-03-03T14:00:00Z"
}
```

---

### 3. List All Outlaws *(1 point)*
**Method:** `GET`
**Endpoint:** `/outlaws`

Returns a list of all registered outlaws.

---

### 4. Update an Outlaw *(Full Update - 1.5 points)*
**Method:** `PUT`
**Endpoint:** `/outlaws/{id}`

#### Request Body:
```json
{
  "name": "Jesse James",
  "bounty": 10000,
  "status": "free"
}
```
> **Note:** *Status cannot be updated using PUT. All fields must be valid.*

---

### 5. Set Bounty *(Partial Update - 1.5 points)*
**Method:** `PATCH`
**Endpoint:** `/outlaws/{id}`

#### Request Body:
```json
{
  "bounty": 5000
}
```
#### Response:
Same as outlaw details response.

#### Rules:
- Throws `422 Unprocessable Entity` if the amount is negative.

---

### 6. Register a Sheriff *(2 points)*
**Method:** `POST`
**Endpoint:** `/sheriffs`

#### Request Body:
```json
{
  "name": "Wyatt Earp",
  "salary": 1000,
  "captures": 0,
  "eliminations": 0
}
```

#### Response:
```json
{
  "id": "6227fb68-7c90-4530-967c-f76457723ffb",
  "name": "Wyatt Earp",
  "salary": 1000,
  "captures": 0,
  "eliminations": 0,
  "createdAt": "2024-03-03T14:00:00Z"
}
```

---

### 7. Retrieve Sheriff Details *(1 point)*
**Method:** `GET`
**Endpoint:** `/sheriffs/{id}`

Fetches a sheriff’s details by ID.

---

### 8. List All Sheriffs *(1 point)*
**Method:** `GET`
**Endpoint:** `/sheriffs`

Returns a list of all registered sheriffs.

---

### 9. Update Sheriff Information *(1.5 points)*
**Method:** `PUT`
**Endpoint:** `/sheriffs/{id}`

#### Request Body:
```json
{
  "name": "Wyatt Earp",
  "salary": 1500
}
```
> **Note:** *All fields must be valid.*

---

### 10. Delete a Sheriff *(1 point)*
**Method:** `DELETE`
**Endpoint:** `/sheriffs/{id}`

Removes a sheriff from the system. If they have a capture or elimination record, mark them as deleted:
```json
{
  "id": "6227fb68-7c90-4530-967c-f76457723ffb",
  "name": "Wyatt Earp",
  "salary": 1000,
  "captures": 0,
  "eliminations": 0,
  "createdAt": "2024-03-03T14:00:00Z",
  "deleted": true
}
```

---

### 11. Change Outlaw’s Status *(1.5 points)*
**Method:** `POST`

#### Endpoints:
- `/sheriffs/{sheriffId}/capture/{outlawId}` → Changes status to **"captured"**.
- `/sheriffs/{sheriffId}/eliminate/{outlawId}` → Changes status to **"eliminated"**.

#### Rules:
- Captured outlaws add their bounty to the sheriff’s salary.
- Eliminated outlaws update sheriff statistics, but no bounty is awarded.
- Eliminated outlaws cannot have their status changed again.

---

### 12. Find the Most Wanted Outlaw *(1 point)*
**Method:** `GET`
**Endpoint:** `/outlaws/most-wanted`

Finds the outlaw with the highest bounty who is still free.

> If multiple outlaws have the same bounty, return the one wanted the longest (earliest creation date).

---

## Error Handling *(1.5 points)*
Your API should handle errors properly and return appropriate HTTP status codes:
- `200 OK` → Request proceeded successfully
- `201 CREATED` → Resource created
- `404 Not Found` → Outlaw or sheriff does not exist or has been removed.
- `422 Unprocessable Entity` → Invalid input data.
- `500 Internal Server Error` → Unexpected errors.

---

## Submission Guidelines
- Update the README including your name
- Submit the project via **GIT** and ensure the final code is in the **main branch**.

---


