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
        // Tạo suffix thời gian để email luôn là unique mỗi lần bấm Run
        String time = String.valueOf(System.currentTimeMillis());

        Employee e = new Employee("test_" + time + "@company.com", "Test", Gender.MALE, new BigDecimal("1000"), LocalDate.now());
        Project p = new Project("PRJ-A_" + time, "Website Redesign", new BigDecimal("50000"), LocalDate.now());
        System.out.println(e.getFullName());   // in "Test"
        System.out.println(p.getProjectName()); // in "Website Redesign"

        Employee e2 = new Employee("test2_" + time + "@company.com", "Test2", Gender.MALE, new BigDecimal("1000"), LocalDate.now());
        System.out.println(e2.getProjects()); // in []

        Project p2 = new Project("PRJ-B_" + time, "Test Project", new BigDecimal("1000"), LocalDate.now());
        System.out.println(p2.getEmployees()); // in []

        Set<Employee> set = new HashSet<>();
        set.add(new Employee("dup_" + time + "@company.com", "A1", Gender.MALE, BigDecimal.ONE, LocalDate.now()));
        set.add(new Employee("dup_" + time + "@company.com", "A2", Gender.FEMALE, BigDecimal.TEN, LocalDate.now()));
        System.out.println(set.size()); // in 1

        Employee emp = new Employee("h1_" + time + "@company.com", "H1", Gender.MALE, BigDecimal.ONE, LocalDate.now());
        Project prj = new Project("PRJ-H_" + time, "Helper Test", BigDecimal.TEN, LocalDate.now());
        emp.assignToProject(prj);
        System.out.println(emp.getProjects().contains(prj)); // in true
        System.out.println(prj.getEmployees().contains(emp)); // in true

        EmployeeDAO employeeDAO = new EmployeeDAO();
        ProjectDAO projectDAO = new ProjectDAO();

        Employee empDao = new Employee("dao1_" + time + "@company.com", "DAO1", Gender.MALE, BigDecimal.ONE, LocalDate.now());
        Project prjDao = new Project("PRJ-DAO_" + time, "DAO Test", BigDecimal.TEN, LocalDate.now());
        employeeDAO.save(empDao);
        projectDAO.save(prjDao);

        employeeDAO.assignEmployeeToProject(empDao.getId(), prjDao.getId());
        Employee found = employeeDAO.findByIdWithProjects(empDao.getId());
        System.out.println(found.getProjects().size()); // in 1

        Employee nv1 = new Employee("nv1_" + time + "@company.com", "NV1", Gender.MALE, new BigDecimal("15000000"), LocalDate.now());
        Employee nv2 = new Employee("nv2_" + time + "@company.com", "NV2", Gender.FEMALE, new BigDecimal("18000000"), LocalDate.now());
        Employee nv3 = new Employee("nv3_" + time + "@company.com", "NV3", Gender.OTHER, new BigDecimal("12000000"), LocalDate.now());
        Project prjA = new Project("PRJ-A2_" + time, "Website", new BigDecimal("50000"), LocalDate.now());
        Project prjB = new Project("PRJ-B2_" + time, "Mobile App", new BigDecimal("80000"), LocalDate.now());

        employeeDAO.save(nv1);
        employeeDAO.save(nv2);
        employeeDAO.save(nv3);
        projectDAO.save(prjA);
        projectDAO.save(prjB);

        employeeDAO.assignEmployeeToProject(nv1.getId(), prjA.getId());
        employeeDAO.assignEmployeeToProject(nv1.getId(), prjB.getId());
        employeeDAO.assignEmployeeToProject(nv2.getId(), prjB.getId());
        employeeDAO.assignEmployeeToProject(nv3.getId(), prjA.getId());

        for (Long id : new Long[]{nv1.getId(), nv2.getId(), nv3.getId()}) {
            Employee empFound = employeeDAO.findByIdWithProjects(id);
            System.out.println(empFound.getFullName() + " -> " + empFound.getProjects().size() + " project(s)");
        }
        // đếm employee_project trong SSMS trước: 4 dòng
        employeeDAO.unassignEmployeeFromProject(nv1.getId(), prjB.getId());
// đếm lại: phải còn 3 dòng

        Employee checkE = employeeDAO.findById(nv1.getId());
        Project checkP = projectDAO.findById(prjB.getId());
        System.out.println(checkE != null); // phải true
        System.out.println(checkP != null); // phải true

    }

}