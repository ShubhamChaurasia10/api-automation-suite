package com.reservationhub.client;

import com.reservationhub.config.ConfigManager;
import com.reservationhub.models.AuthRequest;
import com.reservationhub.models.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthClient {

    public static Response createTokenRaw(Object payload) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/auth");
    }

    public static String getValidToken() {
        AuthRequest credentials = new AuthRequest(ConfigManager.get("username"), ConfigManager.get("password"));
        Response response = createTokenRaw(credentials);

        if (response.getStatusCode() == 200) {
            return response.as(AuthResponse.class).getToken();
        }
        throw new RuntimeException("Failed to acquire valid auth token: " + response.getBody().asString());
    }
}