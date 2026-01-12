# Testable Requirements Analysis for PizzaDrone

This document summarises functional and non-functional requirements derived from the project specification. Each requirement is written in a testable form and used to justify the testing strategy in the portfolio.

## 1. Functional Requirements (Business Logic)

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
- **FR7 (Movement Constraints):** The drone moves using **16 compass directions** with a fixed step size/tolerance of **0.00015 degrees**.
- **FR8 (Hovering):** The drone must perform a hover move at pickup (restaurant) and drop-off (Appleton Tower), if required by the specification.
- **FR9 (No-Fly Zones):** The computed flight path must not enter any defined No-Fly Zone polygons.
- **FR10 (Central Area Constraint):** Once the drone enters the defined Central Area, it must not leave it before reaching the destination (Appleton Tower).
- **FR11 (Coordinate System):** All calculations use longitude/latitude degrees.

## 2. System Integration Requirements

- **IR1 (External Data / Data-Driven):** The system must fetch Restaurants, No-Fly Zones, and Central Area definitions from the external ILP REST service (no hardcoding).
- **IR2 (Error Handling):** If external data is missing or malformed, the service should fail gracefully and return appropriate HTTP error responses rather than crashing.

## 3. Performance Requirements

- **PR1 (Execution Time):** Delivery path calculation for a valid order must complete within **60 seconds**.

## 4. Interface Requirements (API)

- **API1:** `POST /validateOrder` returns an order validation result.
- **API2:** `POST /calcDeliveryPath` returns a flight path as a list of `LngLat`.
- **API3:** `POST /calcDeliveryPathAsGeoJson` returns the path as a GeoJSON feature collection.
- **API4 (HTTP Behaviour):** Invalid inputs must return `400 Bad Request` (or a defined validation error response), not `500 Internal Server Error`.
