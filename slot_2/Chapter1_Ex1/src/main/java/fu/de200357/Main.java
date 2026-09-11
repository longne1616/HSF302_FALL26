package fu.de200357;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsf302FU");
        System.out.println("EMF tao thanh cong!");
        emf.close();
    }
}