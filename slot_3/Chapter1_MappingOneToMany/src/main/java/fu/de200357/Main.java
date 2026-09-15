package fu.de200357;

import fu.de200357.dao.DepartmentDAO;
import fu.de200357.pojo.Department;
import fu.de200357.pojo.Employee;
import fu.de200357.pojo.Gender;
import fu.de200357.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        DepartmentDAO departmentDAO = new DepartmentDAO();

        System.out.println("========== TODO 2.7: DEMO PERSIST CASCADE & JOIN FETCH ==========");
        // 1) Tạo Department + 3 Employee qua helper method (TODO 2.4)
        // Sửa "Marketing" thành "Marketing 2" hoặc tên bất kỳ chưa có trong DB
        Department it = new Department("Marketing", "Ha Noi");

        Employee e1 = new Employee("aa2.nguyen@company.com", "Nguyen Van A", Gender.MALE,
                new BigDecimal("15000000"), LocalDate.of(2022, 1, 10));
        Employee e2 = new Employee("bb2.tran@company.com", "Tran Thi B", Gender.FEMALE,
                new BigDecimal("18000000"), LocalDate.of(2021, 6, 1));
        Employee e3 = new Employee("cc2.le@company.com", "Le Van C", Gender.OTHER,
                new BigDecimal("12000000"), LocalDate.of(2023, 3, 15));

        it.addEmployee(e1);
        it.addEmployee(e2);
        it.addEmployee(e3);

        // Tạo thêm 1 phòng nữa để test bài toán N+1
        Department hr = new Department("Human Resources", "Da Nang");
        Employee e4 = new Employee("dd.pham@company.com", "Pham Van D", Gender.MALE,
                new BigDecimal("14000000"), LocalDate.of(2020, 4, 12));
        hr.addEmployee(e4);

        // 2) Chỉ persist department — cascade = ALL tự lưu các Employee
        departmentDAO.save(it);
        departmentDAO.save(hr);
        System.out.println("Luu thanh cong các phong ban!");

        // 3) Tim lại kèm employees bằng JOIN FETCH (TODO 2.6)
        Department found = departmentDAO.findByIdWithEmployees(it.getId());
        System.out.println("Phong ban: " + found.getName());
        for (Employee e : found.getEmployees()) {
            System.out.println("  - " + e);
        }

        System.out.println("\n========== TODO 2.8: TÁI HIỆN N+1 QUERY PROBLEM ==========");
        /*
         * Mở EntityManager trực tiếp tại Main để giữ Session trong khi duyệt Lazy collection
         * 1 câu SELECT lấy toàn bộ N Departments (SELECT d FROM Department d)
         * N câu SELECT lấy danh sách Employee riêng lẻ cho từng Department
         */
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        List<Department> departmentsLazy = em.createQuery("SELECT d FROM Department d", Department.class).getResultList();

        for (Department d : departmentsLazy) {
            System.out.println("Department: " + d.getName() + " - Employee count: " + d.getEmployees().size());
        }
        em.close(); // Đóng EntityManager sau khi kết thúc TODO 2.8

        System.out.println("\n========== TODO 2.9: FIX N+1 PROBLEM BẰNG JOIN FETCH ==========");
        /*
         * Chỉ sinh duy nhất 1 câu SQL JOIN (SELECT DISTINCT d FROM Department d JOIN FETCH d.employees)
         * Load toàn bộ Department và Employees trong 1 lần duy nhất
         */
        List<Department> departmentsFetched = departmentDAO.findAllWithEmployees();
        for (Department d : departmentsFetched) {
            System.out.println("Department: " + d.getName() + " - Employee count: " + d.getEmployees().size());
        }

        JPAUtil.close();
    }
}