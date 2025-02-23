package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.PackageBundle;
import com.example.BookingSystemBackend.Model.PurchasedPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasedPackageRepository extends JpaRepository<PurchasedPackage, Long> {
    public List<PurchasedPackage> findAllByUser_UserId(Long userId);
}
