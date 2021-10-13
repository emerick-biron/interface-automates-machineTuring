package fr.umontpellier.iut.logique;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        Automate automate = new Automate();

        automate.chargerFichier("automates_txt/input.txt");
        automate.lancer("a");
        System.out.println(automate.getEtatsActifs());

    }
}
