package machines.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class VueMachine extends Pane {
    private Machine machine;
    private VuePrincipale vuePrincipale;
    private ObservableList<VueEtat> vuesEtatSelectionnes = FXCollections.observableArrayList();
    private ObservableList<VueTransition> vuesTransitionSelectionnes = FXCollections.observableArrayList();

    public VueMachine(Machine machine, VuePrincipale vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.machine = machine;

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getTarget() == this) {
                deSelectionnerVues();
                vuePrincipale.unbindCheckBoxes();
            }
        });
    }


    public Machine getMachine() {
        Machine machine = this.machine;
        return machine;
    }

    public ObservableList<VueTransition> getVuesTransitionSelectionnes() {
        return vuesTransitionSelectionnes;
    }

    public ObservableList<VueEtat> getVuesEtatSelectionnes() {
        return vuesEtatSelectionnes;
    }

    public VuePrincipale getVuePrincipale() {
        return vuePrincipale;
    }

    public VueEtat getVueEtat(Etat etat) {
        for (Node n : getChildren()) {
            if (n instanceof VueEtat) {
                VueEtat vueEtat = (VueEtat) n;
                if (vueEtat.getEtat().equals(etat)) return vueEtat;
            }
        }
        return null;
    }

    public VueTransition getVueTransition(Transition transition) {
        for (Node n : getChildren()) {
            if (n instanceof VueTransition) {
                VueTransition vueTransition = (VueTransition) n;
                if (vueTransition.getTransition().equals(transition)) return vueTransition;
            }
        }
        return null;
    }

    public ArrayList<VueTransition> getVuesTransition(VueEtat vueEtat1, VueEtat vueEtat2) {
        ArrayList<VueTransition> res = new ArrayList<>();
        for (Transition t : vueEtat1.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat2.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        for (Transition t : vueEtat2.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat1.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        return res;
    }

    public void clear() {
        vuesEtatSelectionnes.clear();
        vuesTransitionSelectionnes.clear();
        List<Etat> etats = machine.getEtats();
        if (!etats.isEmpty()) {
            int l = etats.size();
            for (int i = 0; i < l; i++) {
                machine.supprimerEtat(etats.get(0));
            }
        }
    }

    public abstract void chargerFichier(String nomFichier) throws IOException;

    public abstract void sauvegarder(String nomFichier) throws IOException;


    public void deSelectionnerVues() {
        deSelectionnerVuesEtat();
        deSelectionnerVuesTransition();
    }

    public void deSelectionnerVuesEtat() {
        for (Etat e : machine.getEtats()) {
            VueEtat vueEtat = getVueEtat(e);
            vueEtat.deSelectionner();
        }
    }

    public void deSelectionnerVuesTransition() {
        for (Transition t : machine.getTransitions()) {
            VueTransition vueTransition = getVueTransition(t);
            vueTransition.deSelectionner();
        }
    }
}

































