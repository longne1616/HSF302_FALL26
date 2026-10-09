package com.hsf302.Chapter_6.repository;

import com.hsf302.Chapter_6.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // --- Các query cơ bản ---
    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    // dùng khi EDIT: email trùng nhưng phải là của sinh viên KHÁC
    boolean existsByEmailAndIdNot(String email, Long id);

    List<Student> findByMajor(String major);

    List<Student> findByNameContainingIgnoreCase(String keyword);

    List<Student> findByGpaGreaterThanEqual(Double gpa);

    List<Student> findByAgeBetween(Integer from, Integer to);

    // sắp xếp GPA giảm dần
    List<Student> findAllByOrderByGpaDesc();
}