package machines.logique.automates;

import machines.logique.Transition;

public class TransitionAtmt extends Transition<TransitionAtmt, EtatAtmt> {

    public TransitionAtmt(EtatAtmt etatDepart, EtatAtmt etatArrivee, char etiquette) {
        super(etatDepart, etatArrivee, etiquette);
    }


}
