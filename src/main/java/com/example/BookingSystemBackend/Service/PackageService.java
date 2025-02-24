package com.example.BookingSystemBackend.Service;

import com.example.BookingSystemBackend.DTO.PurchaseRequestDTO;
import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Enum.DurationType;
import com.example.BookingSystemBackend.Exception.LocationMismatchException;
import com.example.BookingSystemBackend.Model.PackageBundle;
import com.example.BookingSystemBackend.Model.PurchasedPackage;
import com.example.BookingSystemBackend.Model.User;
import com.example.BookingSystemBackend.Repository.PackageRepository;
import com.example.BookingSystemBackend.Repository.PurchasedPackageRepository;
import com.example.BookingSystemBackend.Repository.UserRepository;
import com.example.BookingSystemBackend.Utils.ZoneDateTimeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final PurchasedPackageRepository purchasedPackageRepository;
    private final UserRepository userRepository;
    private final ZoneDateTimeHelper zoneDateTimeHelper;

    @Autowired
    public PackageService(PackageRepository packageRepository,
                          PurchasedPackageRepository purchasedPackageRepository,
                          UserRepository userRepository,
                          ZoneDateTimeHelper zoneDateTimeHelper) {
        this.packageRepository = packageRepository;
        this.purchasedPackageRepository = purchasedPackageRepository;
        this.userRepository = userRepository;
        this.zoneDateTimeHelper = zoneDateTimeHelper;
    }

    public List<PackageBundle> viewAllPackages(Country country) {
        return packageRepository.findAllByCountry(country);
    }

    public PurchasedPackage purchasePackage(PurchaseRequestDTO purchaseRequestDTO) {

        // check if package and user exists in db
        Optional<PackageBundle> packageinDB = packageRepository.findById(purchaseRequestDTO.getPackageId());
        Optional<User> userInDB = userRepository.findById(purchaseRequestDTO.getUserId());

        if (packageinDB.isEmpty() || userInDB.isEmpty()) throw new NoSuchElementException();

        // check if package country matches with user's country
        if (packageinDB.get().getCountry() != userInDB.get().getCountry()) throw new LocationMismatchException(packageinDB.get().getCountry());

        ZonedDateTime purchaseDate = zoneDateTimeHelper.calculatePurchaseDate(userInDB.get().getCountry());

        ZonedDateTime expiryDate = zoneDateTimeHelper.calculateExpiryDate(packageinDB.get().getCountry(), packageinDB.get().getDurationValue(), packageinDB.get().getDurationType());

        PurchasedPackage purchasedPackage = new PurchasedPackage(
                packageinDB.get().getCredits(),
                purchaseDate,
                expiryDate,
                false,
                packageinDB.get(),
                userInDB.get()
        );

        return purchasedPackageRepository.save(purchasedPackage);
    }

    public List<PurchasedPackage> viewAllPackagesPurchased(Long userId) {

        return purchasedPackageRepository.findAllByUser_UserId(userId);
    }


}
