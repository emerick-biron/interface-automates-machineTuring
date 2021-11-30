package machines.gui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import machines.gui.automates.VueAutomate;
import machines.gui.automates.VueTransitionAtmt;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;
import machines.logique.automates.TransitionAtmt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class VueMachine extends Pane {
    private Machine machine;
    private VuePrincipale vuePrincipale;
    private ObservableList<VueEtat> vuesEtatSelectionnes = FXCollections.observableArrayList();
    private ObservableList<VueTransition> vuesTransitionSelectionnes = FXCollections.observableArrayList();
    private SetChangeListener<Etat> miseAJourEtats = change -> {
        if (change.wasAdded()) {
            VueEtat vueEtat = new VueEtat(change.getElementAdded(), VueMachine.this);
            vueEtat.setLabelNumEtat(machine.getEtats().size() - 1);
            getChildren().add(vueEtat);
        } else if (change.wasRemoved()) {
            VueEtat vueEtatRemoved = getVueEtat(change.getElementRemoved());
            getChildren().remove(vueEtatRemoved);
            for (Node n : getChildren()) {
                if (n instanceof VueEtat) {
                    VueEtat vueEtat = (VueEtat) n;
                    if (vueEtat.getNumEtat() > vueEtatRemoved.getNumEtat()) {
                        vueEtat.setLabelNumEtat((vueEtat.getNumEtat() - 1));
                    }
                }
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
    private ListChangeListener<VueTransition> miseAJourVuesTransitionSelectionnes = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (VueTransition vueTransition : change.getAddedSubList()) {
                    vueTransition.setCouleurSelection(true);
                }
            }
            if (change.wasRemoved()) {
                for (VueTransition vueTransition : change.getRemoved()) {
                    vueTransition.setCouleurSelection(false);
                }
            }
        }
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

    private void initListeners() {
        machine.etatsProperty().addListener(miseAJourEtats);
        vuesEtatSelectionnes.addListener(miseAJourVuesEtatSelectionnes);
        vuesTransitionSelectionnes.addListener(miseAJourVuesTransitionSelectionnes);
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
        machine.clear();
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

































