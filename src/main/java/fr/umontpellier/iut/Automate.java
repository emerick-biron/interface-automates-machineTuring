package fr.umontpellier.iut;

import java.util.*;

public class Automate {
    private List<Etat> etats;
    private List<Transition> transition;
    private Mot mot;
    private Etat etatInitial;
    private List<Etat> etatsFinaux;

    public Automate(List<Etat> etats, List<Transition> transition, Etat etatInitial, List<Etat> etatsFinaux) {
        this.etats = etats;
        this.transition = transition;
        this.etatInitial = etatInitial;
        this.etatsFinaux = etatsFinaux;
        mot = null;
    }
}