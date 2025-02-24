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
    @JoinColumn(name = "FK_classId")
    private ClassInfo classInfo;
    @ManyToOne
    @JoinColumn(name = "FK_userId")
    private User user;

    public Waitlist(LocalDateTime waitlistTimestamp,
                    ClassInfo classInfo,
                    User user) {
        this.waitlistTimestamp = waitlistTimestamp;
        this.classInfo = classInfo;
        this.user = user;
    }
}
