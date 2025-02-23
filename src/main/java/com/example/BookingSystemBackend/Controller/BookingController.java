package com.example.BookingSystemBackend.Controller;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Model.ClassInfo;
import com.example.BookingSystemBackend.Model.PackageBundle;
import com.example.BookingSystemBackend.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final ClassService classService;

    @Autowired
    public BookingController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/country={country}")
    public ResponseEntity<List<ClassInfo>> viewAllClasses(@PathVariable Country country) {
        return ResponseEntity.ok().body(classService.viewAllClasses(country));
    }

//    public ResponseEntity<?> bookClass() {
//
//    }
}
