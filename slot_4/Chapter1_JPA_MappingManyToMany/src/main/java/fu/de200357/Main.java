package fu.de200357;

import fu.de200357.pojo.Employee;
import fu.de200357.pojo.Project;
import fu.de200357.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Employee e = new Employee("test@company.com", "Test", Gender.MALE, new BigDecimal("1000"), LocalDate.now());
        Project p = new Project("PRJ-A", "Website Redesign", new BigDecimal("50000"), LocalDate.now());
        System.out.println(e.getProjects().isEmpty());  // phải true
        System.out.println(p.getEmployees().isEmpty()); // phải true

        Set<Employee> set = new HashSet<>();
        set.add(new Employee("a@x.com", "A1", Gender.MALE, BigDecimal.ONE, LocalDate.now()));
        set.add(new Employee("a@x.com", "A2", Gender.FEMALE, BigDecimal.TEN, LocalDate.now()));
        System.out.println(set.size()); // phải 1 (trùng email -> equals đúng)
    }
}