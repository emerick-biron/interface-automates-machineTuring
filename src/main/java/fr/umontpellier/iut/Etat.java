package fr.umontpellier.iut;

import java.util.*;

public class Etat {

    private Automate automate;
    private String nom;
    private boolean estInitial;
    private boolean estActif;
    private boolean estFinal;

    public Etat(Automate automate, String nom, boolean estInitial, boolean estFinal) {
        this.automate = automate;
        this.nom = nom;
        this.estInitial = estInitial;
        this.estFinal = estFinal;
        estActif = false;
    }
}