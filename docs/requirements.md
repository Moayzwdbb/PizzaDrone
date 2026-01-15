# Testable Requirements Analysis for PizzaDrone

This document summarises requirements derived from the PizzaDrone project. Each requirement is written in a testable form and used to justify the testing strategy in the portfolio.

## 1. Functional Requirements

### 1.1 Order Validation
- **FR1 (Pizza Count):** Orders must contain **1–4** pizzas.
    - If pizzas are missing/empty, return `INVALID` with `EMPTY_ORDER`.
    - If pizzas exceed 4, return `INVALID` with `MAX_PIZZA_COUNT_EXCEEDED`.
- **FR2 (Pizza Existence):** Each pizza must exist on the chosen restaurant menu; otherwise return `INVALID` with an appropriate pizza error code (e.g., not defined / invalid price).
- **FR3 (Single Restaurant):** All pizzas in an order must come from a single restaurant; otherwise return `INVALID` with `PIZZA_FROM_MULTIPLE_RESTAURANTS`.
- **FR4 (Restaurant Availability):** The restaurant must be open on the order day; otherwise return `INVALID` with `RESTAURANT_CLOSED`.
- **FR5 (Credit Card Format):**
    - Card number must match the expected format (e.g., **16 digits**); otherwise return `INVALID` with `CARD_NUMBER_INVALID`.
    - CVV must be **3 digits**; otherwise return `INVALID` with `CVV_INVALID`.
    - Expiry date must be valid relative to the order date; otherwise return `INVALID` with `EXPIRY_DATE_INVALID`.
- **FR6 (Total Price):** The total price in pence must match the expected sum of pizzas according to menu prices; otherwise return `INVALID` with `TOTAL_INCORRECT`.

### 1.2 Drone Movement & Pathfinding
- **FR7 (Movement Constraints):** The drone moves using **16 compass directions** with a fixed step size/tolerance of **0.00015 degrees**, and the path must not exceed the maximum allowed number of moves (DRONE_MAX_MOVES = 2000)
- **FR8 (Hover semantics):** A hover move (angle = 999) must not change the drone position (LngLat remains identical).
- **FR9 (No-Fly Zones):** The computed flight path must not enter any defined No-Fly Zone polygons.
- **FR10 (Central Area Constraint):** Once the drone enters the defined Central Area, it must not leave it before reaching the destination (Appleton Tower).
- **FR11 (Coordinate System):** All calculations use longitude/latitude degrees.

### 1.3 API Functional Behaviour
- **FR12 (validateOrder endpoint):** POST /validateOrder returns an order validation result.
- **FR13 (calcDeliveryPath endpoint):** POST /calcDeliveryPath returns a flight path as a list of LngLat.
- **FR14 (calcDeliveryPathAsGeoJson endpoint):** POST /calcDeliveryPathAsGeoJson returns a GeoJSON feature collection representing the path.

## 2. Qualitative Requirements
- **QR1 (Data-driven behaviour):** The system must fetch Restaurants, No-Fly Zones, and Central Area definitions from the external ILP REST service (no hardcoding).
- **QR2 (API robustness):** Invalid inputs (malformed JSON, missing required fields, invalid order structure) must not cause 500 Internal Server Error; they should result in 400 Bad Request or a defined validation error response.
- **QR3 (Observability and debuggability):** Important validation and decision points should be logged at an appropriate level, without leaking sensitive information (e.g., do not log full credit card number or CVV).
- **QR4 (Performance):** Delivery path calculation for a valid order must complete within 60 seconds.

## 3. Test Approach

| ID        | Test Approach                                                                                                                                                                                                                                                           |
|-----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| FR1-FR6   | Tests are designed to trigger each validation rule and assert the externally observable outcome (OrderStatus/OrderValidationCode) matches the specification; lower-level validators are unit-tested where applicable to confirm the rule implementations independently. |
| FR7-FR11  | Unit-test movement (calcNextPosition: 16 directions, 0.00015 step, hover=999) and assert path invariants on the produced route                                                                                                                                          |
| FR12–FR14 | API contract tests with `MockMvc`: valid request returns 200 and correct schema; invalid payload returns 400 (not 500).                                                                                                                                                 |
| QR1       | Mock external API client responses and verify service behaviour uses fetched data (no hardcoded regions/menus).                                                                                                                                                         |
| QR2       | Negative API tests (malformed JSON / missing fields / null pizzas) must return 400 with defined error message.                                                                                                                                                          |
| QR3       | Using validation tests and capture logs showing decision points without leaking sensitive data.                                                                                                                                                                         |
| QR4       | Using valid path calculation completes within time limit.                                                                                                                                                                                                               |
