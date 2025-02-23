package com.example.BookingSystemBackend.Exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Invalid email.");
    }
}
