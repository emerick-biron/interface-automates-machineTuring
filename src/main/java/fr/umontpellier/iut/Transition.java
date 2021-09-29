package fr.umontpellier.iut;

import java.util.*;

public class Transition {
    private List<Character> lettres;
    private Etat etatOrigine;
    private Etat etatDestination;

    public Transition(List<Character> lettres, Etat etatOrigine, Etat etatDestination) {
        this.lettres = lettres;
        this.etatOrigine = etatOrigine;
        this.etatDestination = etatDestination;
        etatDestination.ajoutTransitionEntrante(this);
        etatOrigine.ajoutTransitionSortante(this);
    }

    public List<Character> getLettres() {
        return lettres;
    }
}