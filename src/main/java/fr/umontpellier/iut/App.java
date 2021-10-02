package fr.umontpellier.iut;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Automate automate = new Automate("./automates_txt/input.txt");
        System.out.println(automate.reconnaitND("b"));

    }
}
