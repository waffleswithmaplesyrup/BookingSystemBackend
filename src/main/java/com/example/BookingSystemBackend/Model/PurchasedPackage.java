package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PurchasedPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchasedPackageId;
    private int creditsRemaining;
    private Date purchaseDate;
    private Date expiryDate;
    private boolean isExpired;
    @ManyToOne
    private PackageBundle packageBundle;
    @ManyToOne
    private User user;

    public PurchasedPackage(int creditsRemaining,
                            Date purchaseDate,
                            Date expiryDate,
                            boolean isExpired,
                            PackageBundle packageBundle,
                            User user) {
        this.creditsRemaining = creditsRemaining;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.isExpired = isExpired;
        this.packageBundle = packageBundle;
        this.user = user;
    }
}
