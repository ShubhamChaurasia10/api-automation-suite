package tests;

import com.reservationhub.client.BookingClient;
import com.reservationhub.models.BookingRequest;
import com.reservationhub.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class BookingCrudTests extends BaseTest {

    private int createdBookingId;

    @Test(priority = 1, groups = {"crud", "smoke"}, description = "Verify successful creation of a booking")
    public void testCreateBooking() {
        BookingRequest payload = TestDataFactory.createValidBooking();
        Response response = BookingClient.createBooking(payload);
        Assert.assertEquals(response.getStatusCode(), 200);
        createdBookingId = response.jsonPath().getInt("bookingid");
        Assert.assertTrue(createdBookingId > 0);
        Assert.assertEquals(response.jsonPath().getString("booking.firstname"), payload.getFirstname());
    }

    @Test(priority = 2, dependsOnMethods = "testCreateBooking", groups = {"crud", "smoke"}, description = "Verify retrieving a booking by ID")
    public void testGetBookingById() {
        Response response = BookingClient.getBooking(createdBookingId);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), "Alex");
    }

    @Test(priority = 3, groups = {"crud", "regression"}, description = "Verify GET /booking with firstname, lastname, and date query filters")
    public void testGetBookingIdsWithFilters() {
        Response response = BookingClient.getBookingIdsWithFilters("Alex", "Mercer", "2025-06-01", "2025-06-05");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getList("bookingid").size() > 0, "Filtered list should return at least one ID");
    }

    @Test(priority = 4, dependsOnMethods = "testCreateBooking", groups = {"crud", "regression"}, description = "Verify updating an existing booking completely via PUT")
    public void testUpdateBookingPut() {
        BookingRequest updatePayload = TestDataFactory.createValidBooking();
        updatePayload.setFirstname("Alexander");
        updatePayload.setTotalprice(300);
        Response response = BookingClient.updateBooking(createdBookingId, updatePayload, authToken);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), "Alexander");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 300);
    }

    @Test(priority = 5, dependsOnMethods = "testCreateBooking", groups = {"crud", "regression"}, description = "Verify updating an existing booking partially via PATCH")
    public void testUpdateBookingPatch() {
        Map<String, String> patchPayload = Map.of("firstname", "Axel");
        Response response = BookingClient.partialUpdateBooking(createdBookingId, patchPayload, authToken);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), "Axel");
    }

    @Test(priority = 6, dependsOnMethods = "testCreateBooking", groups = {"crud", "smoke"}, description = "Verify deleting a booking")
    public void testDeleteBooking() {
        Response deleteResponse = BookingClient.deleteBooking(createdBookingId, authToken);
        Assert.assertTrue(deleteResponse.getStatusCode() == 201 || deleteResponse.getStatusCode() == 200);
        Response getResponse = BookingClient.getBooking(createdBookingId);
        Assert.assertEquals(getResponse.getStatusCode(), 404);
    }
}