package fu.de200357;

import fu.de200357.dao.EmployeeDAO;
import fu.de200357.pojo.Employee;
import fu.de200357.pojo.Gender;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsf302FU");
        System.out.println("EMF tao thanh cong!");

        // 2. Khởi tạo DAO
        EmployeeDAO employeeDAO = new EmployeeDAO(emf);

        // --- TEST CREATE (TODO 0.3) ---
        Employee emp = Employee.builder()
                .fullName("Nguyen Van A")
                .email("nguyenvana@gmail.com")
                .salary(new BigDecimal("15000000.00"))
                .gender(Gender.MALE)
                .hireDate(LocalDate.of(2023, 1, 15))
                .active(true)
                .build();
        employeeDAO.save(emp);
        System.out.println("-> Da them Employee ID: " + emp.getId());

        // --- TEST READ CO BAN (TODO 0.4) ---
        Employee foundEmp = employeeDAO.findById(emp.getId());
        System.out.println("-> Tim thay theo ID: " + foundEmp);

        List<Employee> list = employeeDAO.findAll();
        System.out.println("-> So luong Employee trong DB: " + list.size());

        // --- TEST READ CO DIEU KIEN (TODO 0.5) ---
        System.out.println("\n=== TEST TODO 0.5 ===");

        // 1. Tìm theo email tồn tại & không tồn tại
        Employee foundByEmail = employeeDAO.findByEmail("nguyenvana@gmail.com");
        System.out.println("-> Tim theo email (co ton tai): " + foundByEmail);

        Employee notFoundEmail = employeeDAO.findByEmail("khongtontai@gmail.com");
        System.out.println("-> Tim theo email (khong ton tai): " + notFoundEmail);

        // 2. Tìm danh sách theo điều kiện salary > minSalary hoặc active = true
        List<Employee> listByCondition = employeeDAO.findBySalaryGreaterThanOrActive(new BigDecimal("10000000.00"));
        System.out.println("-> So luong thoa dieu kien JPQL: " + listByCondition.size());

        // --- TEST UPDATE ---
        if (foundEmp != null) {
            foundEmp.setFullName("Nguyen Van A (Updated)");
            employeeDAO.update(foundEmp);
            System.out.println("\n-> Sau khi update: " + employeeDAO.findById(foundEmp.getId()).getFullName());
        }

        // --- TEST DELETE ---
        if (foundEmp != null) {
            employeeDAO.deleteById(foundEmp.getId());
            System.out.println("-> Da xoa Employee ID: " + foundEmp.getId());
        }

        // 3. Đóng EMF khi kết thúc ứng dụng
        emf.close();
    }
}