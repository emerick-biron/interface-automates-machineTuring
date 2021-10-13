package fr.umontpellier.iut.logique;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Automate automate = new Automate();

        automate.chargerFichier("/Users/lenaisdesbos/Documents/IUT/S3/Projet_S3/Git/clone/automates_txt/input.txt");
        automate.lancer("a");
        System.out.println(automate.getEtatsActifs());

    }
}
