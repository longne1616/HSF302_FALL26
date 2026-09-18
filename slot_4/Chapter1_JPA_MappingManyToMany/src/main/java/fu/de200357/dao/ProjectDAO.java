package fu.de200357.dao;

import fu.de200357.pojo.Project;
import fu.de200357.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

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
}