package com.example.BookingSystemBackend.Controller;

import com.example.BookingSystemBackend.DTO.BookingRequestDTO;
import com.example.BookingSystemBackend.DTO.NewClassRequestDTO;
import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Model.ClassInfo;
import com.example.BookingSystemBackend.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassController {

    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/country={country}")
    public ResponseEntity<List<ClassInfo>> viewAllClasses(@PathVariable Country country) {
        return ResponseEntity.ok().body(classService.viewAllClasses(country));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewClassSchedule(@RequestBody NewClassRequestDTO newClassRequestDTO) {
        try {
            ClassInfo newClass = classService.createNewClassSchedule(newClassRequestDTO);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "class created successfully");
            successResponse.put("data", newClass);

            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
