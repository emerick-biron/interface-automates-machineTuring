package machines.automates.logique;

import machines.logique.Etat;
import machines.logique.Transition;

public class TransitionAtmt extends Transition<EtatAtmt> {

    public TransitionAtmt(EtatAtmt etatDepart, EtatAtmt etatArrivee, char etiquette) {
        super(etatDepart, etatArrivee, etiquette);
    }


}
