package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class BookedClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookedClassId;
    private ZonedDateTime bookingTime;
    private ZonedDateTime cancelledTime;
    private boolean isCancelled;
    private boolean isRefunded;
    private boolean checkedIn;
    @ManyToOne
    @JoinColumn(name = "FK_classId")
    private ClassInfo classInfo;
    @ManyToOne
    @JoinColumn(name = "FK_userId")
    private User user;

    public BookedClass(ZonedDateTime bookingTime,
                       ZonedDateTime cancelledTime,
                       boolean isCancelled,
                       boolean isRefunded,
                       boolean checkedIn,
                       ClassInfo classInfo,
                       User user) {
        this.bookingTime = bookingTime;
        this.cancelledTime = cancelledTime;
        this.isCancelled = isCancelled;
        this.isRefunded = isRefunded;
        this.checkedIn = checkedIn;
        this.classInfo = classInfo;
        this.user = user;
    }
}
