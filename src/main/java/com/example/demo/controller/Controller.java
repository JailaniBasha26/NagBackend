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

//        if (healthCheckService.isDatabaseUp()) {
//            return StudentAzureRepo.findAll();
//        } else {
//            return StudentAwsRepo.findAll();
//        }

        if (schedulerService.currentHealthStatus()) {
            if (healthCheckService.isDatabaseUp())
                return StudentAzureRepo.findAll();
            else {
                schedulerService.scheduleMonitorTask();
                return StudentAwsRepo.findAll();
            }
        } else {
            return StudentAwsRepo.findAll();
        }
    }

    @PostMapping("/createStudentData")
    public Object createStudentData(@RequestBody StudentEntity studentDetail) throws Exception {
        Object result = new Object();

//        if (healthCheckService.isDatabaseUp()) {
//            try {
//                result = StudentAzureRepo.save(studentDetail);
//            } catch (Exception e) {
//            }
//        } else {
//            schedulerService.scheduleMonitorTask();
//        }
//
//        StudentAwsEntity studentAwsEntity = new StudentAwsEntity();
//        studentAwsEntity.setName(studentDetail.getName());
//        studentAwsEntity.setDept(studentDetail.getDept());
//
//        try {
//            result = StudentAwsRepo.save(studentAwsEntity);
//        } catch (Exception e) {
//        }

        if (schedulerService.currentHealthStatus()) {
            if (healthCheckService.isDatabaseUp()) {
                try {
                    result = StudentAzureRepo.save(studentDetail);

                    StudentAwsEntity studentAwsEntity3 = new StudentAwsEntity();
                    studentAwsEntity3.setName(studentDetail.getName());
                    studentAwsEntity3.setDept(studentDetail.getDept());

                    try {
                        result = StudentAwsRepo.save(studentAwsEntity3);
                    } catch (Exception e) {
                    }

                } catch (Exception e) {
                }
            } else {
                schedulerService.scheduleMonitorTask();
                StudentAwsEntity studentAwsEntity2 = new StudentAwsEntity();
                studentAwsEntity2.setName(studentDetail.getName());
                studentAwsEntity2.setDept(studentDetail.getDept());
                try {
                    result = StudentAwsRepo.save(studentAwsEntity2);
                } catch (Exception e) {
                }
            }
        } else {
//            schedulerService.scheduleMonitorTask();
            StudentAwsEntity studentAwsEntity2 = new StudentAwsEntity();
            studentAwsEntity2.setName(studentDetail.getName());
            studentAwsEntity2.setDept(studentDetail.getDept());
            try {
                result = StudentAwsRepo.save(studentAwsEntity2);
            } catch (Exception e2) {
            }
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

    @PostMapping("/updateStudent")
    public Object updateStudentName(@RequestBody StudentEntity studentEntity) throws Exception {

//        if (healthCheckService.isDatabaseUp()) {
//            try {
//                StudentEntity student = null;
//                student = StudentAzureRepo.findById(studentEntity.getId()).orElse(null);
//                if (student != null) {
//                    student.setName(studentEntity.getName());
//                    student.setDept(studentEntity.getDept());
//                    StudentAzureRepo.save(student);
//                }
//            } catch (Exception e) {
//            }
//        } else {
//            schedulerService.scheduleMonitorTask();
//        }
//
//        StudentAwsEntity studentAwsEntity = new StudentAwsEntity();
//        studentAwsEntity = StudentAwsRepo.findById(studentEntity.getId()).orElse(null);
//        if (null != studentAwsEntity) {
//            studentAwsEntity.setName(studentEntity.getName());
//            studentAwsEntity.setDept(studentEntity.getDept());
//            StudentAwsRepo.save(studentAwsEntity);
//        }
///////////
        if (schedulerService.currentHealthStatus()) {
            if (healthCheckService.isDatabaseUp()) {
                try {
                    StudentEntity student = null;
                    student = StudentAzureRepo.findById(studentEntity.getId()).orElse(null);
                    if (student != null) {
                        student.setName(studentEntity.getName());
                        student.setDept(studentEntity.getDept());
                        StudentAzureRepo.save(student);
                    }

                    StudentAwsEntity studentAwsEntity2 = new StudentAwsEntity();
                    studentAwsEntity2 = StudentAwsRepo.findById(studentEntity.getId()).orElse(null);
                    if (null != studentAwsEntity2) {
                        studentAwsEntity2.setName(studentEntity.getName());
                        studentAwsEntity2.setDept(studentEntity.getDept());
                        StudentAwsRepo.save(studentAwsEntity2);
                    }

                    return studentAwsEntity2;
                } catch (Exception e) {
                    return null;
                }
            } else {
                schedulerService.scheduleMonitorTask();
                StudentAwsEntity studentAwsEntity4 = new StudentAwsEntity();
                studentAwsEntity4 = StudentAwsRepo.findById(studentEntity.getId()).orElse(null);
                if (null != studentAwsEntity4) {
                    studentAwsEntity4.setName(studentEntity.getName());
                    studentAwsEntity4.setDept(studentEntity.getDept());
                    StudentAwsRepo.save(studentAwsEntity4);
                }

                return studentAwsEntity4;
            }
        } else {
//            schedulerService.scheduleMonitorTask();
            StudentAwsEntity studentAwsEntity4 = new StudentAwsEntity();
            studentAwsEntity4 = StudentAwsRepo.findById(studentEntity.getId()).orElse(null);
            if (null != studentAwsEntity4) {
                studentAwsEntity4.setName(studentEntity.getName());
                studentAwsEntity4.setDept(studentEntity.getDept());
                StudentAwsRepo.save(studentAwsEntity4);
            }

            return studentAwsEntity4;
        }
    }

    @DeleteMapping("/deleteStudentData")
    public Boolean deleteStudentData(@RequestParam Integer id) throws Exception {
        Boolean status = true;

//        if (healthCheckService.isDatabaseUp()) {
//            try {
//                StudentAzureRepo.deleteById(id);
//            } catch (Exception e) {
//
//            }
//        } else {
//            schedulerService.scheduleMonitorTask();
//        }
//
//        StudentAwsRepo.deleteById(id);

        if (schedulerService.currentHealthStatus()) {
            if (healthCheckService.isDatabaseUp()) {
                StudentAzureRepo.deleteById(id);
                StudentAwsRepo.deleteById(id);
            } else {
                schedulerService.scheduleMonitorTask();
                StudentAwsRepo.deleteById(id);
            }
        } else {
            StudentAwsRepo.deleteById(id);
        }


        return status;
    }
}
