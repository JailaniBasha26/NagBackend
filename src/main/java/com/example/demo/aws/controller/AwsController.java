package com.example.demo.aws.controller;

import com.example.demo.azure.entity.StudentEntity;
import com.example.demo.azure.repo.StudentAzureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.aws.repo.StudentAwsRepo;
import com.example.demo.aws.entity.StudentAwsEntity;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AwsController {
    @Autowired
    private StudentAwsRepo StudentAwsRepo;

    @GetMapping("/readStudentDataAws")
    public List<StudentAwsEntity> readStudentData() throws Exception {
        return StudentAwsRepo.findAll();
    }
}
