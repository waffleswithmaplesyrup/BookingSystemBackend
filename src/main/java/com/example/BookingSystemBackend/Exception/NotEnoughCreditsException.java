package com.example.BookingSystemBackend.Exception;

public class NotEnoughCreditsException extends RuntimeException {
    public NotEnoughCreditsException() {
        super("Not enough credits to book this class. Please purchase a new package.");
    }
}
