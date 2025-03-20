package com.example.demo.implementation;

import com.example.demo.azure.repo.StudentAzureRepo;
import com.example.demo.azure.entity.StudentEntity;

import com.example.demo.aws.repo.StudentAwsRepo;
import com.example.demo.aws.entity.StudentAwsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentImpl {
    @Autowired
    private StudentAwsRepo StudentAwsRepo;

    @Autowired
    private StudentAzureRepo StudentAzureRepo;

    public boolean copyDataFromSecondaryToParentDatabase() {

        // Step 1: Truncate StudentAzureRepo
        StudentAzureRepo.deleteAll(); // Deletes all records

        // Step 2: Fetch all students from AWS
        List<StudentAwsEntity> awsStudents = StudentAwsRepo.findAll();

        // Step 3: Convert AWS students to Azure entities
        List<StudentEntity> newAzureStudents = awsStudents.stream()
                .map(student -> {
                    StudentEntity newStudent = new StudentEntity();
                    newStudent.setId(student.getId());
                    newStudent.setName(student.getName());
                    newStudent.setDept(student.getDept());
                    return newStudent; // No ID copied; it will be auto-generated
                })
                .collect(Collectors.toList());

        // Step 4: Insert all AWS students into Azure
        if (!newAzureStudents.isEmpty()) {
            for (StudentEntity studentEntity : newAzureStudents) {
                StudentAzureRepo.insertStudentPrimaryDb(studentEntity.getId(),
                        studentEntity.getName(), studentEntity.getDept());
            }
        }

        return true;
    }
}
