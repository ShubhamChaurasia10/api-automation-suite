package com.reservationhub.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookingClient {

    public static Response createBooking(Object payload) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .post("/booking");
    }

    public static Response getBooking(Object bookingId) {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .get("/booking/" + bookingId);
    }

    public static Response getBookingIdsWithFilters(String firstname, String lastname, String checkin, String checkout) {
        RequestSpecification spec = RestAssured.given().accept(ContentType.JSON);
        if (firstname != null) spec.queryParam("firstname", firstname);
        if (lastname != null) spec.queryParam("lastname", lastname);
        if (checkin != null) spec.queryParam("checkin", checkin);
        if (checkout != null) spec.queryParam("checkout", checkout);
        return spec.get("/booking");
    }

    public static Response updateBooking(Object bookingId, Object payload, String token) {
        RequestSpecification spec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload);

        if (token != null) {
            spec.cookie("token", token);
        }
        return spec.put("/booking/" + bookingId);
    }

    public static Response partialUpdateBooking(Object bookingId, Object payload, String token) {
        RequestSpecification spec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload);

        if (token != null) {
            spec.cookie("token", token);
        }
        return spec.patch("/booking/" + bookingId);
    }

    public static Response deleteBooking(Object bookingId, String token) {
        RequestSpecification spec = RestAssured.given()
                .contentType(ContentType.JSON);

        if (token != null) {
            spec.cookie("token", token);
        }
        return spec.delete("/booking/" + bookingId);
    }

    public static Response deleteBookingWithBasicAuth(Object bookingId, String username, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(username, password)
                .delete("/booking/" + bookingId);
    }
}