# Defect Report - Restful-Booker API

## BUG-01: API accepts negative total price when creating a booking
* **Severity:** High
* **Justification:** Generates erroneous financial records and permits negative revenue transactions.
* **Steps to Reproduce (cURL):**
  ```bash
  curl -X POST https://restful-booker.herokuapp.com/booking \
    -H "Content-Type: application/json" \
    -d '{
      "firstname": "Alex",
      "lastname": "Martin",
      "totalprice": -500,
      "depositpaid": true,
      "bookingdates": { "checkin": "2025-01-01", "checkout": "2025-01-05" },
      "additionalneeds": "Breakfast"
    }'