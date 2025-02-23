package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
    public List<Waitlist> findAllByUser_UserIdAndClassWaitlisted_ClassId(Long userId, Long classId);

    @Query("SELECT w FROM Waitlist w " +
            "WHERE w.classWaitlisted.classId = :classId " +
            "ORDER BY waitlistTimestamp LIMIT 1")
    public Optional<Waitlist> getFirstOnWaitlist(@Param("classId") Long classId);
}
