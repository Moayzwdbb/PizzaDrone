# PizzaDrone

PizzaDrone is a Spring Boot service for the ILP-style drone pizza delivery scenario. It provides endpoints for order validation and delivery path calculation (including GeoJSON output), and includes a test suite covering controller and service behaviours.

## Tech Stack

- Java 21 (recommended)
- Maven (via Maven Wrapper `./mvnw`)
- Spring Boot
- JUnit 5 + Spring Boot Test

## Project Structure

- `src/main/java/com/ilp/pizzadrone/`
  - `controller/` REST controllers (API endpoints)
  - `service/` business logic (e.g., order validation, path calculation)
  - `validation/` validators for order/pizza/restaurant/credit card rules
  - `util/` helper utilities
  - `dto/`, `model/`, `constant/` data structures and constants
- `src/test/java/com/ilp/pizzadrone/`
  - controller tests and service tests
