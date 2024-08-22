package com.stanbic.redbox.debit.service.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//    @Scheduled(fixedRate = 500000)
//    public void reportCurrentTime() {
//        log.info("From Scheduler: The time is now {}", dateFormat.format(new Date()));
//        System.out.println("The time is: " + dateFormat.format(new Date()));
//    }
}
