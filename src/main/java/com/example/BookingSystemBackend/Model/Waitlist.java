package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitlistId;
    private Timestamp waitlistTimestamp;
    @ManyToOne
    private ClassInfo classWaitlisted;
    @ManyToOne
    private User user;

    public Waitlist(Timestamp waitlistTimestamp,
                    ClassInfo classWaitlisted,
                    User user) {
        this.waitlistTimestamp = waitlistTimestamp;
        this.classWaitlisted = classWaitlisted;
        this.user = user;
    }
}
