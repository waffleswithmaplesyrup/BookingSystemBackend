package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookedClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookedClassId;
    private Timestamp bookingTimestamp;
    private Timestamp cancelledTimestamp;
    private boolean isCancelled;
    private boolean isRefunded;
    private boolean checkedIn;
    @ManyToOne
    private Class classBooked;
    @ManyToOne
    private User user;
}
