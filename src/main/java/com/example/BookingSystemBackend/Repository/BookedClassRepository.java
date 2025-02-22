package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.BookedClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedClassRepository extends JpaRepository<BookedClass, Long> {
}
