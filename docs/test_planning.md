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

## 2. Instrumentation & Scaffolding

To facilitate white-box testing and debugging, the following mechanisms were implemented.

### 2.1 Instrumentation
* **Logging (SLF4J):** Implemented targeted `INFO` and `WARN` logging in `OrderService` (see `src/main/.../OrderService.java`) to trace specific validation failure reasons (e.g., `CVV_INVALID` vs `CARD_NUMBER_INVALID`) during testing. This allows distinguishing between different failure reasons.

### 2.2 Scaffolding
* **Mocks (Mockito):** Created mock objects for `RestTemplate` to isolate the system under test from the unstable external ILP API and simulate "dirty data" scenarios (e.g., closed restaurants).
* **Test Drivers (MockMvc):** Used `MockMvc` to simulate HTTP requests (POST/GET) and assert response bodies.

## 3. Test Process & Lifecycle

### 3.1 Lifecycle Selection: XP & DevOps

**Development Phase (XP):** I adopted the **Extreme Programming (XP)** "Test-First" workflow. Core validation logic (e.g., `CreditCardValidator`) was written with unit tests preceding implementation, ensuring high fault detection early in the cycle.

**Verification Phase (DevOps):** To manage integration, I implemented a **DevOps** lifecycle. The `VERIFY` stage is automated via GitHub Actions, providing continuous feedback loops and preventing "integration hell."

### 3.2 Automated Regression
To support this lifecycle:
* **Pipeline:** A GitHub Actions workflow (`.github/workflows/ci.yml`) triggers on every `push`.
* **Policy:** No code is merged to `main` unless the "Verify" stage (Unit + Integration Tests) passes.

### 3.3 Process Risks
* **Schedule Risk:** Inadequate unit testing could lead to costly delays during the later integration phase.
**Mitigation:** The "TestFirst" approach ensures unit correctness before components are combined.

* **Technology Risk:** Unfamiliarity with the `MockMvc` testing framework could lead to spending too much time on scaffolding and not enough time on testing complex business logic.
**Mitigation:** I invested time in scaffolding simple "Hello World" controller tests to master the tool before testing complex business logic.