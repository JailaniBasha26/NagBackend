package com.example.demo.azure.repo;

import com.example.demo.azure.entity.StudentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAzureRepo extends JpaRepository<StudentEntity, Integer> {

    @Modifying
    @Query(value = "SET IDENTITY_INSERT student ON; INSERT INTO student (id, name, dept) VALUES (?1,?2,?3);SET IDENTITY_INSERT student OFF;", nativeQuery = true)
    @Transactional
    void insertStudentPrimaryDb(Integer id, String name, String dept);

}
