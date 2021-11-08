package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import fr.umontpellier.iut.logique.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VueAutomate extends Pane {
    private Automate automate;
    private VuePrincipale vuePrincipale;
    private ObservableList<VueEtat> vuesEtatSelectionnes = FXCollections.observableArrayList();
    private ListChangeListener<Etat> miseAJourEtats = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (Etat e : change.getAddedSubList()) {
                    getChildren().add(new VueEtat(e, VueAutomate.this));
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
                vueEtat.setLabelNumEtat(automate.etatsProperty().indexOf(vueEtat.getEtat()));
            }
        }
    };
    private ListChangeListener<Transition> miseAJourTransition = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (Transition t : change.getAddedSubList()) {
                    fr.umontpellier.iut.gui.VueTransition
                            vueTransition = new fr.umontpellier.iut.gui.VueTransition(t, VueAutomate.this);
                    getChildren().add(vueTransition);
                    vueTransition.toBack();
                }
            } else if (change.wasRemoved()) {
                for (Transition t : change.getRemoved()) {
                    getChildren().remove(getVueTransition(t));
                }
            }
        }
        for (Transition t : automate.getTransitions()) {
            int n = 0;
            for (int i = 0; i < automate.getTransitions().indexOf(t); i++) {
                Transition t2 = automate.getTransitions().get(i);
                if (t.getEtatDepart() == t2.getEtatDepart() && t.getEtatArrivee() == t2.getEtatArrivee()) {
                    n++;
                }
            }
            VueTransition vueTransition = getVueTransition(t);
            if (n > 0){
                vueTransition.setFlechesVisible(false);
                vueTransition.toFront();
            } else {
                vueTransition.setFlechesVisible(true);
                vueTransition.toBack();
            }
            vueTransition.positionnerLabelEtiquette(n);
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
        vuePrincipale.getBoutonSupprimerEtat().setVisible(vuesEtatSelectionnes.size() > 0);
        vuePrincipale.getBoutonAjouterTransition()
                .setVisible(vuesEtatSelectionnes.size() <= 2 && vuesEtatSelectionnes.size() >= 1);
        vuePrincipale.getTextFieldEtiquette()
                .setVisible(vuesEtatSelectionnes.size() <= 2 && vuesEtatSelectionnes.size() >= 1);
    };

    public VueAutomate(Automate automate, VuePrincipale vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.automate = automate;
        this.automate.etatsProperty().addListener(miseAJourEtats);
        this.automate.transitionsProperty().addListener(miseAJourTransition);
        vuesEtatSelectionnes.addListener(miseAJourVuesEtatSelectionnes);

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getTarget() == this) {
                deSelectionnerVuesEtat();
                vuePrincipale.unbindCheckBoxes();
            }
        });
    }

    public ObservableList<VueEtat> getVuesEtatSelectionnes() {
        return vuesEtatSelectionnes;
    }

    public VuePrincipale getVuePrincipale() {
        return vuePrincipale;
    }

    public Automate getAutomate() {
        return automate;
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

    public void clear() {
        List<Etat> etats = automate.getEtats();
        if (!etats.isEmpty()) {
            int l = etats.size();
            for (int i = 0; i < l; i++) {
                automate.supprimerEtat(etats.get(0));
            }
        }
    }

    public void chargerFichier(String nomFichier) throws IOException {
        clear();
        automate.chargerFichier(nomFichier);

        FileReader fr = new FileReader(nomFichier);
        BufferedReader bf = new BufferedReader(fr);

        String ligne = bf.readLine();

        while (!(ligne == null || ligne.contains("###"))) {
            ligne = bf.readLine();
        }

        ligne = bf.readLine();

        while (ligne != null) {
            String[] split = ligne.split(" ");

            if (split.length >= 3) {

                int numEtat = Integer.parseInt(split[0]);
                double xPos = Double.parseDouble(split[1]);
                double yPos = Double.parseDouble(split[2]);

                Etat etat = automate.getEtats().get(numEtat);
                VueEtat vueEtat = getVueEtat(etat);

                //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
                double taille = vueEtat.getCercle().getRadius() * 2 + 20;
                if (xPos >= 0 && xPos + taille <= getBoundsInLocal().getMaxX()) {
                    vueEtat.setLayoutX(xPos);
                }
                if (yPos >= 0 && yPos + taille <= getBoundsInLocal().getMaxY() - 50) {
                    vueEtat.setLayoutY(yPos);
                }

            }
            ligne = bf.readLine();
        }


        bf.close();
        fr.close();
    }

    public void sauvegarder(String nomFichier) throws IOException {
        automate.sauvegarder(nomFichier);

        Writer fileWriter = new FileWriter(nomFichier, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("###");
        bufferedWriter.newLine();

        List<Etat> listEtats = automate.getEtats();

        for (Etat e : listEtats) {
            VueEtat vueEtat = getVueEtat(e);
            bufferedWriter.write(listEtats.indexOf(e) + " " + vueEtat.getLayoutX() + " " + vueEtat.getLayoutY());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    public void deSelectionnerVuesEtat() {
        for (Etat e : automate.getEtats()) {
            VueEtat vueEtat = getVueEtat(e);
            vueEtat.deSelectionner();
        }
    }
}




































