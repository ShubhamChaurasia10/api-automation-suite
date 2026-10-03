package tests;

import com.reservationhub.client.BookingClient;
import com.reservationhub.models.BookingRequest;
import com.reservationhub.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingNegativeTests extends BaseTest {

    @Test(groups = {"negative", "bug"}, description = "Verify API rejects creation when checkout date precedes checkin date")
    public void testCheckoutBeforeCheckinRejected() {
        BookingRequest badDateBooking = TestDataFactory.createBookingWithDates("2025-08-10", "2025-08-01");
        Response response = BookingClient.createBooking(badDateBooking);
        Assert.assertEquals(response.getStatusCode(), 400, "API should reject checkout date prior to checkin date");
    }

    @Test(groups = {"negative", "bug"}, description = "Verify API rejects creation when total price is negative")
    public void testNegativeTotalPriceRejected() {
        BookingRequest negativePriceBooking = TestDataFactory.createBookingWithPrice(-100);
        Response response = BookingClient.createBooking(negativePriceBooking);
        Assert.assertEquals(response.getStatusCode(), 400, "API should reject negative total prices");
    }

    @Test(groups = {"negative", "boundary"}, description = "Verify API handles missing required fields gracefully")
    public void testMissingRequiredFields() {
        String missingFieldsPayload = "{\"firstname\": \"OnlyFirstname\"}";
        Response response = BookingClient.createBooking(missingFieldsPayload);
        Assert.assertTrue(response.getStatusCode() >= 400, "API should return 4xx error on missing required fields");
    }

    @Test(groups = {"negative", "boundary"}, description = "Verify API rejects invalid data types (string passed into price integer field)")
    public void testInvalidDataTypeForTotalPrice() {
        String invalidTypePayload = "{\n" +
                "  \"firstname\": \"Jim\",\n" +
                "  \"lastname\": \"Brown\",\n" +
                "  \"totalprice\": \"invalidStringPrice\",\n" +
                "  \"depositpaid\": true,\n" +
                "  \"bookingdates\": { \"checkin\": \"2025-01-01\", \"checkout\": \"2025-01-05\" }\n" +
                "}";
        Response response = BookingClient.createBooking(invalidTypePayload);
        Assert.assertTrue(response.getStatusCode() >= 400, "API should reject string values in integer field");
    }

    @Test(groups = {"negative", "boundary"}, description = "Verify API rejects non-ISO malformed date formats")
    public void testMalformedDateFormats() {
        BookingRequest badDateBooking = TestDataFactory.createBookingWithDates("01/01/2025", "2025-13-45");
        Response response = BookingClient.createBooking(badDateBooking);
        Assert.assertTrue(response.getStatusCode() >= 400, "API should reject malformed date format strings");
    }

    @Test(groups = {"negative", "boundary"}, description = "Verify fetching a non-existent booking returns 404")
    public void testGetNonExistentBookingId() {
        Response response = BookingClient.getBooking(99999999);
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(groups = {"negative", "boundary"}, description = "Verify API handles empty creation payload gracefully")
    public void testEmptyPayloadCreation() {
        Response response = BookingClient.createBooking("{}");
        Assert.assertTrue(response.getStatusCode() >= 400, "API should return client error code for empty JSON");
    }
}