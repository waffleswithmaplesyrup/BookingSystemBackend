package com.example.BookingSystemBackend.Model;

import com.example.BookingSystemBackend.Enum.ClassType;
import com.example.BookingSystemBackend.Enum.Country;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class ClassInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int duration;
    private int creditsRequired;
    private int classSize;
    private int availableSlots;
    @Enumerated(EnumType.STRING)
    private ClassType classType;
    @Enumerated(EnumType.STRING)
    private Country country;

    public ClassInfo(ZonedDateTime startTime,
                     ZonedDateTime endTime,
                     int duration,
                     int creditsRequired,
                     int classSize,
                     int availableSlots,
                     ClassType classType,
                     Country country) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.creditsRequired = creditsRequired;
        this.classSize = classSize;
        this.availableSlots = availableSlots;
        this.classType = classType;
        this.country = country;
    }
}
