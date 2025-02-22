package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitlistId;
    private Timestamp waitlistTimestamp;
    @ManyToOne
    private Class classWaitlisted;
    @ManyToOne
    private User user;
}
