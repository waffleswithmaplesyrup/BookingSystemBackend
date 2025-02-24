package com.example.BookingSystemBackend.Model;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Enum.Currency;
import com.example.BookingSystemBackend.Enum.DurationType;
import com.example.BookingSystemBackend.Enum.PackageType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class PackageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;
    private int credits;
    private int durationValue;
    @Enumerated(EnumType.STRING)
    private DurationType durationType;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    @Enumerated(EnumType.STRING)
    private Country country;

    public PackageInfo(int credits,
                       int durationValue,
                       DurationType durationType,
                       BigDecimal price,
                       Currency currency,
                       PackageType packageType,
                       Country country) {
        this.credits = credits;
        this.durationValue = durationValue;
        this.durationType = durationType;
        this.price = price;
        this.currency = currency;
        this.packageType = packageType;
        this.country = country;
    }
}
