package com.example.BookingSystemBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {

    private Long packageId;
    private Long userId;
}
