package fu.de200357.dao;

import fu.de200357.pojo.Employee;
import fu.de200357.pojo.Project;
import fu.de200357.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class EmployeeDAO {

    public void save(Employee e) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(e);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Employee findById(Long id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    // TODO 5.6
    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee e = em.find(Employee.class, employeeId);
            Project p = em.find(Project.class, projectId);
            if (e == null || p == null) {
                throw new IllegalArgumentException("Employee hoặc Project không tồn tại");
            }
            e.assignToProject(p);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    // TODO 5.9
    public void unassignEmployeeFromProject(Long employeeId, Long projectId) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee e = em.find(Employee.class, employeeId);
            Project p = em.find(Project.class, projectId);
            e.unassignFromProject(p);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Employee findByIdWithProjects(Long id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Employee e JOIN FETCH e.projects WHERE e.id = :id", Employee.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // TODO 5.10
    public List<Employee> findActiveEmployeesInMultipleProjects() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT e FROM Employee e WHERE e.active = true AND SIZE(e.projects) > 1",
                    Employee.class).getResultList();
        } finally {
            em.close();
        }
    }

    // TODO 5.11
    public void deactivateEmployee(Long employeeId) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee e = em.find(Employee.class, employeeId);
            e.setActive(false);
            tx.commit();

        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}