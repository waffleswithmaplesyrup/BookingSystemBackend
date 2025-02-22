package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<ClassInfo, Long> {
}
