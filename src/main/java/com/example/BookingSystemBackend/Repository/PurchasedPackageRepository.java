package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.PurchasedPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedPackageRepository extends JpaRepository<PurchasedPackage, Long> {
}
