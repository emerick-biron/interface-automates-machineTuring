package machines.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import machines.automates.gui.VueEtatAtmt;
import machines.automates.gui.VuePrincipaleAtmt;
import machines.automates.gui.VueTransitionAtmt;
import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class VueMachine<VP extends VuePrincipale<VP, VM, VE, VT, M, E, T>, VM extends VueMachine<VP, VM, VE,
        VT, M, E, T>, VE extends VueEtat<VP, VM, VE, VT, M, E, T>, VT extends VueTransition<VP, VM, VE, VT, M, E, T>,
        M extends Machine<E, T>, E extends Etat<E, T>, T extends Transition<T, E>>
        extends Pane {
    private M machine;
    private VP vuePrincipale;
    private ObservableList<VE> vuesEtatSelectionnes = FXCollections.observableArrayList();
    private ObservableList<VT> vuesTransitionSelectionnes = FXCollections.observableArrayList();

    public VueMachine(M machine, VP vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.machine = machine;

        initListeners();

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getTarget() == this) {
                deSelectionnerVues();
                vuePrincipale.unbindCheckBoxes();
            }
        });
    }

    public abstract void initListeners();

    public M getMachine() {
        return machine;
    }

    public ObservableList<VT> getVuesTransitionSelectionnes() {
        return vuesTransitionSelectionnes;
    }

    public ObservableList<VE> getVuesEtatSelectionnes() {
        return vuesEtatSelectionnes;
    }

    public VP getVuePrincipale() {
        return vuePrincipale;
    }

    public VE getVueEtat(E etat) {
        for (Node n : getChildren()) {
            if (n instanceof VueEtat) {
                VE vueEtat = (VE) n;
                if (vueEtat.getEtat().equals(etat)) return vueEtat;
            }
        }
        return null;
    }

    public VT getVueTransition(T transition) {
        for (Node n : getChildren()) {
            if (n instanceof VueTransition) {
                VT vueTransition = (VT) n;
                if (vueTransition.getTransition().equals(transition)) return vueTransition;
            }
        }
        return null;
    }

    public ArrayList<VT> getVuesTransition(VE vueEtat1, VE vueEtat2) {
        ArrayList<VT> res = new ArrayList<>();
        for (T t : vueEtat1.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat2.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        for (T t : vueEtat2.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat1.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        return res;
    }

    public void clear() {
        vuesEtatSelectionnes.clear();
        vuesTransitionSelectionnes.clear();
        List<E> etats = machine.getEtats();
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
        for (E e : machine.getEtats()) {
            VE vueEtat = getVueEtat(e);
            vueEtat.deSelectionner();
        }
    }

    public void deSelectionnerVuesTransition() {
        for (T t : machine.getTransitions()) {
            VT vueTransition = getVueTransition(t);
            vueTransition.deSelectionner();
        }
    }
}

































