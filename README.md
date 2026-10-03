```markdown
# Reservation Hub API Automation

## Overview & Architecture
This suite provides automated regression coverage for the Reservation Hub API built on top of the **Restful-Booker** service.

* **Tech Stack:** Java 17, REST Assured, TestNG, ExtentReports, Jackson.
* **Design Pattern:** Client/Service Abstraction Pattern separating API calls, POJO models, configuration management, and test assertions.

---

## Strategy & Design Decisions
1. **CRUD Lifecycle Coverage:** Standard flow created, read, updated, and deleted dynamically without relying on pre-existing records.
2. **Contract & Negative Testing:** JsonSchema validation ensures schema stability, while negative boundary probes check price and date validation logic.
3. **Cold-Start Handling:** `HealthCheckUtil` issues a polling loop against `/ping` before running tests to wake up the Heroku dyno.
4. **Isolated Test Execution:** Tests generate their own payloads via `TestDataFactory` to avoid dependencies on data removed by periodic environment resets.

---

## Execution Instructions
Run the full test suite via Maven:
```bash
mvn clean test
```

