package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.BookedClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedClassRepository extends JpaRepository<BookedClass, Long> {

    @Query("SELECT count(b) FROM BookedClass b " +
            "WHERE b.user.userId = :userId " +
            "AND b.classBooked.classId = :classId")
    public int numberOfBookingsMadeToThisClass(@Param("userId") Long userId, @Param("classId") Long classId);
}
