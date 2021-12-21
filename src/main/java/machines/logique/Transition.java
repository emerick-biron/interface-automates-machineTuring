package machines.logique;

public abstract class Transition<T extends Transition<T>> {
    private Etat<T> etatDepart;
    private Etat<T> etatArrivee;
    private char etiquette;

    public Transition(Etat<T> etatDepart, Etat<T> etatArrivee, char etiquette) {
        this.etatDepart = etatDepart;
        this.etatArrivee = etatArrivee;
        this.etiquette = etiquette;
    }

    public Etat<T> getEtatDepart() {
        return etatDepart;
    }

    public Etat<T> getEtatArrivee() {
        return etatArrivee;
    }

    public char getEtiquette() {
        return etiquette;
    }
}