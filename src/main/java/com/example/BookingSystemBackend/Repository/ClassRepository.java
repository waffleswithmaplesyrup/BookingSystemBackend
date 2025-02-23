package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Model.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassInfo, Long> {
    public List<ClassInfo> findAllByCountry(Country country);
}
