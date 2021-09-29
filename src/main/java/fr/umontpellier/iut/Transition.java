package fr.umontpellier.iut;

import java.util.*;

public class Transition {
    private Automate automate;
    private List<Character> lettres;
    private List<Etat> etatsDestinations;
    private List<Etat> etatsOrigines;

    public Transition(List<Character> lettres, List<Etat> etatsDestinations, List<Etat> etatsOrigines) {
        this.lettres = lettres;
        this.etatsDestinations = etatsDestinations;
        this.etatsOrigines = etatsOrigines;
    }
}