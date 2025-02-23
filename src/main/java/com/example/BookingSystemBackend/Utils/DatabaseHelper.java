package com.example.BookingSystemBackend.Utils;

import com.example.BookingSystemBackend.Enum.*;
import com.example.BookingSystemBackend.Model.*;
import com.example.BookingSystemBackend.Model.ClassInfo;
import com.example.BookingSystemBackend.Model.PackageBundle;
import com.example.BookingSystemBackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseHelper {

    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final ClassRepository classRepository;
    private final PurchasedPackageRepository purchasedPackageRepository;
    private final BookedClassRepository bookedClassRepository;
    private final WaitlistRepository waitlistRepository;

    @Autowired
    public DatabaseHelper(
            UserRepository userRepository,
            PackageRepository packageRepository,
            ClassRepository classRepository,
            PurchasedPackageRepository purchasedPackageRepository,
            BookedClassRepository bookedClassRepository,
            WaitlistRepository waitlistRepository
    ) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.classRepository = classRepository;
        this.purchasedPackageRepository = purchasedPackageRepository;
        this.bookedClassRepository = bookedClassRepository;
        this.waitlistRepository = waitlistRepository;
    }

    public void saveUsers() {

        //! encode password after adding security dependency

        userRepository.saveAll(Arrays.asList(
                new User("Howard", "hamlin@gmail.com", "password", Role.CUSTOMER, Country.SINGAPORE),
                new User("Jimmy", "mcgill@gmail.com", "password", Role.CUSTOMER, Country.SINGAPORE),
                new User("Kim", "wexler@gmail.com", "password", Role.CUSTOMER, Country.MYANMAR),
                new User("Chuck", "chuck@gmail.com", "password", Role.CUSTOMER, Country.MYANMAR),
                new User("Mike", "ehrmantraut@gmail.com", "password", Role.CUSTOMER, Country.MYANMAR)
        ));
    }

    public void savePackages() {
        packageRepository.saveAll(Arrays.asList(
                new PackageBundle(5, 15, DurationType.DAY, BigDecimal.valueOf(200), Currency.SGD, PackageType.BASIC, Country.SINGAPORE),
                new PackageBundle(50, 12, DurationType.MONTH, BigDecimal.valueOf(1900), Currency.SGD, PackageType.ULTIMATE, Country.SINGAPORE),
                new PackageBundle(5, 15, DurationType.DAY, BigDecimal.valueOf(30_000), Currency.MMK, PackageType.BASIC, Country.MYANMAR),
                new PackageBundle(50, 12, DurationType.MONTH, BigDecimal.valueOf(285_000), Currency.MMK, PackageType.ULTIMATE, Country.MYANMAR)
        ));
    }

    public void saveClasses() {
        classRepository.saveAll(Arrays.asList(
                new ClassInfo(Timestamp.valueOf("2025-02-24 18:00:00"), 1, 1, 10, 10, ClassType.YOGA, Country.SINGAPORE),
                new ClassInfo(Timestamp.valueOf("2025-02-24 17:00:00"), 2, 2, 10, 1, ClassType.PILATES, Country.SINGAPORE),
                new ClassInfo(Timestamp.valueOf("2025-02-24 18:00:00"), 1, 1, 10, 8, ClassType.YOGA, Country.MYANMAR),
                new ClassInfo(Timestamp.valueOf("2025-02-24 17:00:00"), 2, 2, 10, 0, ClassType.PILATES, Country.MYANMAR)
        ));
    }

    public void savePurchasedPackages() {

        List<User> allUsers = userRepository.findAll();
        System.out.println("Line 80: " + allUsers);

        List<PackageBundle> allPackages = packageRepository.findAll();
        System.out.println("Line 77: " + allPackages);

        purchasedPackageRepository.saveAll(Arrays.asList(
                new PurchasedPackage(40, Timestamp.valueOf("2025-02-20 17:00:00"), Timestamp.valueOf("2026-02-20 17:00:00"), false, allPackages.get(1), allUsers.get(0)),
//                new PurchasedPackage(2, Timestamp.valueOf("2025-02-18 13:00:00"), Timestamp.valueOf("2025-03-05 13:00:00"), false, allPackages.get(0), allUsers.get(1)),
                new PurchasedPackage(2, Timestamp.valueOf("2025-02-18 12:00:00"), Timestamp.valueOf("2025-03-05 12:00:00"), false, allPackages.get(2), allUsers.get(2))
//                new PurchasedPackage(2, Timestamp.valueOf("2025-02-21 17:00:00"), Timestamp.valueOf("2025-03-08 17:00:00"), false, allPackages.get(2), allUsers.get(3)),
//                new PurchasedPackage(2, Timestamp.valueOf("2025-02-22 19:00:00"), Timestamp.valueOf("2025-03-09 19:00:00"), false, allPackages.get(2), allUsers.get(4))
        ));
    }

    public void saveBookedClasses() {

        List<User> allUsers = userRepository.findAll();

        List<ClassInfo> allClasses = classRepository.findAll();

        bookedClassRepository.saveAll(Arrays.asList(
                new BookedClass(Timestamp.valueOf("2025-02-20 19:00:00"), Timestamp.valueOf("2025-02-21 15:00:00"), true, true, false, allClasses.get(0), allUsers.get(0)),
                new BookedClass(Timestamp.valueOf("2025-02-21 20:00:00"), null, false, false, false, allClasses.get(1), allUsers.get(0)),
                new BookedClass(Timestamp.valueOf("2025-02-22 15:00:00"), null, false, false, false, allClasses.get(3), allUsers.get(2))
        ));
    }

    public void saveWaitlist() {

        List<User> allUsers = userRepository.findAll();

        List<ClassInfo> allClasses = classRepository.findAll();

        waitlistRepository.saveAll(Arrays.asList(
                new Waitlist(Timestamp.valueOf("2025-02-22 21:00:00"), allClasses.get(3), allUsers.get(3)),
                new Waitlist(Timestamp.valueOf("2025-02-23 09:00:00"), allClasses.get(3), allUsers.get(4))
        ));
    }
}
