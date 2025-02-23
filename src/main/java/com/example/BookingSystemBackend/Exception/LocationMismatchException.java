package com.example.BookingSystemBackend.Exception;

import com.example.BookingSystemBackend.Enum.Country;

public class LocationMismatchException extends RuntimeException {
    public LocationMismatchException(Country country) {
        super("user is not from " + country);
    }
}
