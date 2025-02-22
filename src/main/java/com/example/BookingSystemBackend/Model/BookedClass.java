package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
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
    private ClassInfo classBooked;
    @ManyToOne
    private User user;

    public BookedClass(Timestamp bookingTimestamp,
                       Timestamp cancelledTimestamp,
                       boolean isCancelled,
                       boolean isRefunded,
                       boolean checkedIn,
                       ClassInfo classBooked,
                       User user) {
        this.bookingTimestamp = bookingTimestamp;
        this.cancelledTimestamp = cancelledTimestamp;
        this.isCancelled = isCancelled;
        this.isRefunded = isRefunded;
        this.checkedIn = checkedIn;
        this.classBooked = classBooked;
        this.user = user;
    }
}
