package com.example.BookingSystemBackend.Service;

import com.example.BookingSystemBackend.Jobs.RefundWaitlistUserJob;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class QuartzSchedulerService {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzSchedulerService.class);
    private final Scheduler scheduler;

    @Autowired
    public QuartzSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }

    public void scheduleRefundJob(Long classId, ZonedDateTime endTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(RefundWaitlistUserJob.class)
                .withIdentity("refundJob-" + classId)
                .usingJobData("classId", classId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger-" + classId)
                .startAt(java.util.Date.from(endTime.toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
