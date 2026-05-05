# SplitMoney Backend

SplitMoney 2.0 is a backend service for managing group-based expenses. Users can create groups, track shared expenses, and automatically calculate who owes whom using a greedy settlement algorithm.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.2 |
| API | GraphQL (Spring for GraphQL) |
| Database | PostgreSQL |
| ORM | Hibernate JPA |
| Auth | JWT (JJWT 0.11.5) |
| Password Hashing | BCrypt |
| Build Tool | Gradle (Kotlin DSL) |
| JDK | Java 21 |

---

## Architecture

The application follows a layered monolithic architecture:

```
GraphQL Request
      │
  Resolver Layer        (AuthResolver, MutationResolver, QueryResolver, GroupFieldResolver)
      │
  Service Layer         (AuthService, GroupService, ExpenseService)
      │
  Repository Layer      (UserRepository, GroupRepository, ExpenseRepository)
      │
  PostgreSQL Database
```

JWT authentication is enforced through a `JwtAuthenticationFilter` that runs before every request and sets the Spring Security context.

---

## Project Structure

```
SplitMoney/
├── src/main/kotlin/com/splitmoney/
│   ├── SplitMoneyApplication.kt          # Entry point, sets timezone to Asia/Kolkata
│   ├── common/model/
│   │   └── BaseEntity.kt                 # Abstract base: UUID PK, createdAt, updatedAt
│   ├── config/
│   │   ├── SecurityConfig.kt             # Spring Security + BCrypt bean
│   │   └── JwtAuthenticationFilter.kt    # Extracts & validates Bearer token per request
│   ├── model/
│   │   ├── UserEntity.kt                 # users table
│   │   ├── GroupEntity.kt                # groups table + group_members join
│   │   ├── ExpenseEntity.kt              # expenses table
│   │   └── data/splitmoneyDataclasses.kt # DTOs: CreateGroupInput, CreateExpenseInput, Balance, Settlement, AuthPayload
│   ├── repository/
│   │   ├── UserRepository.kt
│   │   ├── GroupRepository.kt
│   │   └── ExpenseRepository.kt
│   ├── service/
│   │   ├── auth/
│   │   │   ├── AuthService.kt            # register / login
│   │   │   └── JwtTokenProvider.kt       # generate / validate JWT
│   │   ├── GroupService.kt               # createGroup, getGroupById, calculateBalance
│   │   ├── ExpenseService.kt             # createExpense
│   │   └── CustomUserDetailsService.kt   # Spring Security UserDetailsService
│   └── resolver/
│       ├── QueryResolver.kt
│       ├── MutationResolver.kt
│       ├── AuthResolver.kt
│       └── GroupFieldResolver.kt         # Resolves Group.members from memberIds
└── src/main/resources/
    ├── application.yml
    └── graphql/schema.graphqls
```

---

## Data Models

### UserEntity
| Field | Type | Notes |
|-------|------|-------|
| id | UUID | Auto-generated PK |
| name | String | Required |
| email | String | Required, unique |
| password | String? | Nullable (BCrypt hashed) |
| provider | String | Default: `"email"` |
| providerId | String? | For future OAuth support |
| createdAt / updatedAt | LocalDateTime | From BaseEntity |

### GroupEntity
| Field | Type | Notes |
|-------|------|-------|
| id | UUID | Auto-generated PK |
| name | String | |
| createdBy | UserEntity | ManyToOne |
| memberIds | MutableSet\<UUID\> | Stored in `group_members` table |
| expenses | List\<ExpenseEntity\> | OneToMany, cascade delete |

### ExpenseEntity
| Field | Type | Notes |
|-------|------|-------|
| id | UUID | Auto-generated PK |
| description | String | |
| amount | BigDecimal | Precision 19, scale 2 |
| payer | UserEntity | ManyToOne |
| group | GroupEntity | ManyToOne |

---

## GraphQL API

### Queries
```graphql
hello: String                          # Health check — returns "Hello SplitMoney!"
users: [User!]!                        # All users
user(id: ID!): User                    # Single user
groups: [Group!]!                      # All groups
group(id: ID!): Group                  # Single group
expenses(groupId: ID!): [Expense!]!   # Expenses in a group
```

### Mutations
```graphql
register(name: String!, email: String!, password: String!): AuthPayload!
login(email: String!, password: String!): AuthPayload!
createGroup(input: CreateGroupInput!): Group!
createExpense(input: CreateExpenseInput!): Expense!
```

### Input Types
```graphql
input CreateGroupInput {
  name: String!
  memberIds: [ID!]!
  createdBy: ID!
}

input CreateExpenseInput {
  description: String!
  amount: Float!
  payerId: ID!
  groupId: ID!
}
```

### Response Types
```graphql
type AuthPayload {
  token: String!
  user: User!
}
```

**Authentication:** Pass the JWT as a Bearer token in the `Authorization` header for protected operations.

---

## Balance Calculation

`GroupService.calculateBalance(groupId)` implements a greedy settlement algorithm:

1. Sum all expenses per member (how much each person paid).
2. Divide total group spend equally among all members to get each member's fair share.
3. Compute each member's **net balance** = amount paid − fair share.
    - Positive → this person is owed money (creditor).
    - Negative → this person owes money (debtor).
4. Sort creditors (descending) and debtors (ascending) and greedily match them to minimise the number of transactions.

> This endpoint exists in `GroupService` but is not yet exposed through the GraphQL schema.

---

## Local Setup

### Prerequisites
- JDK 21
- PostgreSQL running locally
- Gradle (or use the `./gradlew` wrapper)

### 1. Database
```sql
CREATE DATABASE splitmoney;
CREATE USER admin WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE splitmoney TO admin;
```

### 2. Configuration

Edit `src/main/resources/application.yml` if your database credentials differ:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/splitmoney
    username: admin
    password: password
  jpa:
    hibernate:
      ddl-auto: update   # auto-creates/updates schema on startup
```

> The JWT secret in `application.yml` is hard-coded and must be replaced with an environment variable before any production deployment.

### 3. Run
```bash
./gradlew bootRun
```

The GraphiQL IDE is available at `http://localhost:8080/graphiql` once the server is running.

---