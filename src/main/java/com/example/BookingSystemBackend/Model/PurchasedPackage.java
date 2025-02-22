package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchasedPackageId;
    private int creditsRemaining;
    private Timestamp purchaseDate;
    private Timestamp expiryDate;
    private boolean ixExpired;
    @ManyToOne
    private Package packagePurchased;
    @ManyToOne
    private User user;
}
