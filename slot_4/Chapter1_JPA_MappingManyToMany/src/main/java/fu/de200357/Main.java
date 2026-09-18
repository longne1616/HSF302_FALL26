package fu.de200357;

import fu.de200357.dao.EmployeeDAO;
import fu.de200357.dao.ProjectDAO;
import fu.de200357.pojo.*;
import fu.de200357.util.JPAUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Employee e = new Employee("test@company.com", "Test", Gender.MALE, new BigDecimal("1000"), LocalDate.now());
        Project p = new Project("PRJ-A", "Website Redesign", new BigDecimal("50000"), LocalDate.now());
        System.out.println(e.getFullName());   // phải in "Test"
        System.out.println(p.getProjectName()); // phải in "Website Redesign"

        Employee e2 = new Employee("test2@company.com", "Test2", Gender.MALE, new BigDecimal("1000"), LocalDate.now());
        System.out.println(e2.getProjects()); // phải in []

        Project p2 = new Project("PRJ-B", "Test Project", new BigDecimal("1000"), LocalDate.now());
        System.out.println(p2.getEmployees()); // phải in []

        Set<Employee> set = new HashSet<>();
        set.add(new Employee("dup@company.com", "A1", Gender.MALE, BigDecimal.ONE, LocalDate.now()));
        set.add(new Employee("dup@company.com", "A2", Gender.FEMALE, BigDecimal.TEN, LocalDate.now()));
        System.out.println(set.size()); // phải 1
    }
}