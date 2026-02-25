# Car Rental System - Technical Assessment

## Project Overview

This is a simulated **Car Rental System** built with Java 17 and Spring Boot 3.4. 

It follows **Domain-Driven Design (DDD)** principles and was developed using the **Test-Driven Development (TDD)** methodology.

## Architecture Decisions

**DDD Layers:** Strict separation between api (DTOs/Controllers), domain (Business Logic/Models), and infrastructure.

**Java Records:** Used for DTOs and immutable Domain Models to ensure data integrity and clean code (eliminating Lombok-related boilerplate and IDE issues).

**In-Memory Storage:** Using ConcurrentHashMap for reservations to focus on business logic rather than database configuration.

**Concurrency Control:** The reservation logic is synchronized to prevent "double booking" (race conditions) in a multi-threaded environment.

**Architecture Testing:** Integrated ArchUnit to automatically verify that layer boundaries are respected (e.g., Domain not accessing API).

## AI Usage & Disclosure

AI tools were used to accelerate development and brainstorm edge cases.

Prompt Tuning Example

**Initial Prompt:** "Write a car rental system in Spring Boot." -> Resulted in a generic, monolithic application without tests.

**Enhanced Prompt:** "Implement a car rental reservation system using Java 17, Spring Boot 3.4, and DDD. Focus on thread-safe domain logic for checking date overlaps. Use TDD approach with JUnit 5 and AssertJ. Include ArchUnit tests for layer separation." -> This produced the current modular structure.

## Validation & Review

**Correction:** I manually replaced deprecated @MockBean with the new Spring Boot 3.4 @MockitoBean.

**Bug Fix:** AI initially generated a simple date check. I manually implemented the overlapsWith logic to handle complex interval overlaps correctly.

**Refactoring:** Switched from Lombok @Data to Java Records to avoid compilation issues and follow modern Java standards.

## Known Gaps & Future Improvements

**Persistence:** Data is volatile (lost on restart). Integration with Spring Data JPA/Hibernate is the next logical step.

**Date Validation:** Current version allows booking in the past. Should add @FutureOrPresent validation.

**Time Zones:** Uses LocalDateTime. A production system should use ZonedDateTime or Instant (UTC).

**Pricing Engine:** Currently, the system only handles availability. A pricing module based on car type and duration would be a natural extension.

## How to Run & Test

**Clone the repository:** git clone <your-repo-link>

**Run the app:** ./mvnw spring-boot:run

**Execute tests:** ./mvnw clean verify

## Manual Test (curl):

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{"type": "SEDAN", "startTime": "2025-10-10T10:00:00", "durationDays": 3}'


## API Test Scenarios

**1. Basic Reservation (SEDAN)**

Should return 200 OK (Car ID: SED-1).

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{
"type": "SEDAN",
"startTime": "2025-10-10T10:00:00",
"durationDays": 3
}'


**2. Second Reservation of the same type (SEDAN)**

Should return 200 OK (Car ID: SED-2), as we have two Sedans in the fleet.

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{
"type": "SEDAN",
"startTime": "2025-10-10T10:00:00",
"durationDays": 2
}'


**3. Exceeding Fleet Limit (SEDAN)**

Should return 400 Bad Request, as both Sedans are already occupied during this period.

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{
"type": "SEDAN",
"startTime": "2025-10-10T10:00:00",
"durationDays": 1
}'


**4. Van Reservation (Unique resource)**

Should return 200 OK. We only have one Van ("VAN-1").

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{
"type": "VAN",
"startTime": "2025-11-01T12:00:00",
"durationDays": 5
}'


**5. Overlapping Dates (Same Van)**

Should return 400 Bad Request, because the date (Nov 3rd) overlaps with the existing Van reservation (Nov 1st + 5 days).

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{
"type": "VAN",
"startTime": "2025-11-03T12:00:00",
"durationDays": 2
}'


**6. Reservation of the same car at a different time**

Should return 200 OK. The Van is available after Nov 6th.

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{
"type": "VAN",
"startTime": "2025-11-10T12:00:00",
"durationDays": 2
}'


**7. List all reservations**

Returns a JSON array with all successful operations.

curl -X GET http://localhost:8080/api/rentals/reservations


**8. Invalid Data (Days <= 0)**

Should return 400 Bad Request (validation in the service).

curl -X POST http://localhost:8080/api/rentals/reserve \
-H "Content-Type: application/json" \
-d '{
"type": "SUV",
"startTime": "2025-12-01T10:00:00",
"durationDays": 0
}'
