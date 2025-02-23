package com.example.BookingSystemBackend.Exception;

public class InvalidTimeException extends RuntimeException {
    public InvalidTimeException() {
        super("Invalid time.");
    }
}
