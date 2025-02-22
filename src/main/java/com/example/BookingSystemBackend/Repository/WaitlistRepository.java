package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
}
