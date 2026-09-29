package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    // ===== Part B — Built-in =====
    long count();
    Optional findById(Long id);

    // TODO 7
    List findAllOrderByGpaDesc();
    Page findPage(int pageIndex, int size, String sortField);
}
