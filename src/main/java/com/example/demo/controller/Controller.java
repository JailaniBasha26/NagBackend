package com.example.demo.controller;

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

    @GetMapping("/readStudentData")
    public List<StudentEntity> readStudentDataCommon() throws Exception {
        return StudentAzureRepo.findAll();
    }

    @GetMapping("/readStudentDataAws")
    public List<StudentAwsEntity> readStudentDataAwsCommon() throws Exception {
        return StudentAwsRepo.findAll();
    }

    @PostMapping("/createStudentData")
    public Boolean createStudentData(@RequestBody StudentEntity studentDetail) throws Exception {
        try {
            StudentAzureRepo.save(studentDetail);
        } catch (Exception e) {
        }

        StudentAwsEntity studentAwsEntity = new StudentAwsEntity();
        studentAwsEntity.setName(studentDetail.getName());
        studentAwsEntity.setDept(studentDetail.getDept());
        try {
            StudentAwsRepo.save(studentAwsEntity);
        } catch (Exception e) {
        }

        return true;
    }

}
