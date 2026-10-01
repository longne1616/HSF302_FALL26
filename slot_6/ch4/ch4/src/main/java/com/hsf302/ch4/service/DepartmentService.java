package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.DepartmentStatDTO;
import com.hsf302.ch4.pojo.Department;

import java.util.List;

public interface DepartmentService {
    long count();
    boolean existsById(Long id);
    List<Department> findDepartmentsWithoutStudents();
    List<DepartmentStatDTO> getStatistics();
}
