package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitlistId;
    private LocalDateTime waitlistTimestamp;
    @ManyToOne
    private ClassInfo classWaitlisted;
    @ManyToOne
    private User user;

    public Waitlist(LocalDateTime waitlistTimestamp,
                    ClassInfo classWaitlisted,
                    User user) {
        this.waitlistTimestamp = waitlistTimestamp;
        this.classWaitlisted = classWaitlisted;
        this.user = user;
    }
}
