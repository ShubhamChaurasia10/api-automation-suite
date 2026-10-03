package tests;

import com.reservationhub.client.AuthClient;
import com.reservationhub.config.ConfigManager;
import com.reservationhub.utils.HealthCheckUtil;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static String authToken;

    @BeforeSuite
    public void globalSetup() {
        RestAssured.baseURI = ConfigManager.get("baseUrl");

        // Address Heroku dyno cold-start delay
        HealthCheckUtil.waitForApiToWakeUp();

        // Retrieve token for write operations
        authToken = AuthClient.getValidToken();
    }
}