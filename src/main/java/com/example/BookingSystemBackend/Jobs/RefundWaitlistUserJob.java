package com.example.BookingSystemBackend.Jobs;

import com.example.BookingSystemBackend.Model.ClassInfo;
import com.example.BookingSystemBackend.Model.PurchasedPackage;
import com.example.BookingSystemBackend.Model.User;
import com.example.BookingSystemBackend.Model.Waitlist;
import com.example.BookingSystemBackend.Repository.ClassRepository;
import com.example.BookingSystemBackend.Repository.PurchasedPackageRepository;
import com.example.BookingSystemBackend.Repository.WaitlistRepository;
import com.example.BookingSystemBackend.Service.QuartzSchedulerService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class RefundWaitlistUserJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(RefundWaitlistUserJob.class);

    private final ClassRepository classRepository;
    private final WaitlistRepository waitlistRepository;
    private final PurchasedPackageRepository purchasedPackageRepository;

    @Autowired
    public RefundWaitlistUserJob(ClassRepository classRepository,
                                 WaitlistRepository waitlistRepository,
                                 PurchasedPackageRepository purchasedPackageRepository) {
        this.classRepository = classRepository;
        this.waitlistRepository = waitlistRepository;
        this.purchasedPackageRepository = purchasedPackageRepository;
    }

    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Long classId = jobExecutionContext.getJobDetail().getJobDataMap().getLong("classId");
        Optional<ClassInfo> classInfoOptional = classRepository.findById(classId);

        if (classInfoOptional.isPresent()) {
            ClassInfo classInfo = classInfoOptional.get();

            int creditsToRefund = classInfo.getCreditsRequired();

            // Perform update on waitlist and purchasedPackage

            List<Waitlist> waitlistUsers = waitlistRepository.findAllByClassWaitlisted_ClassId(classInfo.getClassId());

            // cycle through each user in waitlist
            for (Waitlist waitlistUser : waitlistUsers) {
                User user = waitlistUser.getUser();

                // refund credits
                refundCredits(user, creditsToRefund);

                // remove user from waitlist
                waitlistRepository.deleteById(waitlistUser.getWaitlistId());
            }
        }
    }
    private void refundCredits(User user, int creditsRefunded) {

        // look for the purchased package that is the last to expire
        Optional<PurchasedPackage> packageToRefund = purchasedPackageRepository.findPackageToRefund(user.getUserId());

        if (packageToRefund.isPresent()) {
            packageToRefund.get().setCreditsRemaining(packageToRefund.get().getCreditsRemaining() + creditsRefunded);

            LOG.info("User " + user.getEmail() + " refunded " + creditsRefunded + " credits.");
            purchasedPackageRepository.save(packageToRefund.get());
        }
    }
}
