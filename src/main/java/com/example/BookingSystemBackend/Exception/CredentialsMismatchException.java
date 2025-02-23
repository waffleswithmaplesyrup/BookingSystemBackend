package com.example.BookingSystemBackend.Exception;

public class CredentialsMismatchException extends RuntimeException {
    public CredentialsMismatchException() {
        super("Wrong credentials. Please try again.");
    }
}
