package com.example.BookingSystemBackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class BookedClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookedClassId;
    private Date bookingTimestamp;
    private Date cancelledTimestamp;
    private boolean isCancelled;
    private boolean isRefunded;
    private boolean checkedIn;
    @ManyToOne
    private ClassInfo classBooked;
    @ManyToOne
    private User user;

    public BookedClass(Date bookingTimestamp,
                       Date cancelledTimestamp,
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
