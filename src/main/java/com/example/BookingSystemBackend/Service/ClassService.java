package com.example.BookingSystemBackend.Service;

import com.example.BookingSystemBackend.DTO.BookingRequestDTO;
import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Enum.DurationType;
import com.example.BookingSystemBackend.Exception.*;
import com.example.BookingSystemBackend.Model.*;
import com.example.BookingSystemBackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final PurchasedPackageRepository purchasedPackageRepository;
    private final BookedClassRepository bookedClassRepository;
    private final WaitlistRepository waitlistRepository;

    @Autowired
    public ClassService(ClassRepository classRepository,
                        UserRepository userRepository,
                        PurchasedPackageRepository purchasedPackageRepository,
                        BookedClassRepository bookedClassRepository,
                        WaitlistRepository waitlistRepository) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.purchasedPackageRepository = purchasedPackageRepository;
        this.bookedClassRepository = bookedClassRepository;
        this.waitlistRepository = waitlistRepository;
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
        if (classInDB.get().getAvailableSlots() == 0) throw new NoAvailableSlotsException();

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

    public BookedClass cancelBooking(Long bookedClassId) {

        // check if booking exists
        Optional<BookedClass> bookedClassInDB = bookedClassRepository.findById(bookedClassId);
        if(bookedClassInDB.isEmpty()) throw new NoSuchElementException();

        BookedClass cancelledBooking = bookedClassInDB.get();
        ClassInfo classCancelled = cancelledBooking.getClassBooked();

        // check if class is in the past
        Date classTiming = new Date(classCancelled.getStartTimestamp().getTime());
        Date cancellationTime = new Date();

        if(cancellationTime.after(classTiming)) throw new InvalidTimeException();

        cancelledBooking.setCancelledTimestamp(cancellationTime);
        cancelledBooking.setCancelled(true);

        if (getsRefund(cancelledBooking)) {
            cancelledBooking.setRefunded(true);

            // give refund
            refundCredits(cancelledBooking.getUser(), classCancelled.getCreditsRequired());
        }

        // check if there is a waitlist
        Optional<Waitlist> firstOnWaitlist = waitlistRepository.getFirstOnWaitlist(classCancelled.getClassId());

        if(firstOnWaitlist.isPresent()) {
            Timestamp bookingTimestamp = Timestamp.valueOf(LocalDateTime.now());

            BookedClass bookedClass = new BookedClass(
                    bookingTimestamp,
                    null,
                    false,
                    false,
                    false,
                    firstOnWaitlist.get().getClassWaitlisted(),
                    firstOnWaitlist.get().getUser()
            );
            // first in waitlist gets autobooked to class
            bookedClassRepository.save(bookedClass);

            // remove first on waitlist from the list
            waitlistRepository.deleteById(firstOnWaitlist.get().getWaitlistId());
        } else {
            // if no waitlist, available slots for the class is increased by 1
            classCancelled.setAvailableSlots(classCancelled.getAvailableSlots() + 1);
        }

        return bookedClassRepository.save(cancelledBooking);
    }

    public Waitlist addToWaitlist(BookingRequestDTO bookingRequestDTO) {
        // check if class and user exists in db
        Optional<ClassInfo> classInDB = classRepository.findById(bookingRequestDTO.getClassId());
        Optional<User> userInDB = userRepository.findById(bookingRequestDTO.getUserId());

        if (classInDB.isEmpty() || userInDB.isEmpty()) throw new NoSuchElementException("class or user not found");

        // check if class country matches with user's country
        if (classInDB.get().getCountry() != userInDB.get().getCountry()) throw new LocationMismatchException(classInDB.get().getCountry());

        // check if user has bought a package and the package has not expired yet and there is enough credits
        // sort by expiry_date so that the package that is expiring is first and then return the first entry
        PurchasedPackage packageAvailable = purchasedPackageRepository.findPackageToBookClassWith(bookingRequestDTO.getUserId(), classInDB.get().getCreditsRequired());

        // if no available package, then throw exception
        if (packageAvailable == null) throw new NotEnoughCreditsException();

        // check if user has already booked this class
        boolean hasBookedThisClass =  bookedClassRepository.numberOfBookingsMadeToThisClass(bookingRequestDTO.getUserId(), bookingRequestDTO.getClassId()) >= 1;
        boolean isAlreadyInWaitlist = !waitlistRepository.findAllByUser_UserIdAndClassWaitlisted_ClassId(bookingRequestDTO.getUserId(), bookingRequestDTO.getClassId()).isEmpty();
        if (hasBookedThisClass || isAlreadyInWaitlist) throw new AlreadyBookedClassException();

        // check if class has available slots
        if(classInDB.get().getAvailableSlots() > 0) throw new RuntimeException("Class still has available slots. Book class now.");

        // after passing every check, confirm the booking

        // update the credits remaining in purchased package
        updateCreditsRemaining(packageAvailable, classInDB.get().getCreditsRequired());

        Timestamp waitlistTimestamp = Timestamp.valueOf(LocalDateTime.now());

        Waitlist waitlist = new Waitlist(
                waitlistTimestamp,
                classInDB.get(),
                userInDB.get()
        );
        return waitlistRepository.save(waitlist);
    }

    private void refundCredits(User user, int creditsRefunded) {

        // look for the purchased package that is the last to expire
        Optional<PurchasedPackage> packageToRefund = purchasedPackageRepository.findPackageToRefund(user.getUserId());

        if (packageToRefund.isPresent()) {
            packageToRefund.get().setCreditsRemaining(packageToRefund.get().getCreditsRemaining() + creditsRefunded);

            purchasedPackageRepository.save(packageToRefund.get());
        }
    }

    private boolean getsRefund(BookedClass bookedClass) {
        // check if the cancellation time is 4 hours or more before class start
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookedClass.getCancelledTimestamp());
        calendar.add(Calendar.HOUR, 4);
        Date afterFourHours = calendar.getTime();
        return afterFourHours.before(bookedClass.getClassBooked().getStartTimestamp());
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
