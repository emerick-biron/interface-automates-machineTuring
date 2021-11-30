package machines.gui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import machines.gui.automates.VueAutomate;
import machines.gui.automates.VueTransitionAtmt;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;
import machines.logique.automates.TransitionAtmt;

import java.io.*;
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

    public abstract void ajoutVueTransition(Transition transition);

    public void supprimerVueTransition(Transition transition) {
        VueTransition vueTransition = getVueTransition(transition);
        if (vueTransition != null) {
            getChildren().remove(vueTransition);
            ArrayList<VueTransition> vueTransitions =
                    getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin());
            for (int i = 0; i < vueTransitions.size(); i++) {
                vueTransitions.get(i).positionnerLabelEtiquette(i);
            }
        }
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

    public VueEtat getVueEtat(String labelNumEtat) {
        for (Node n : getChildren()) {
            if (n instanceof VueEtat) {
                VueEtat vueEtat = (VueEtat) n;
                if (vueEtat.getLabelNumEtat().getText().equals(labelNumEtat)) return vueEtat;
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

    public void chargerFichier(String nomFichier) throws IOException {
        clear();
        machine.chargerFichier(nomFichier);

        FileReader fr = new FileReader(nomFichier);
        BufferedReader bf = new BufferedReader(fr);

        String ligne = bf.readLine();

        while (!(ligne == null || ligne.contains("###"))) {
            ligne = bf.readLine();
        }

        double largeurVA;
        double hauteurVA;

        ligne = bf.readLine();
        if (ligne.contains("DIM")) {
            Screen sreen = Screen.getPrimary();
            String[] dimensions = ligne.split(" ");
            double largeurVP = Double.parseDouble(dimensions[1]);
            double hauteurVP = Double.parseDouble(dimensions[2]);
            double largeurEcran = sreen.getBounds().getWidth();
            double hauteurEcran = sreen.getBounds().getHeight();
            double deltaHauteur = 1;
            double deltaLargeur = 1;

            if (largeurEcran < largeurVP) {
                deltaLargeur = largeurEcran / largeurVP;
            }
            if (hauteurEcran < hauteurVP) {
                deltaHauteur = hauteurEcran / hauteurVP;
            }

            largeurVA = Double.parseDouble(dimensions[3]) * deltaLargeur;
            hauteurVA = Double.parseDouble(dimensions[4]) * deltaHauteur;

            Stage primaryStage = getVuePrincipale().getApp().getPrimaryStage();

            primaryStage.setWidth(largeurVP * deltaLargeur);
            primaryStage.setHeight(hauteurVP * deltaHauteur);

            ligne = bf.readLine();
        } else {
            largeurVA = getWidth();
            hauteurVA = getHeight();
        }

        while (ligne != null) {
            String[] split = ligne.split(" ");

            if (split.length >= 3) {

                String labelNumEtat = split[0];
                double xPos = Double.parseDouble(split[1]);
                double yPos = Double.parseDouble(split[2]);

                VueEtat vueEtat = getVueEtat(labelNumEtat);

                if (vueEtat != null) {
                    //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
                    double taille = vueEtat.getCercle().getRadius() * 2 + 20;
                    if (xPos >= 0 && xPos + taille <= largeurVA) {
                        vueEtat.setLayoutX(xPos);
                    }
                    if (yPos >= 0 && yPos + taille <= hauteurVA - 50) {
                        vueEtat.setLayoutY(yPos);
                    }
                }
            }
            ligne = bf.readLine();
        }


        bf.close();
        fr.close();
    }

    public void sauvegarder(String nomFichier) throws IOException {
        machine.sauvegarder(nomFichier);

        Writer fileWriter = new FileWriter(nomFichier, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("###");
        bufferedWriter.newLine();

        Stage primaryStage = getVuePrincipale().getApp().getPrimaryStage();

        bufferedWriter
                .write("DIM: " + primaryStage.getWidth() + " " + primaryStage.getHeight() + " " + getWidth() + " " +
                        getHeight());
        bufferedWriter.newLine();

        for (Etat e : machine.getEtats()) {
            VueEtat vueEtat = getVueEtat(e);
            if (vueEtat != null) {
                bufferedWriter.write(vueEtat.getLabelNumEtat().getText() + " " + vueEtat.getLayoutX() + " " +
                        vueEtat.getLayoutY());
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
        fileWriter.close();
    }


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

































