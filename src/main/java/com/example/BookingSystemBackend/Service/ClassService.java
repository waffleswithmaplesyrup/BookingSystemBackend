package com.example.BookingSystemBackend.Service;

import com.example.BookingSystemBackend.DTO.BookingRequestDTO;
import com.example.BookingSystemBackend.DTO.NewClassRequestDTO;
import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Exception.*;
import com.example.BookingSystemBackend.Model.*;
import com.example.BookingSystemBackend.Repository.*;
import com.example.BookingSystemBackend.Utils.ZoneDateTimeHelper;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final PurchasedPackageRepository purchasedPackageRepository;
    private final BookedClassRepository bookedClassRepository;
    private final WaitlistRepository waitlistRepository;
    private final ZoneDateTimeHelper zoneDateTimeHelper;
    private final QuartzSchedulerService quartzSchedulerService;

    @Autowired
    public ClassService(ClassRepository classRepository,
                        UserRepository userRepository,
                        PurchasedPackageRepository purchasedPackageRepository,
                        BookedClassRepository bookedClassRepository,
                        WaitlistRepository waitlistRepository,
                        ZoneDateTimeHelper zoneDateTimeHelper,
                        QuartzSchedulerService quartzSchedulerService) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.purchasedPackageRepository = purchasedPackageRepository;
        this.bookedClassRepository = bookedClassRepository;
        this.waitlistRepository = waitlistRepository;
        this.zoneDateTimeHelper = zoneDateTimeHelper;
        this.quartzSchedulerService = quartzSchedulerService;
    }

    public ClassInfo createNewClassSchedule(NewClassRequestDTO newClassRequestDTO) {

        ZonedDateTime startTime = zoneDateTimeHelper.convertStringToZonedDateTime(newClassRequestDTO.getStartTime(), newClassRequestDTO.getCountry());
        ZonedDateTime endTime = zoneDateTimeHelper.convertStringToZonedDateTime(newClassRequestDTO.getEndTime(), newClassRequestDTO.getCountry());


        ClassInfo newClass = new ClassInfo(
                startTime,
                endTime,
                newClassRequestDTO.getDuration(),
                newClassRequestDTO.getCreditsRequired(),
                newClassRequestDTO.getClassSize(),
                newClassRequestDTO.getClassSize(),
                newClassRequestDTO.getClassType(),
                newClassRequestDTO.getCountry()
        );

        ClassInfo savedClass = classRepository.save(newClass);

        try {
            quartzSchedulerService.scheduleRefundJob(savedClass.getClassId(), savedClass.getEndTime());

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return savedClass;
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

        // check if user has other classes that overlap with the timing
        int overlappingClasses = bookedClassRepository.overlappingClasses(userInDB.get().getUserId(), classInDB.get().getStartTime(), classInDB.get().getEndTime());

        if(overlappingClasses > 0) throw new RuntimeException("class schedule clash with another class");

        // check if class still have available slots
        if (classInDB.get().getAvailableSlots() == 0) throw new NoAvailableSlotsException();

        // after passing every check, confirm the booking

        // update the class available slot
        updateAvailableSlot(classInDB.get());

        // update the credits remaining in purchased package
        updateCreditsRemaining(packageAvailable, classInDB.get().getCreditsRequired());

        ZonedDateTime bookingTimestamp = ZonedDateTime.now();

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

        ZonedDateTime classTiming = classCancelled.getStartTime();
        ZonedDateTime cancellationTime = ZonedDateTime.now();

        if(cancellationTime.isAfter(classTiming)) throw new InvalidTimeException();

        cancelledBooking.setCancelledTime(cancellationTime);
        cancelledBooking.setCancelled(true);

        if (getsRefund(cancelledBooking)) {
            cancelledBooking.setRefunded(true);

            // give refund
            refundCredits(cancelledBooking.getUser(), classCancelled.getCreditsRequired());
        }

        // check if there is a waitlist
        Optional<Waitlist> firstOnWaitlist = waitlistRepository.getFirstOnWaitlist(classCancelled.getClassId());

        if(firstOnWaitlist.isPresent()) {
            ZonedDateTime bookingTimestamp = ZonedDateTime.now();

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

        // check if user has other classes that overlap with the timing
        int overlappingClasses = bookedClassRepository.overlappingClasses(userInDB.get().getUserId(), classInDB.get().getStartTime(), classInDB.get().getEndTime());

        if(overlappingClasses > 0) throw new RuntimeException("class schedule clash with another class");

        // after passing every check, confirm the booking

        // update the credits remaining in purchased package
        updateCreditsRemaining(packageAvailable, classInDB.get().getCreditsRequired());

        LocalDateTime waitlistTimestamp = LocalDateTime.now();

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
        long hoursBeforeClassStarts = Duration.between(bookedClass.getCancelledTime(), bookedClass.getClassBooked().getStartTime()).toHours();
        return hoursBeforeClassStarts >= 4;
    }


    private void updateAvailableSlot(ClassInfo classInfo) {

        classInfo.setAvailableSlots(classInfo.getAvailableSlots()-1);

        classRepository.save(classInfo);
    }

    private void updateCreditsRemaining(PurchasedPackage purchasedPackage, int creditsRequired) {

        purchasedPackage.setCreditsRemaining(purchasedPackage.getCreditsRemaining()-creditsRequired);

        purchasedPackageRepository.save(purchasedPackage);
    }

    public BookedClass checkInToClass(Long bookedClassId) {
        // check if booking exists
        Optional<BookedClass> bookedClassInDB = bookedClassRepository.findById(bookedClassId);
        if(bookedClassInDB.isEmpty()) throw new NoSuchElementException("class or user not found");

        BookedClass checkInClass = bookedClassInDB.get();
        ClassInfo classInfo = checkInClass.getClassBooked();

        // cannot check in to class that user has cancelled
        if (checkInClass.isCancelled()) throw new RuntimeException("cannot check in to cancelled class");

        ZonedDateTime startTime = classInfo.getStartTime();
        ZonedDateTime checkInTime = ZonedDateTime.now();

        // cannot check in after class starts
        if(checkInTime.isAfter(startTime)) throw new RuntimeException("cannot check in after class starts");

        // cannot check in more than 30 minutes before class starts
        if(Duration.between(checkInTime, startTime).toMinutes() > 30) throw new RuntimeException("check in earliest 30 minutes before class");

        checkInClass.setCheckedIn(true);

        return bookedClassRepository.save(checkInClass);
    }
}
