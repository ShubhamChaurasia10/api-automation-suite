package com.reservationhub.utils;

import com.reservationhub.models.BookingDates;
import com.reservationhub.models.BookingRequest;

public class TestDataFactory {

    public static BookingRequest createValidBooking() {
        return new BookingRequest(
                "Alex",
                "Mercer",
                250,
                true,
                new BookingDates("2025-06-01", "2025-06-05"),
                "Breakfast"
        );
    }

    public static BookingRequest createBookingWithDates(String checkin, String checkout) {
        return new BookingRequest(
                "Sally",
                "Brown",
                150,
                false,
                new BookingDates(checkin, checkout),
                "Late check-in"
        );
    }

    public static BookingRequest createBookingWithPrice(Integer price) {
        return new BookingRequest(
                "James",
                "Brown",
                price,
                true,
                new BookingDates("2025-07-01", "2025-07-04"),
                "None"
        );
    }
}