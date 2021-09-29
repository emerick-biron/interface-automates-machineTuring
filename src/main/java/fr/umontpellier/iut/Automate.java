package fr.umontpellier.iut;

import java.util.*;

public class Automate {
    private List<Etat> etats;
    private List<Transition> transition;
    private Mot mot;
    private Etat etatInitial;
    private List<Etat> etatsFinaux;

    public Automate(List<Etat> etats, List<Transition> transition) {
        this.etats = etats;
        this.transition = transition;
        mot = null;
    }

    public void setEtatInitial() {
        for (Etat etat : etats) {
            if (etat.estInitial()) {
                etatInitial = etat;
            }
        }
    }

    public void setEtatsFinaux() {
        etatsFinaux = new ArrayList<>();
        for (Etat etat : etats) {
            if (etat.estFinal()) {
                etatsFinaux.add(etat);
            }
        }
    }
}
























