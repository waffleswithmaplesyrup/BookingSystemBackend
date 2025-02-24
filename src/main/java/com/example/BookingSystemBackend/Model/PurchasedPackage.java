package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
public class PurchasedPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchasedPackageId;
    private int creditsRemaining;
    private ZonedDateTime purchaseDate;
    private ZonedDateTime expiryDate;
    private boolean isExpired;
    @ManyToOne
    @JoinColumn(name = "FK_packageInfoId")
    private PackageInfo packageInfo;
    @ManyToOne
    @JoinColumn(name = "FK_userId")
    private User user;

    public PurchasedPackage(int creditsRemaining,
                            ZonedDateTime purchaseDate,
                            ZonedDateTime expiryDate,
                            boolean isExpired,
                            PackageInfo packageInfo,
                            User user) {
        this.creditsRemaining = creditsRemaining;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.isExpired = isExpired;
        this.packageInfo = packageInfo;
        this.user = user;
    }
}
