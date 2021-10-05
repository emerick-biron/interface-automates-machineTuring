package fr.umontpellier.iut.logique;

public class Transition {
    private Etat etatDepart;
    private Etat etatArrivee;
    private char etiquette;

    public Transition(Etat etatDepart, Etat etatArrivee, char etiquette) {
        this.etatDepart = etatDepart;
        this.etatArrivee = etatArrivee;
        this.etiquette = etiquette;
    }

    public Etat getEtatDepart() {
        return etatDepart;
    }

    public Etat getEtatArrivee() {
        return etatArrivee;
    }

    public char getEtiquette() {
        return etiquette;
    }


}
