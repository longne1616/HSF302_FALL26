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

        // --- TEST CREATE ---
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

        // --- TEST READ ---
        Employee foundEmp = employeeDAO.findById(emp.getId());
        System.out.println("-> Tim thay: " + foundEmp);

        List<Employee> list = employeeDAO.findAll();
        System.out.println("-> So luong Employee trong DB: " + list.size());

        // --- TEST UPDATE ---
        if (foundEmp != null) {
            foundEmp.setFullName("Nguyen Van A (Updated)");
            employeeDAO.update(foundEmp);
            System.out.println("-> Sau khi update: " + employeeDAO.findById(foundEmp.getId()).getFullName());
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