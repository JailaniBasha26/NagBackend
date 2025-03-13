package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private StudentRepo studentRepo;

    @PostMapping("/createStudentData")
    public StudentEntity createStudentData(@RequestBody StudentEntity studentDetail) throws Exception {
        return studentRepo.save(studentDetail);
    }

    @GetMapping("/readStudentData")
    public List<StudentEntity> readStudentData() throws Exception {
        return studentRepo.findAll();
    }

//    @PostMapping("/updateStudent")
//    public StudentEntity updateStudentName(@RequestParam Integer id, @RequestParam String updateValue,
//                                           @RequestParam String update) throws Exception {
//        StudentEntity student = studentRepo.findById(id).orElse(null);
//        if (student != null) {
//            if (update.equalsIgnoreCase("name")) {
//                student.setName(updateValue);
//            } else if (update.equalsIgnoreCase("dept")) {
//                student.setDept(updateValue);
//            }
//            studentRepo.save(student);
//            return student;
//        }
//        return null;
//    }

    @PostMapping("/updateStudent")
    public StudentEntity updateStudentName(@RequestBody StudentEntity studentEntity) throws Exception {
        StudentEntity student = studentRepo.findById(studentEntity.getId()).orElse(null);
        if (student != null) {
            student.setName(studentEntity.getName());
            student.setDept(studentEntity.getDept());
            studentRepo.save(student);
            return student;
        } else
            return null;
    }

    @DeleteMapping("/deleteStudentData")
    public Boolean deleteStudentData(@RequestParam Integer id) throws Exception {
        if (studentRepo.existsById(id)) {
            studentRepo.deleteById(id);
            return true;
        } else
            return false;
    }
}
