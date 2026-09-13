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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsf302FU");
        EmployeeDAO employeeDAO = new EmployeeDAO(emf);

        System.out.println("==================================================");
        System.out.println("====== TODO 0.8: DEMO LUONG CRUD DAY DU IN MAIN ===");
        System.out.println("==================================================");

        // 1. CREATE
        System.out.println("\n--- 1. CREATE ---");
        Employee emp = Employee.builder()
                .fullName("Nguyen Van A")
                .email("nguyenvana@gmail.com")
                .salary(new BigDecimal("15000000.00"))
                .gender(Gender.MALE)
                .hireDate(LocalDate.of(2023, 1, 15))
                .active(true)
                .build();
        employeeDAO.save(emp);
        Long id = emp.getId();
        System.out.println("-> [CREATE] Da them Employee thanh cong voi ID: " + id);

        // 2. READ
        System.out.println("\n--- 2. READ ---");
        Employee foundEmp = employeeDAO.findById(id);
        System.out.println("-> [READ] Tim thay theo ID: " + foundEmp);

        // 3. UPDATE
        System.out.println("\n--- 3. UPDATE ---");
        if (foundEmp != null) {
            System.out.println("-> Luong truoc khi update: " + foundEmp.getSalary());
            foundEmp.setSalary(new BigDecimal("20000000.00"));
            foundEmp.setFullName("Nguyen Van A (Updated)");
            employeeDAO.update(foundEmp);
            System.out.println("-> [UPDATE] Da cap nhat thong tin cho Employee ID: " + id);
        }

        // 4. READ LAI KIEM TRA SAU UPDATE
        System.out.println("\n--- 4. READ LAI KIEM TRA SAU UPDATE ---");
        Employee updatedEmp = employeeDAO.findById(id);
        if (updatedEmp != null) {
            System.out.println("-> Ten sau khi update: " + updatedEmp.getFullName());
            System.out.println("-> Luong sau khi update: " + updatedEmp.getSalary());
        }

        // 5. DELETE
        System.out.println("\n--- 5. DELETE ---");
        employeeDAO.delete(id);
        System.out.println("-> [DELETE] Da goi xoa Employee ID: " + id);

        // 6. READ LAI KIEM TRA DA XOA
        System.out.println("\n--- 6. READ LAI KIEM TRA SAU DELETE ---");
        Employee checkDeleted = employeeDAO.findById(id);
        if (checkDeleted == null) {
            System.out.println("-> Ket qua findById(" + id + "): null (Thong bao: Khong tim thay nhan vien)");
        } else {
            System.out.println("-> Nhan vien van con: " + checkDeleted);
        }

        System.out.println("\n==================================================");
        System.out.println("====== HOAN THANH LUONG DEMO CRUD TUAN TU ======");
        System.out.println("==================================================");

        emf.close();
    }
}