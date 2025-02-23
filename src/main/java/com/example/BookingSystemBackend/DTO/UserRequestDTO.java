package com.example.BookingSystemBackend.DTO;

import com.example.BookingSystemBackend.Enum.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Country country;
}
