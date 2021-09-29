package fr.umontpellier.iut;

import java.util.*;

public class Mot {
    private List<Character> lettres;

    public Mot(List<Character> lettres) {
        this.lettres = lettres;
    }

    public List<Character> getLettres() {
        return lettres;
    }
}