package com.example.demo.controller;

import com.example.demo.config.DatabaseHealthCheckService;
import com.example.demo.scheduling.DynamicSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.example.demo.azure.repo.StudentAzureRepo;
import com.example.demo.azure.entity.StudentEntity;

import com.example.demo.aws.repo.StudentAwsRepo;
import com.example.demo.aws.entity.StudentAwsEntity;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class Controller {

    @Autowired
    private StudentAwsRepo StudentAwsRepo;

    @Autowired
    private StudentAzureRepo StudentAzureRepo;

    @Autowired
    private DynamicSchedulerService schedulerService;

    @Autowired
    private DatabaseHealthCheckService healthCheckService;

    @GetMapping("/readStudentData")
    public List<StudentEntity> readStudentDataCommon() throws Exception {
        return StudentAzureRepo.findAll();
    }

    @GetMapping("/readStudentDataAws")
    public List<StudentAwsEntity> readStudentDataAwsCommon() throws Exception {
        return StudentAwsRepo.findAll();
    }

    @GetMapping("/getStudentData")
    public List<?> getStudentData() throws Exception {

        if (healthCheckService.isDatabaseUp()) {
            return StudentAzureRepo.findAll();
        } else {
            return StudentAwsRepo.findAll();
        }
    }

    @PostMapping("/createStudentData")
    public Object createStudentData(@RequestBody StudentEntity studentDetail) throws Exception {
        Object result = new Object();
        Boolean isPrimaryDatabaseDown = false;

        if (healthCheckService.isDatabaseUp()) {
            try {
                result = StudentAzureRepo.save(studentDetail);
            } catch (Exception e) {
            }
        } else {
            schedulerService.scheduleMonitorTask();
            isPrimaryDatabaseDown = true;
        }

        StudentAwsEntity studentAwsEntity = new StudentAwsEntity();
        studentAwsEntity.setName(studentDetail.getName());
        studentAwsEntity.setDept(studentDetail.getDept());

        try {
            result = StudentAwsRepo.save(studentAwsEntity);
        } catch (Exception e) {
        }

        return result;
    }

    @PostMapping("/insert")
    public String insertData(@RequestParam boolean shouldSchedule) {
        if (!shouldSchedule) {
            schedulerService.scheduleMonitorTask();
            return "Monitor task scheduled every 30 minutes.";
        } else {
            schedulerService.cancelScheduledTask();
            return "Monitor task was not scheduled.";
        }
    }

    @GetMapping("/scheduleMonitor")
    public String scheduleMonitor() {
        System.out.println("Executing scheduled monitor task...");
        return "Monitor task executed.";
    }
}
