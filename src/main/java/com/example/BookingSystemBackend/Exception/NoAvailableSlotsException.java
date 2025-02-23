package com.example.BookingSystemBackend.Exception;

public class NoAvailableSlotsException extends RuntimeException {
    public NoAvailableSlotsException() {
        super("No available slots left for this class. User has been added to the waitlist.");
    }
}
