package com.example.BookingSystemBackend.Controller;

import com.example.BookingSystemBackend.DTO.BookingRequestDTO;
import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Exception.*;
import com.example.BookingSystemBackend.Model.BookedClass;
import com.example.BookingSystemBackend.Model.ClassInfo;
import com.example.BookingSystemBackend.Model.Waitlist;
import com.example.BookingSystemBackend.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final ClassService classService;

    @Autowired
    public BookingController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("/book-class")
    public ResponseEntity<?> bookClass(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            BookedClass bookingClass = classService.bookClass(bookingRequestDTO);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "class booked successfully");
            successResponse.put("data", bookingClass);

            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "class or user not found");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/cancel={bookedClassId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookedClassId) {
        try {
            BookedClass cancelledClass = classService.cancelBooking(bookedClassId);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "class cancelled successfully");
            successResponse.put("data", cancelledClass);

            return ResponseEntity.ok().body(successResponse);
        } catch (InvalidTimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (NoSuchElementException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "class or user not found");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/waitlist")
    public ResponseEntity<?> addToWaitlist(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            Waitlist waitlist = classService.addToWaitlist(bookingRequestDTO);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "added to waitlist successfully");
            successResponse.put("data", waitlist);

            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PatchMapping("/check-in={bookedClassId}")
    public ResponseEntity<?> checkInToClass(@PathVariable Long bookedClassId) {
        try {
            BookedClass checkInClass = classService.checkInToClass(bookedClassId);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "checked in successfully");
            successResponse.put("data", checkInClass);

            return ResponseEntity.ok().body(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
