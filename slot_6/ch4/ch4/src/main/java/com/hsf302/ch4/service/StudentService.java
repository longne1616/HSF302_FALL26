package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    // ===== Part B — Built-in =====
    long count();
    Optional<Student> findById(Long id);

    // TODO 7
    List<Student> findAllOrderByGpaDesc();
    Page<Student> findPage(int pageIndex, int size, String sortField);

    // TODO 8
    Optional<Student> findByStudentCode(String studentCode);
    boolean isEmailExisted(String email);
    long countActive();
    // TODO 9
    List<Student> searchByName(String keyword);
    List<Student> findByEmailDomain(String domain);
    List<Student> findWithoutEmail();
    // TODO 11
    List<Student> findByDepartment(String deptCode);
    long countByDepartment(String deptCode);
    List<Student> findTop3ByGpa();
    // TODO 10
    List<Student> findByGpaRange(double min, double max);
    List<Student> findActiveByGender(Gender gender);
    List<Student> findBornAfter(LocalDate date);
    List<Student> findGoodStudents(String deptCode, double minGpa);

    List<Student> searchByKeyword(String keyword);

    List<Student> findAboveAverageGpa();
    List<Student> findTopNInDepartment(String deptCode, int n);
}
