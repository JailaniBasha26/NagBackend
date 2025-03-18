package com.example.demo.azure.repo;

import com.example.demo.azure.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAzureRepo extends JpaRepository<StudentEntity, Integer> {
}
