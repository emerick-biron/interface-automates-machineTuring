package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import fr.umontpellier.iut.logique.Transition;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VueAutomate extends Pane {
    private Automate automate;
    private VuePrincipale vuePrincipale;
    private VueEtat vueEtatSelectionne = null;
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
                    VueTransition vueTransition = new VueTransition(t, VueAutomate.this);
                    getChildren().add(vueTransition);
                    vueTransition.toBack();
                }
            } else if (change.wasRemoved()) {
                for (Transition t : change.getRemoved()) {
                    getChildren().remove(getVueTransition(t));
                }
            }

        }
    };

    public VueAutomate(Automate automate, VuePrincipale vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.automate = automate;
        this.automate.etatsProperty().addListener(miseAJourEtats);
        this.automate.transitionsProperty().addListener(miseAJourTransition);

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getTarget() == this) {
                vuePrincipale.setDefaultActionSouris();
                vueEtatSelectionne = null;
            }
        });
    }

    public VueEtat getVueEtatSelectionne() {
        return vueEtatSelectionne;
    }

    public void setVueEtatSelectionne(VueEtat vueEtatSelectionne) {
        this.vueEtatSelectionne = vueEtatSelectionne;
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

            if (split.length < 3) break;

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

            ligne = bf.readLine();
        }


        bf.close();
        fr.close();
    }
}




































