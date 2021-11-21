package machines.logique;

import machines.automates.logique.EtatAtmt;

public class Transition<T extends Transition<T, E>, E extends Etat<E, T>> {
    private E etatDepart;
    private E etatArrivee;
    private char etiquette;

    public Transition(E etatDepart, E etatArrivee, char etiquette) {
        this.etatDepart = etatDepart;
        this.etatArrivee = etatArrivee;
        this.etiquette = etiquette;
    }

    public E getEtatDepart() {
        return etatDepart;
    }

    public E getEtatArrivee() {
        return etatArrivee;
    }

    public char getEtiquette() {
        return etiquette;
    }
}
