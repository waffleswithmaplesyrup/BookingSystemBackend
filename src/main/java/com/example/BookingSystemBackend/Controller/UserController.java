package com.example.BookingSystemBackend.Controller;

import com.example.BookingSystemBackend.DTO.ChangePasswordRequestDTO;
import com.example.BookingSystemBackend.DTO.ResetPasswordRequestDTO;
import com.example.BookingSystemBackend.DTO.UserRequestDTO;
import com.example.BookingSystemBackend.Exception.CredentialsMismatchException;
import com.example.BookingSystemBackend.Exception.EmailAlreadyTakenException;
import com.example.BookingSystemBackend.Exception.InvalidEmailException;
import com.example.BookingSystemBackend.Exception.PasswordTooShortException;
import com.example.BookingSystemBackend.Model.User;
import com.example.BookingSystemBackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userRequestDTO) {

        try {
            User savedUser = userService.registerUser(userRequestDTO);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "user successfully registered");
            successResponse.put("data", savedUser);

            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (EmailAlreadyTakenException | PasswordTooShortException | InvalidEmailException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> viewProfile(@PathVariable Long userId) {
        try {
            User user = userService.viewProfile(userId);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "profile found");
            successResponse.put("data", user);

            return ResponseEntity.ok().body(successResponse);
        } catch (NoSuchElementException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "profile not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        try {
            userService.changePassword(changePasswordRequestDTO);

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "password changed successfully");

            return ResponseEntity.ok().body(successResponse);
        } catch (CredentialsMismatchException | PasswordTooShortException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (NoSuchElementException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "profile not found");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequest) {
        try {
            userService.resetPassword(resetPasswordRequest);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "password reset successfully");

            return ResponseEntity.ok().body(successResponse);
        } catch (InvalidEmailException | PasswordTooShortException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
