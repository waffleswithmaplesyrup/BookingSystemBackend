package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Model.PurchasedPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasedPackageRepository extends JpaRepository<PurchasedPackage, Long> {
    public List<PurchasedPackage> findAllByUser_UserId(Long userId);

    @Query("SELECT p FROM PurchasedPackage p " +
            "WHERE p.user.userId = :userId " +
            "AND expiryDate > NOW() " +
            "AND isExpired = false " +
            "AND creditsRemaining >= :creditsRequired " +
            "ORDER BY expiryDate LIMIT 1")
    public PurchasedPackage findPackageToBookClassWith(@Param("userId") Long userId, @Param("creditsRequired") int creditsRequired);

    @Query("SELECT p FROM PurchasedPackage p " +
            "WHERE p.user.userId = :userId " +
            "AND expiryDate > NOW() " +
            "AND isExpired = false " +
            "ORDER BY expiryDate DESC LIMIT 1")
    public Optional<PurchasedPackage> findPackageToRefund(@Param("userId") Long userId);
}
