# Task Manager — Spring Boot + React

A full-stack task management application built with **Spring Boot** and **React**.

I built this project as a hands-on exercise to refresh my full-stack development skills while learning and comparing the Spring Boot / React ecosystem with technologies I have previously worked with, including Ruby on Rails and Angular.

## Features

### Users
- Create users
- View users
- Update users
- Delete users
- Prevent duplicate email addresses
- Validation and structured error responses

### Tasks
- Create tasks
- View tasks
- Update tasks
- Delete tasks
- Assign tasks to users
- Task status using enums:
  - `TODO`
  - `IN_PROGRESS`
  - `COMPLETED`
- Task priority using enums:
  - `LOW`
  - `MEDIUM`
  - `HIGH`
- Due dates
- Request validation

## Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Jakarta Validation
- H2 Database
- Maven
- JUnit
- MockMvc

### Frontend

- React
- JavaScript
- Vite
- Fetch API
- CSS

## Architecture

The backend follows a layered architecture:

```text
HTTP Request
     ↓
Controller
     ↓
Service
     ↓
Repository
     ↓
JPA / Hibernate
     ↓
H2 Database
```

The React frontend communicates with the Spring Boot backend through REST APIs:

```text
React UI
   ↓
Fetch API
   ↓
Spring Boot REST API
   ↓
Service Layer
   ↓
Repository
   ↓
Database
```

## API Endpoints

### Users

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

### Tasks

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create task |
| PUT | `/api/tasks/{id}` | Update task |
| DELETE | `/api/tasks/{id}` | Delete task |

## Validation and Error Handling

The backend includes validation and centralized exception handling.

Examples include:

- Blank task titles return `400 Bad Request`
- Invalid enum values return `400 Bad Request`
- Missing resources return `404 Not Found`
- Duplicate user emails return `409 Conflict`
- Tasks must reference an existing user

A global exception handler provides consistent API error responses.

## Testing

The backend includes integration tests using **JUnit** and **MockMvc**.

The test suite covers scenarios including:

- Application context startup
- Creating users
- Duplicate user email handling
- Creating tasks
- Task validation
- Missing users
- Invalid enum values

Run the tests with:

```bash
cd backend
./mvnw test
```

Current test result:

```text
Tests run: 7
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Running the Application

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

The API runs on:

```text
http://localhost:8080
```

### Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

The React application runs on:

```text
http://localhost:5173
```

## Production Frontend Build

To verify the React production build:

```bash
cd frontend
npm run build
```

## Development Notes

The application currently uses an **H2 in-memory database**, which keeps local development simple but means data is reset when the backend application restarts.

CORS is configured to allow the local React development server to communicate with the Spring Boot API.

## Key Learning Areas

This exercise provided hands-on practice with:

- Spring Boot application structure
- Dependency injection
- REST controllers
- Service and repository layers
- Spring Data JPA and Hibernate
- Entity relationships
- Java enums with JPA
- Jakarta Bean Validation
- Jackson JSON serialization/deserialization
- HTTP status code semantics
- Centralized exception handling
- JUnit and MockMvc integration testing
- React state management with hooks
- Controlled forms
- REST API integration with `fetch`
- CORS
- Full CRUD workflows across frontend and backend
- Debugging integration issues across application layers

## Possible Next Steps

Some improvements I would consider for a production-oriented version include:

- PostgreSQL or another persistent database
- DTOs instead of exposing persistence entities directly
- Authentication and authorization
- More comprehensive frontend error handling
- Additional service and repository tests
- API documentation with OpenAPI/Swagger
- Environment-based configuration
- Docker/containerization

---

Built as a hands-on full-stack learning and skills-refresh exercise.