package com.hsf302.Chapter_6.controller;

import com.hsf302.Chapter_6.entity.Student;
import com.hsf302.Chapter_6.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final List<String> MAJORS = List.of("CNTT", "KTPM", "ATTT", "HTTT");

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ===== LIST + SEARCH =====
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String major,
                       Model model) {
        model.addAttribute("students", studentService.search(keyword, major));
        model.addAttribute("majors", MAJORS);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedMajor", major);
        return "Students/list";
    }

    // ===== DETAIL =====
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "Students/detail";
    }

    // ===== CREATE =====
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("majors", MAJORS);
        model.addAttribute("formTitle", "Thêm sinh viên");
        return "Students/form";
    }

    // ===== EDIT =====
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("majors", MAJORS);
        model.addAttribute("formTitle", "Sửa sinh viên");
        return "Students/form";
    }

    // ===== SAVE (dùng chung cho thêm và sửa) =====
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student,
                       BindingResult result,
                       Model model,
                       RedirectAttributes ra) {

        // kiểm tra trùng email (id == null là thêm mới)
        if (student.getEmail() != null
                && studentService.isEmailTaken(student.getEmail(), student.getId())) {
            result.rejectValue("email", "duplicate", "Email đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("majors", MAJORS);
            model.addAttribute("formTitle", student.getId() == null ? "Thêm sinh viên" : "Sửa sinh viên");
            return "Students/form";   // trả về form, giữ nguyên dữ liệu + lỗi
        }

        boolean isNew = student.getId() == null;
        studentService.save(student);
        ra.addFlashAttribute("message", isNew ? "Thêm sinh viên thành công" : "Cập nhật thành công");
        return "redirect:/students";
    }

    // ===== DELETE =====
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        studentService.deleteById(id);
        ra.addFlashAttribute("message", "Đã xóa sinh viên");
        return "redirect:/students";
    }

    // ===== TOP GPA =====
    @GetMapping("/top")
    public String top(Model model) {
        model.addAttribute("students", studentService.findTopByGpa());
        model.addAttribute("majors", MAJORS);
        return "Students/list";
    }
}