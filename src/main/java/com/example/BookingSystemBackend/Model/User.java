package com.example.BookingSystemBackend.Model;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Country country;

    public User(String username,
                String email,
                String password,
                Role role,
                Country country) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.country = country;
    }
}
