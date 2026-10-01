package com.hsf302.ch4.dto;

public class DepartmentStatDTO {

    private String code;
    private String name;
    private long totalStudents;
    private Double avgGpa;

    public DepartmentStatDTO(String code, String name, Long totalStudents, Double avgGpa) {
        this.code = code;
        this.name = name;
        this.totalStudents = totalStudents;
        this.avgGpa = avgGpa;
    }
    @Override
    public String toString() {
        return String.format("%-3s | %-25s | %2d | %s",
                code, name, totalStudents,
                avgGpa == null ? "null" : String.format("%.3f", avgGpa));
    }
}