package com.example.BookingSystemBackend.Exception;

public class PasswordTooShortException extends RuntimeException {
    public PasswordTooShortException() {
        super("Password is too short. Please enter at least 8 characters.");
    }
}
