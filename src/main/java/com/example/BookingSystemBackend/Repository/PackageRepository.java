package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.PackageBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<PackageBundle, Long> {
}
