package com.example.BookingSystemBackend.Exception;

public class AlreadyBookedClassException extends RuntimeException {
    public AlreadyBookedClassException() {
        super("User has already booked this class. One booking per class is allowed.");
    }
}
