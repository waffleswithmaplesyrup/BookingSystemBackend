package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.BookedClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookedClassRepository extends JpaRepository<BookedClass, Long> {

    @Query("SELECT count(b) FROM BookedClass b " +
            "WHERE b.user.userId = :userId " +
            "AND b.classBooked.classId = :classId")
    public int numberOfBookingsMadeToThisClass(@Param("userId") Long userId, @Param("classId") Long classId);

    @Query("SELECT count(b) FROM BookedClass b " +
            "WHERE b.user.userId = :userId AND b.isCancelled = false AND " +
            "b.classBooked.startTime between :startTime and :classEndTime")
    public int overlappingClasses(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("classEndTime") LocalDateTime classEndTime);
}
