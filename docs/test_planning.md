# Testing Planning

## 1. Risk Analysis & Prioritisation

Given the constrained timeline and safety-critical nature of drone operations, testing was prioritized based on risk severity and business impact.

### 1.1 Priority Matrix

| Priority | Component | Justification (Business & Safety) | Test Strategy |
| :--- | :--- | :--- | :--- |
| **P1 (Critical)** | **Pathfinding (Safety)** | **Regulatory Compliance:** Drones entering No-Fly Zones can cause physical harm and lead to severe legal penalties. | **Partition Testing:** Exhaustive Unit Testing of geometry logic; Boundary Value Analysis for zone edges. |
| **P1 (Critical)** | **Order Validation (Revenue)** | **Revenue Protection:** Processing invalid orders results in financial loss. Basic integrity checks catch the majority of user errors. | **Specification-Based:** Strict **Syntactic Validation** of Credit Card (Length: 16, Numeric check) and Pizza count constraints (1-4). |
| **P2 (High)** | **API Contract** | **System Integration:** The frontend/mobile app relies on a stable API contract. Breaking changes causes service outages. | **Integration Testing:** Using `MockMvc` to verify HTTP 400/200 responses against the JSON schema. |
| **P3 (Medium)** | **Performance** | **User Experience:** The 60s calculation limit is important for service throughput but secondary to safety. | **Non-Functional:** Validated via JUnit `@Timeout` rules to prevent infinite loops. |

## 2. Instrumentation & Scaffolding Strategy

To facilitate white-box testing and debugging without a full GUI, the following mechanisms were implemented.

### 2.1 Instrumentation (Observability)
* **Logging (SLF4J):** Implemented targeted `INFO` and `WARN` logging in `OrderService` (see `src/main/.../OrderService.java`).
    * *Purpose:* To trace specific validation failure reasons (e.g., `CVV_INVALID` vs `CARD_NUMBER_INVALID`) during test execution. This allows distinguishing between "correct rejection" and "unexpected code crash".

### 2.2 Scaffolding (Isolation)
* **Mocks (Mockito):** Created mock objects for `RestTemplate`.
    * *Purpose:* To isolate the System Under Test (SUT) from the unstable external ILP API and simulate "dirty data" scenarios (e.g., closed restaurants) deterministically.
* **Test Drivers (MockMvc):** Used `MockMvc` as a headless driver.
    * *Purpose:* To simulate HTTP requests (POST/GET) and assert response bodies without the overhead of deploying a full Tomcat server.

## 3. Test Process & Lifecycle

### 3.1 Methodology
Unlike a traditional waterfall approach where testing occurs only after implementation, this project adopted an **Iterative / Agile** lifecycle.
* **TDD Influence:** Core validation logic (e.g., `CreditCardValidator`) was tested in isolation using Unit Tests before full system integration.

### 3.2 Automated Regression (CI/CD)
To mitigate the risk of "works on my machine" and regression bugs:
* **Pipeline:** A GitHub Actions workflow (`.github/workflows/ci.yml`) triggers on every `push`.
* **Policy:** No code is merged to `main` unless all Unit and Integration tests pass (Green Build).