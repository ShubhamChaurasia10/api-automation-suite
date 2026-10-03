package tests;


import com.reservationhub.client.BookingClient;
import com.reservationhub.models.BookingRequest;
import com.reservationhub.utils.TestDataFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingSchemaTests extends BaseTest {

    @Test(description = "Validate POST /booking response payload against JSON schema contract")
    public void testCreateBookingSchema() {
        BookingRequest payload = TestDataFactory.createValidBooking();
        Response response = BookingClient.createBooking(payload);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-response-schema.json"));
    }

    @Test(description = "Validate GET /booking/{id} response payload against JSON schema contract")
    public void testGetBookingSchema() {
        Response response = BookingClient.getBooking(1);
        if (response.getStatusCode() == 200) {
            response.then().assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
        } else {
            Assert.assertEquals(response.getStatusCode(), 404, "Booking ID 1 not found in current environment state");
        }
    }
}