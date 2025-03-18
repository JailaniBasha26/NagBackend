package com.example.demo.aws.repo;

import com.example.demo.aws.entity.StudentAwsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAwsRepo extends JpaRepository<StudentAwsEntity, Integer> {
}
