package fr.umontpellier.iut.logique;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Automate automate = new Automate();
        Etat etat = new Etat();
        automate.ajouterEtat(etat);

        automate.ecrit("a.txt");
    }
}
