package com.hsf302.Chapter_6.service.impl;

import com.hsf302.Chapter_6.entity.Student;
import com.hsf302.Chapter_6.repository.StudentRepository;
import com.hsf302.Chapter_6.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    // constructor injection (không cần @Autowired)
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên id = " + id));
    }

    @Override
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên id = " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public boolean isEmailTaken(String email, Long currentId) {
        if (currentId == null) {                       // đang THÊM MỚI
            return studentRepository.existsByEmail(email);
        }
        return studentRepository.existsByEmailAndIdNot(email, currentId); // đang SỬA
    }

    @Override
    public List<Student> search(String keyword, String major) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasMajor = major != null && !major.isBlank();

        if (hasKeyword && hasMajor) {
            return studentRepository.findByNameContainingIgnoreCase(keyword.trim())
                    .stream()
                    .filter(s -> s.getMajor().equalsIgnoreCase(major))
                    .toList();
        }
        if (hasKeyword) return studentRepository.findByNameContainingIgnoreCase(keyword.trim());
        if (hasMajor)   return studentRepository.findByMajor(major);
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findTopByGpa() {
        return studentRepository.findAllByOrderByGpaDesc();
    }
}