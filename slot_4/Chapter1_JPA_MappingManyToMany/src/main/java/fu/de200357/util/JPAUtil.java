package fu.de200357.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("hsf302FU");

    public static EntityManagerFactory getEMF() { return EMF; }
    public static void close() { EMF.close(); }
}