package com.example.BookingSystemBackend.Model;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Enum.Currency;
import com.example.BookingSystemBackend.Enum.DurationType;
import com.example.BookingSystemBackend.Enum.PackageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;
    private int credits;
    private int durationValue;
    private DurationType durationType;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    @Enumerated(EnumType.STRING)
    private Country country;
}
