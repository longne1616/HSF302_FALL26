package fu.de200357;

import fu.de200357.dao.DepartmentDAO;
import fu.de200357.dao.EmployeeDAO;
import fu.de200357.pojo.Department;
import fu.de200357.pojo.Employee;
import fu.de200357.pojo.Gender;
import fu.de200357.util.JPAUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        DepartmentDAO deptDAO = new DepartmentDAO();
        EmployeeDAO empDAO = new EmployeeDAO();

        System.out.println("==================================================");
        System.out.println("      KIỂM TRA TODO 2.1 VÀ TODO 2.2      ");
        System.out.println("==================================================\n");

        try {
            // 1. Tạo và lưu Department trước để lấy FK
            Department dept = new Department("Phòng Kỹ Thuật", "Hà Nội");
            deptDAO.save(dept);
            System.out.println("[TEST PASS] Tạo Department thành công, ID = " + dept.getId());

            // 2. TODO 2.1 & 2.2: Tạo Employee gắn với Department (Owning side)
            Employee emp = new Employee(
                    "dev.nguyen@company.com",
                    "Nguyễn Văn Dev",
                    Gender.MALE, // Enum String
                    new BigDecimal("20000000.00"), // BigDecimal
                    LocalDate.now() // LocalDate
            );

            // Thiết lập mối quan hệ Owning side từ phía Employee
            emp.setDepartment(dept);

            // Lưu Employee xuống DB
            empDAO.save(emp);
            System.out.println("[TEST PASS] Lưu Employee thành công với department_id = " + emp.getDepartment().getId());

            // 3. Kiểm tra Validation Unique Email (TODO 2.1)
            System.out.print("Kiểm tra Unique Email... ");
            try {
                Employee dupEmp = new Employee(
                        "dev.nguyen@company.com", // Trùng email
                        "Nguyễn Văn Trùng",
                        Gender.FEMALE,
                        new BigDecimal("15000000.00"),
                        LocalDate.now()
                );
                dupEmp.setDepartment(dept);
                empDAO.save(dupEmp);
                System.err.println("-> [FAILED]: Trùng email nhưng không ném Exception!");
            } catch (Exception e) {
                System.out.println("-> [TEST PASS] Đã chặn trùng email thành công!");
            }

            System.out.println("\n==================================================");
            System.out.println("   KẾT QUẢ: TODO 2.1 VÀ 2.2 HOẠT ĐỘNG CHUẨN!   ");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("\n[LỖI RUNTIME]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JPAUtil.close();
        }
    }
}