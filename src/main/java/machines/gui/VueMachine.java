package machines.gui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import machines.gui.automates.VueAutomate;
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
    private ListChangeListener<Etat> miseAJourEtats = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (Etat e : change.getAddedSubList()) {
                    getChildren().add(new VueEtat(e, VueMachine.this));
                }
            } else if (change.wasRemoved()) {
                for (Etat e : change.getRemoved()) {
                    getChildren().remove(getVueEtat(e));
                }
            }

        }
        for (Node n : getChildren()) {
            if (n instanceof VueEtat) {
                VueEtat vueEtat = (VueEtat) n;
                vueEtat.setLabelNumEtat(machine.etatsProperty().indexOf(vueEtat.getEtat()));
            }
        }
    };
    private ListChangeListener<VueEtat> miseAJourVuesEtatSelectionnes = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (VueEtat vueEtat : change.getAddedSubList()) {
                    vueEtat.getCercle().setStroke(Color.valueOf("#003576"));
                    vueEtat.getCercle().setStrokeType(StrokeType.INSIDE);
                    vueEtat.getCercle().setStrokeWidth(3);
                }
            }
            if (change.wasRemoved()) {
                for (VueEtat vueEtat : change.getRemoved()) {
                    vueEtat.getCercle().setStroke(null);
                }
            }
        }
        getVuePrincipale().getBoutonAjouterTransition()
                .setVisible(getVuesEtatSelectionnes().size() <= 2 && getVuesEtatSelectionnes().size() >= 1);
        getVuePrincipale().getTextFieldEtiquette()
                .setVisible(getVuesEtatSelectionnes().size() <= 2 && getVuesEtatSelectionnes().size() >= 1);
    };

    public VueMachine(Machine machine, VuePrincipale vuePrincipale) {
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

    private void initListeners(){
        machine.etatsProperty().addListener(miseAJourEtats);
        getVuesEtatSelectionnes().addListener(miseAJourVuesEtatSelectionnes);
    }


    public Machine getMachine() {
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

































