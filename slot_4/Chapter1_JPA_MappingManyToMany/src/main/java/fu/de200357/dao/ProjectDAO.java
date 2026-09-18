package fu.de200357.dao;

import fu.de200357.pojo.Project;
import fu.de200357.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class ProjectDAO {

    public void save(Project p) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(p);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Project findById(Long id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.find(Project.class, id);
        } finally {
            em.close();
        }
    }

    // TODO 5.8
    public void printActiveEmployeeStatsPerProject() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                            "SELECT p.projectName, COUNT(e), SUM(e.salary) " +
                                    "FROM Project p JOIN p.employees e " +
                                    "WHERE e.active = true " +
                                    "GROUP BY p.projectName", Object[].class)
                    .getResultList();
            for (Object[] r : rows) {
                System.out.println(r[0] + " | active=" + r[1] + " | totalSalary=" + r[2]);
            }
        } finally {
            em.close();
        }
    }
}