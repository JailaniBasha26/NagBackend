package com.example.demo.aws.repo;

import com.example.demo.aws.entity.StudentAwsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

//@Repository
public interface StudentAwsRepo extends JpaRepository<StudentAwsEntity, Integer> {

    @Modifying
    @Query(value = "INSERT INTO student (id, name, dept) VALUES (?1,?2,?3)", nativeQuery = true)
    @Transactional
    void insertStudentSecondaryDb(Integer id, String name, String dept);

}
