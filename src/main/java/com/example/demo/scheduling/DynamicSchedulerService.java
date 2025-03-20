package com.example.demo.scheduling;

import com.example.demo.config.DatabaseHealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.example.demo.implementation.StudentImpl;

@Service
public class DynamicSchedulerService {

    @Autowired
    private DatabaseHealthCheckService healthCheckService;

    @Autowired
    private StudentImpl studentImpl;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;
    private final RestTemplate restTemplate;
    private boolean IsScheduleRunning = false;
    private boolean currentHealthStatus = true;

    public DynamicSchedulerService() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        this.taskScheduler = scheduler;
        this.restTemplate = new RestTemplate();
    }

    public void scheduleMonitorTask() {
        // Cancel any existing scheduled task
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
//        if (!IsScheduleRunning) {
        // Schedule the new task to run every 30 minutes
        scheduledFuture = taskScheduler.scheduleAtFixedRate(() -> {
            System.out.println("Calling /scheduleMonitor API...");
            IsScheduleRunning = true;
            currentHealthStatus = false;
            try {
//                restTemplate.getForObject("http://localhost:8080/scheduleMonitor", String.class);
                Boolean healthCheckStatus = healthCheckService.isDatabaseUp();

                if (healthCheckStatus && !currentHealthStatus) {
                    Boolean status = false;
                    status = studentImpl.copyDataFromSecondaryToParentDatabase();

                    if (status) {
                        currentHealthStatus = true;
                        scheduledFuture.cancel(false);
//                        cancelScheduledTask();
                        System.out.println("Scheduled task cancelled.");
                    }
                }

                /*
                if healthCheckStatus is true,
                    copy the student data from japan to us
                * */
                System.out.println("AUTO CALL >>> " + healthCheckStatus);

            } catch (Exception e) {
                System.out.println("Error calling /scheduleMonitor: " + e.getMessage());
            }
//        }, TimeUnit.MINUTES.toMillis(30)); // 30 minutes
        }, TimeUnit.SECONDS.toMillis(5));  // Updated interval to 5 seconds
//        }
    }

    public void cancelScheduledTask() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
            IsScheduleRunning = false;
            System.out.println("Scheduled task cancelled.");
        }
    }

    public boolean currentHealthStatus() {
        return currentHealthStatus;
    }
}
