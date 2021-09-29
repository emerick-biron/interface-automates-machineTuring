package fr.umontpellier.iut;

import java.util.*;

public class Etat {

    private String nom;
    private boolean estInitial;
    private boolean estActif;
    private boolean estFinal;
    private List<Transition> transitionsEntrantes;
    private List<Transition> transitionsSortantes;

    public List<Transition> getTransitionsSortantes() {
        return transitionsSortantes;
    }

    public Etat(String nom, boolean estInitial, boolean estFinal) {
        this.nom = nom;
        this.estInitial = estInitial;
        this.estFinal = estFinal;
        transitionsEntrantes = new ArrayList<>();
        transitionsSortantes = new ArrayList<>();
        estActif = false;
    }

    public void ajoutTransitionSortante(Transition transitionSortante){
        transitionsSortantes.add(transitionSortante);
    }

    public void ajoutTransitionEntrante(Transition transitionEntrante){
        transitionsEntrantes.add(transitionEntrante);
    }

    public String getNom() {
        return nom;
    }

    public boolean estInitial() {
        return estInitial;
    }

    public boolean estActif() {
        return estActif;
    }

    public boolean estFinal() {
        return estFinal;
    }
}