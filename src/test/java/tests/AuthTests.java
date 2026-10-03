package tests;

import com.reservationhub.client.AuthClient;
import com.reservationhub.client.BookingClient;
import com.reservationhub.models.AuthRequest;
import com.reservationhub.models.BookingRequest;
import com.reservationhub.utils.TestDataFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTests extends BaseTest {

    @Test(groups = {"auth", "smoke"}, description = "Verify successful authentication returns a valid token")
    public void testValidAuthentication() {
        AuthRequest validReq = new AuthRequest("admin", "password123");
        Response response = AuthClient.createTokenRaw(validReq);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("token"));
    }

    @Test(groups = {"auth", "negative"}, description = "Verify authentication fails with invalid credentials")
    public void testInvalidAuthentication() {
        AuthRequest invalidReq = new AuthRequest("invalidUser", "wrongPass");
        Response response = AuthClient.createTokenRaw(invalidReq);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test(groups = {"auth", "security"}, description = "Verify write operation fails when auth token is missing")
    public void testUpdateWithoutAuthToken() {
        Response response = BookingClient.deleteBooking(1, null);
        Assert.assertEquals(response.getStatusCode(), 403, "Expected 403 Forbidden when token is omitted");
    }

    @Test(groups = {"auth", "smoke"}, description = "Verify HTTP Basic Auth works for protected delete operation")
    public void testDeleteWithHttpBasicAuth() {
        // Create a temporary booking to delete via Basic Auth
        BookingRequest payload = TestDataFactory.createValidBooking();
        Response createResp = BookingClient.createBooking(payload);
        int tempId = createResp.jsonPath().getInt("bookingid");
        Response deleteResp = BookingClient.deleteBookingWithBasicAuth(tempId, "admin", "password123");
        Assert.assertTrue(deleteResp.getStatusCode() == 201 || deleteResp.getStatusCode() == 200);
    }
}