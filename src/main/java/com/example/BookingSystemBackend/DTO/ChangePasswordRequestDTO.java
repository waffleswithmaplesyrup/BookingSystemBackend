package com.example.BookingSystemBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {

    private Long userId;
    private String oldPassword;
    private String newPassword;
}
