package fu.de200357;

import fu.de200357.dao.DepartmentDAO;
import fu.de200357.dao.EmployeeDAO;
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
        EmployeeDAO employeeDAO = new EmployeeDAO();

        // Vi hbm2ddl.auto = update nen du lieu KHONG bi xoa moi lan chay.
        // Doi hau to nay (v1 -> v2 -> v3...) truoc moi lan chay lai de khong dung unique.
        String suffix = "v1";

        System.out.println("========== TODO 2.7: DEMO PERSIST CASCADE & JOIN FETCH ==========");

        // 1) Tao Department + 3 Employee qua helper method (TODO 2.4)
        Department it = new Department("Marketing " + suffix, "Ha Noi");

        Employee e1 = new Employee("aa." + suffix + "@company.com", "Nguyen Van A", Gender.MALE,
                new BigDecimal("15000000"), LocalDate.of(2022, 1, 10));
        Employee e2 = new Employee("bb." + suffix + "@company.com", "Tran Thi B", Gender.FEMALE,
                new BigDecimal("18000000"), LocalDate.of(2021, 6, 1));
        Employee e3 = new Employee("cc." + suffix + "@company.com", "Le Van C", Gender.OTHER,
                new BigDecimal("12000000"), LocalDate.of(2023, 3, 15));

        it.addEmployee(e1);
        it.addEmployee(e2);
        it.addEmployee(e3);

        // Kiem chung TODO 2.4 — dong bo 2 chieu, phai in ra 2 dong true
        System.out.println("Sync check (dept chua employee): " + it.getEmployees().contains(e1));
        System.out.println("Sync check (employee tro ve dept): " + (e1.getDepartment() == it));

        // Tao them 1 phong nua de test bai toan N+1
        Department hr = new Department("Human Resources " + suffix, "Da Nang");
        Employee e4 = new Employee("dd." + suffix + "@company.com", "Pham Van D", Gender.MALE,
                new BigDecimal("14000000"), LocalDate.of(2020, 4, 12));
        hr.addEmployee(e4);

        // 2) Chi persist department — cascade = ALL tu luu cac Employee (TODO 2.7)
        departmentDAO.save(it);
        departmentDAO.save(hr);
        System.out.println("Da luu Department, id = " + it.getId() + " va " + hr.getId());

        // 3) Tim lai kem employees bang JOIN FETCH (TODO 2.6)
        //    Khong bi LazyInitializationException du EntityManager da dong,
        //    vi employees da duoc load ngay trong cung 1 query.
        Department found = departmentDAO.findByIdWithEmployees(it.getId());
        System.out.println("Phong ban: " + found.getName());
        for (Employee e : found.getEmployees()) {
            System.out.println("  - " + e);
        }

        System.out.println("\n========== TODO 2.8: TAI HIEN N+1 QUERY PROBLEM ==========");
        /*
         * Mo EntityManager truc tiep tai Main de giu Session trong khi duyet Lazy collection.
         * 1 cau SELECT lay toan bo N Department + N cau SELECT rieng cho tung danh sach Employee.
         */
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        List<Department> departmentsLazy =
                em.createQuery("SELECT d FROM Department d", Department.class).getResultList();

        for (Department d : departmentsLazy) {
            System.out.println("Department: " + d.getName()
                    + " - Employee count: " + d.getEmployees().size());
        }
        int n = departmentsLazy.size();
        em.close();

        System.out.println("\n========== TODO 2.9: FIX N+1 BANG JOIN FETCH ==========");
        /*
         * Chi sinh duy nhat 1 cau SQL JOIN:
         * SELECT DISTINCT d FROM Department d JOIN FETCH d.employees
         */
        List<Department> departmentsFetched = departmentDAO.findAllWithEmployees();
        for (Department d : departmentsFetched) {
            System.out.println("Department: " + d.getName()
                    + " - Employee count: " + d.getEmployees().size());
        }

        // Ghi lai so cau SQL truoc/sau khi fix
        System.out.println("\n>>> TODO 2.8 (LAZY): 1 + N = 1 + " + n + " = " + (1 + n) + " cau SQL");
        System.out.println(">>> TODO 2.9 (JOIN FETCH): 1 cau SQL duy nhat");

        System.out.println("\n========== TEST UNIQUE EMAIL ==========");
        try {
            Employee dup = new Employee("aa." + suffix + "@company.com", "Trung Email", Gender.MALE,
                    new BigDecimal("9000000"), LocalDate.now());
            dup.setDepartment(it);
            employeeDAO.save(dup);
            System.out.println("LOI: dang le phai nem exception vi trung email!");
        } catch (Exception ex) {
            System.out.println("OK - da nem exception vi pham unique: " + ex.getClass().getSimpleName());
        }

        System.out.println("\n========== TEST CASCADE DELETE + ORPHAN REMOVAL ==========");
        System.out.println("Truoc khi xoa, tong so employee = " + employeeDAO.findAll().size());
        departmentDAO.delete(hr.getId());
        System.out.println("Sau khi xoa phong HR, tong so employee = " + employeeDAO.findAll().size());
        System.out.println("Tim lai phong HR (phai null): " + departmentDAO.findById(hr.getId()));

        JPAUtil.close();
    }
}