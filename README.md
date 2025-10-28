
# Bloggy Service

## 1. Overview

Bloggy is a robust backend service for a modern blogging platform. It provides a complete RESTful API for managing posts, categories, tags, and user authentication. The service is built with a focus on clean architecture, security, and scalability, making it a solid foundation for any content-driven application.

Key features include JWT-based authentication, CRUD operations for all core entities, and advanced querying capabilities such as filtering posts by category or tag.

## 2. Technologies & Frameworks

This service is built using a modern Java stack, leveraging the power and simplicity of the Spring ecosystem.

*   **Frameworks:**
    *   Spring Boot 3.5.6
    *   Spring Web
    *   Spring Data JPA
    *   Spring Security
*   **Language:**
    *   Java 21
*   **Database:**
    *   PostgreSQL (Production/Development)
    *   H2 (Testing)
*   **Libraries:**
    *   **Lombok:** Reduces boilerplate code for model and entity classes.
    *   **MapStruct:** Simplifies mapping between DTOs and JPA entities.
    *   **JSON Web Token (jjwt):** For implementing token-based authentication.
*   **Build Tool:**
    *   Maven

## 3. Service Architecture

The Bloggy service is designed as a monolithic application following a classic layered architecture pattern, which promotes separation of concerns and maintainability.

*   **Controllers (`/controllers`):** Handle incoming HTTP requests, validate input (DTOs), and delegate business logic to the service layer.
*   **Services (`/services`):** Contain the core business logic. They coordinate operations between repositories and perform tasks like data validation, transformation, and authorization checks.
*   **Repositories (`/repositories`):** Abstract the data access layer using Spring Data JPA. They manage all database interactions with JPA entities.
*   **Entities (`/domain/entities`):** JPA entities that map to the database tables (e.g., `User`, `Post`, `Category`).
*   **DTOs (`/domain/dtos`):** Data Transfer Objects used to shape the API's request and response payloads, decoupling the API from the internal database schema.
*   **Mappers (`/mappers`):** Using MapStruct, these interfaces automatically generate the boilerplate code for converting between DTOs and Entities.
*   **Security (`/security`):** Contains all security-related configurations, including the JWT filter, `UserDetailsService` implementation, and security configuration chain.

Authentication is handled via JWT. A client authenticates via the `/api/v1/auth/login` endpoint to receive a token, which must be included in the `Authorization` header for all subsequent requests to protected endpoints.

## 4. Running Locally

Follow these instructions to set up and run the service on your local machine.

### Prerequisites

*   Java 21
*   Apache Maven
*   PostgreSQL Server
*   An IDE like IntelliJ IDEA or VS Code (recommended)

### Database Setup

1.  Ensure you have a running PostgreSQL instance.
2.  Create a database. The default configuration expects a database named `postgres`.
3.  Update the database credentials in the `src/main/resources/application.properties` file if they differ from the defaults:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

### Configuration

The primary configuration file is `application.properties`. The most important value to be aware of is the JWT secret key.

```properties
# JWT Secret Key for signing tokens
jwt.secret=09da15750923e2c5ccdb980213d73fa740f6a921
```

**Note:** For production environments, it is strongly recommended to externalize these properties using environment variables or a configuration server instead of hardcoding them in the file.

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd bloggy
    ```

2.  **Build and run using Maven:**
    ```bash
    mvn spring-boot:run
    ```

The service will start on `http://localhost:8080`.

## 5. API Endpoints

The API is versioned under `/api/v1`.

### Authentication

| Method | Endpoint               | Description                                | Authentication |
| :----- | :--------------------- | :----------------------------------------- | :------------- |
| `POST` | `/api/v1/auth/login`   | Authenticates a user and returns a JWT.    | None           |

**Request Body (`/login`):**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

### Posts

| Method      | Endpoint               | Description                                | Authentication |
| :---------- | :--------------------- | :----------------------------------------- | :------------- |
| `GET`       | `/api/v1/posts`        | Get all published posts. Can filter by `categoryId` and/or `tagId`. | None           |
| `GET`       | `/api/v1/posts/drafts` | Get all draft posts for the authenticated user. | **Required**   |
| `POST`      | `/api/v1/posts`        | Create a new post.                         | **Required**   |
| `GET`       | `/api/v1/posts/{id}`   | Get a single post by its ID.               | None           |
| `PUT`       | `/api/v1/posts/{id}`   | Update an existing post.                   | **Required**   |
| `DELETE`    | `/api/v1/posts/{id}`   | Delete a post by its ID.                   | **Required**   |

### Categories

| Method   | Endpoint                  | Description                     | Authentication |
| :------- | :------------------------ | :------------------------------ | :------------- |
| `GET`    | `/api/v1/categories`      | Get all categories.             | None           |
| `POST`   | `/api/v1/categories`      | Create a new category.          | **Required**   |
| `DELETE` | `/api/v1/categories/{id}` | Delete a category by its ID.    | **Required**   |

### Tags

| Method   | Endpoint              | Description                               | Authentication |
| :------- | :-------------------- | :---------------------------------------- | :------------- |
| `GET`    | `/api/v1/tags`        | Get all tags.                             | None           |
| `POST`   | `/api/v1/tags`        | Create one or more new tags.              | **Required**   |
| `DELETE` | `/api/v1/tags/{id}`   | Delete a tag by its ID.                   | **Required**   |

## 6. Authentication Notes

*   To access protected endpoints, you must first obtain a JWT by calling the `/api/v1/auth/login` endpoint.
*   The token must be included in the `Authorization` header of your requests with the `Bearer` prefix.
*   Example Header: `Authorization: Bearer <your-jwt-token>`
*   The `JwtAuthenticationFilter` intercepts incoming requests, validates the token, and sets the user's security context, making the user's details (including their UUID) available for downstream processing.