package com.example.demo.azure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.azure.repo.StudentAzureRepo;
import com.example.demo.azure.entity.StudentEntity;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AzureController {
    @Autowired
    private StudentAzureRepo StudentAzureRepo;

    @GetMapping("/readStudentData")
    public List<StudentEntity> readStudentData() throws Exception {
        return StudentAzureRepo.findAll();
    }
}
