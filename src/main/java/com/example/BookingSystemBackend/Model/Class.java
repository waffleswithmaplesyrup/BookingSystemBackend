package com.example.BookingSystemBackend.Model;

import com.example.BookingSystemBackend.Enum.ClassType;
import com.example.BookingSystemBackend.Enum.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;
    private Timestamp startTimestamp;
    private int duration;
    private int creditsRequired;
    private int classSize;
    private int availableSlots;
    @Enumerated(EnumType.STRING)
    private ClassType classType;
    @Enumerated(EnumType.STRING)
    private Country country;
}
