package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
    public List<Waitlist> findAllByUser_UserIdAndClassWaitlisted_ClassId(Long userId, Long classId);
}
