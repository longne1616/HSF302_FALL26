package com.hsf302.Chapter_6.service;

import com.hsf302.Chapter_6.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();
    Student findById(Long id);          // không thấy thì ném exception
    Student save(Student student);      // dùng cho cả thêm mới và cập nhật
    void deleteById(Long id);

    boolean isEmailTaken(String email, Long currentId);

    List<Student> search(String keyword, String major);
    List<Student> findTopByGpa();
}