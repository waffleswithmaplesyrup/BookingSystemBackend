package com.example.BookingSystemBackend.Controller;

import com.example.BookingSystemBackend.DTO.PurchaseRequestDTO;
import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Exception.LocationMismatchException;
import com.example.BookingSystemBackend.Model.PackageInfo;
import com.example.BookingSystemBackend.Model.PurchasedPackage;
import com.example.BookingSystemBackend.Service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/package")
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/country={country}")
    public ResponseEntity<List<PackageInfo>> viewAllPackages(@PathVariable Country country) {
        return ResponseEntity.ok().body(packageService.viewAllPackages(country));
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchasePackage(@RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        try {
            PurchasedPackage purchasingPackage = packageService.purchasePackage(purchaseRequestDTO);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "package successfully purchased");
            successResponse.put("data", purchasingPackage);

            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (LocationMismatchException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        } catch (NoSuchElementException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "package or user not found");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user={userId}")
    public ResponseEntity<List<PurchasedPackage>> viewAllPackagesPurchased(@PathVariable Long userId) {
        return ResponseEntity.ok().body(packageService.viewAllPackagesPurchased(userId));
    }
}
