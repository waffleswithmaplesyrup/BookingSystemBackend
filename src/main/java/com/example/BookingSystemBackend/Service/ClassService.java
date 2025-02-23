package com.example.BookingSystemBackend.Service;

import com.example.BookingSystemBackend.DTO.BookingRequestDTO;
import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Exception.AlreadyBookedClassException;
import com.example.BookingSystemBackend.Exception.LocationMismatchException;
import com.example.BookingSystemBackend.Exception.NoAvailableSlotsException;
import com.example.BookingSystemBackend.Exception.NotEnoughCreditsException;
import com.example.BookingSystemBackend.Model.*;
import com.example.BookingSystemBackend.Repository.BookedClassRepository;
import com.example.BookingSystemBackend.Repository.ClassRepository;
import com.example.BookingSystemBackend.Repository.PurchasedPackageRepository;
import com.example.BookingSystemBackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final PurchasedPackageRepository purchasedPackageRepository;
    private final BookedClassRepository bookedClassRepository;

    @Autowired
    public ClassService(ClassRepository classRepository,
                        UserRepository userRepository,
                        PurchasedPackageRepository purchasedPackageRepository,
                        BookedClassRepository bookedClassRepository) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.purchasedPackageRepository = purchasedPackageRepository;
        this.bookedClassRepository = bookedClassRepository;
    }

    public List<ClassInfo> viewAllClasses(Country country) {
        return classRepository.findAllByCountry(country);
    }

    public BookedClass bookClass(BookingRequestDTO bookingRequestDTO) {

        // check if class and user exists in db
        Optional<ClassInfo> classInDB = classRepository.findById(bookingRequestDTO.getClassId());
        Optional<User> userInDB = userRepository.findById(bookingRequestDTO.getUserId());

        if (classInDB.isEmpty() || userInDB.isEmpty()) throw new NoSuchElementException();

        // check if class country matches with user's country
        if (classInDB.get().getCountry() != userInDB.get().getCountry()) throw new LocationMismatchException(classInDB.get().getCountry());

        // check if user has bought a package and the package has not expired yet and there is enough credits
        // sort by expiry_date so that the package that is expiring is first and then return the first entry
        PurchasedPackage packageAvailable = purchasedPackageRepository.findPackageToBookClassWith(bookingRequestDTO.getUserId(), classInDB.get().getCreditsRequired());

        // if no available package, then throw exception
        if (packageAvailable == null) throw new NotEnoughCreditsException();

        // check if user has already booked this class
        boolean hasBookedThisClass =  bookedClassRepository.numberOfBookingsMadeToThisClass(bookingRequestDTO.getUserId(), bookingRequestDTO.getClassId()) >= 1;
        if (hasBookedThisClass) throw new AlreadyBookedClassException();

        // check if class still have available slots
        if (classInDB.get().getAvailableSlots() == 0) {
            // add user to waitlist

            throw new NoAvailableSlotsException();
        }

        // after passing every check, confirm the booking

        // update the class available slot
        updateAvailableSlot(classInDB.get());

        // update the credits remaining in purchased package
        updateCreditsRemaining(packageAvailable, classInDB.get().getCreditsRequired());

        Timestamp bookingTimestamp = Timestamp.valueOf(LocalDateTime.now());

        BookedClass bookedClass = new BookedClass(
                bookingTimestamp,
                null,
                false,
                false,
                false,
                classInDB.get(),
                userInDB.get()
        );
        return bookedClassRepository.save(bookedClass);
    }

    private void updateAvailableSlot(ClassInfo classInfo) {

        classInfo.setAvailableSlots(classInfo.getAvailableSlots()-1);

        classRepository.save(classInfo);
    }

    private void updateCreditsRemaining(PurchasedPackage purchasedPackage, int creditsRequired) {

        purchasedPackage.setCreditsRemaining(purchasedPackage.getCreditsRemaining()-creditsRequired);

        purchasedPackageRepository.save(purchasedPackage);
    }
}
