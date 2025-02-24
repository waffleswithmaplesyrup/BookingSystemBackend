package com.example.BookingSystemBackend.DTO;

import com.example.BookingSystemBackend.Enum.ClassType;
import com.example.BookingSystemBackend.Enum.Country;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewClassRequestDTO {

    private String startTime;
    private String endTime;
    private int duration;
    private int creditsRequired;
    private int classSize;
    @Enumerated(EnumType.STRING)
    private ClassType classType;
    @Enumerated(EnumType.STRING)
    private Country country;
}
