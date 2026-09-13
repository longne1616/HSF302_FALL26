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
        System.out.println("====== TODO 0.8 & 0.10: DEMO CRUD & ENTITY LIFECYCLE ===");
        System.out.println("==================================================");

        // ---------------------------------------------------------------------
        // 1. CREATE
        // ---------------------------------------------------------------------
        System.out.println("\n--- 1. CREATE ---");

        // [TODO 0.10 - Lifecycle]: Trước save(), emp vừa khởi tạo bằng Builder/new, chưa có ID -> Trạng thái New / Transient
        Employee emp = Employee.builder()
                .fullName("Nguyen Van A")
                .email("nguyenvana@gmail.com")
                .salary(new BigDecimal("15000000.00"))
                .gender(Gender.MALE)
                .hireDate(LocalDate.of(2023, 1, 15))
                .active(true)
                .build();
        System.out.println("-> [Lifecycle Check]: Entity emp moi tao dang o trang thai New/Transient");

        // Gọi save(): Bên trong save(), ngay sau em.persist() entity chuyển sang Managed (trong Transaction).
        // Ngay sau khi method save() return (EntityManager đã close) -> emp chuyển sang trạng thái Detached.
        employeeDAO.save(emp);
        Long id = emp.getId();
        System.out.println("-> [CREATE] Da them Employee thanh cong voi ID: " + id);
        System.out.println("-> [Lifecycle Check]: Sau khi save() ket thuc va em.close(), entity emp sang trang thai Detached");

        // ---------------------------------------------------------------------
        // 2. READ
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. READ ---");

        // [TODO 0.10 - Lifecycle]: Bên trong findById(), em.find() tạo entity ở trạng thái Managed.
        // Sau khi findById() return (em đã close), foundEmp trả về nằm ở trạng thái Detached.
        Employee foundEmp = employeeDAO.findById(id);
        System.out.println("-> [READ] Tim thay theo ID: " + foundEmp);
        System.out.println("-> [Lifecycle Check]: Entity foundEmp nhan tu findById() dang o trang thai Detached");

        // ---------------------------------------------------------------------
        // 3. UPDATE
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. UPDATE ---");
        if (foundEmp != null) {
            System.out.println("-> Luong truoc khi update: " + foundEmp.getSalary());

            // Thay đổi giá trị khi foundEmp đang Detached
            foundEmp.setSalary(new BigDecimal("20000000.00"));
            foundEmp.setFullName("Nguyen Van A (Updated)");

            // [TODO 0.10 - Lifecycle]: Khi gọi update():
            // - Bên trong update(), object trả về từ em.merge() là MANAGED (trong transaction đó).
            // - Object cũ foundEmp truyền vào vẫn giữ nguyên trạng thái DETACHED.
            Employee mergedEmp = employeeDAO.update(foundEmp);
            System.out.println("-> [UPDATE] Da cap nhat thong tin cho Employee ID: " + id);
            System.out.println("-> [Lifecycle Check]: Object truyen bao foundEmp van la Detached; Object merged tu merge() tro thanh Managed trong transaction");
        }

        // ---------------------------------------------------------------------
        // 4. READ LAI KIEM TRA SAU UPDATE
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. READ LAI KIEM TRA SAU UPDATE ---");
        Employee updatedEmp = employeeDAO.findById(id);
        if (updatedEmp != null) {
            System.out.println("-> Ten sau khi update: " + updatedEmp.getFullName());
            System.out.println("-> Luong sau khi update: " + updatedEmp.getSalary());
        }

        // ---------------------------------------------------------------------
        // 5. DELETE
        // ---------------------------------------------------------------------
        System.out.println("\n--- 5. DELETE ---");

        // [TODO 0.10 - Lifecycle]: Khi gọi delete(id):
        // - Bên trong delete(), em.find() load entity lên Managed, sau đó em.remove() chuyển entity sang trạng thái REMOVED.
        // - Sau khi tx.commit(), bản ghi chính thức bị xóa hoàn toàn khỏi DB SQL Server.
        employeeDAO.delete(id);
        System.out.println("-> [DELETE] Da goi xoa Employee ID: " + id);
        System.out.println("-> [Lifecycle Check]: Trong delete(), entity chuyen sang Removed va xoa khoi DB sau commit()");

        // ---------------------------------------------------------------------
        // 6. READ LAI KIEM TRA DA XOA
        // ---------------------------------------------------------------------
        System.out.println("\n--- 6. READ LAI KIEM TRA SAU DELETE ---");
        Employee checkDeleted = employeeDAO.findById(id);
        if (checkDeleted == null) {
            System.out.println("-> Ket qua findById(" + id + "): null (Khong tim thay nhan vien)");
        }

        // ---------------------------------------------------------------------
        // TODO 0.9: KIỂM CHỨNG RÀNG BUỘC UNIQUE EMAIL
        // ---------------------------------------------------------------------
        System.out.println("\n==================================================");
        System.out.println("====== TODO 0.9: KIEM CHUNG UNIQUE EMAIL CONST ====");
        System.out.println("==================================================");

        String duplicateEmail = "unique_lifecycle_test@gmail.com";

        // emp1 ở trạng thái New/Transient
        Employee emp1 = Employee.builder()
                .fullName("Nhan Vien 1")
                .email(duplicateEmail)
                .salary(new BigDecimal("12000000.00"))
                .gender(Gender.MALE)
                .hireDate(LocalDate.now())
                .active(true)
                .build();
        employeeDAO.save(emp1);
        System.out.println("-> [SAVE 1] Them thanh cong nhan vien 1 voi ID: " + emp1.getId());

        // emp2 ở trạng thái New/Transient
        Employee emp2 = Employee.builder()
                .fullName("Nhan Vien 2 (Email Trung)")
                .email(duplicateEmail)
                .salary(new BigDecimal("15000000.00"))
                .gender(Gender.FEMALE)
                .hireDate(LocalDate.now())
                .active(true)
                .build();

        System.out.println("-> [SAVE 2] Co y save() nhan vien 2 voi email trùng: " + duplicateEmail);
        try {
            employeeDAO.save(emp2);
        } catch (Exception ex) {
            System.out.println("\n[DA BAT LOI UNIQUE CONSTRAINT THANH CONG]");
            System.out.println("-> Thong bao: Khong the luu do email '" + duplicateEmail + "' da ton tai trong CSDL!");
            System.out.println("-> Ngoai le ghi nhan: " + ex.getClass().getName());
        }

        // Dọn dẹp
        if (emp1.getId() != null) {
            employeeDAO.delete(emp1.getId());
            System.out.println("-> Da xoa du lieu test (ID: " + emp1.getId() + ")");
        }

        System.out.println("\n==================================================");
        System.out.println("======    HOAN THANH TOAN BO BAI TODO    ==========");
        System.out.println("==================================================");

        emf.close();
    }

}