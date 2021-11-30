package machines.gui.automates;

import javafx.collections.SetChangeListener;
import javafx.stage.Screen;
import javafx.stage.Stage;
import machines.gui.VueEtat;
import machines.gui.VueTransition;
import machines.logique.Etat;
import machines.logique.Transition;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import machines.gui.VueMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VueAutomate extends VueMachine {
    private SetChangeListener<Transition> miseAJourTransitions = change -> {
        if (change.wasAdded()) {
            Transition t = change.getElementAdded();
            ajoutVueTransition(t);
        } else if (change.wasRemoved()) {
            Transition t = change.getElementRemoved();
            supprimerVueTransition(t);
        }
    };
    private SetChangeListener<Etat> miseAJourEtats = change -> {
        if (change.wasAdded()) change.getElementAdded().transitionsProperty().addListener(miseAJourTransitions);
    };

    public VueAutomate(Automate automate, VuePrincipaleAtmt vuePrincipale) {
        super(automate, vuePrincipale);
        automate.etatsProperty().addListener(miseAJourEtats);
    }

    public Automate getAutomate() {
        return (Automate) super.getMachine();
    }

    @Override
    public void ajoutVueTransition(Transition transition) {
        VueTransitionAtmt vueTransition = new VueTransitionAtmt((TransitionAtmt) transition, VueAutomate.this);
        getChildren().add(vueTransition);
        vueTransition.toBack();
        int nbrTrans = 0;
        for (Transition t : transition.getEtatDepart().getListeTransitions()) {
            if (t.getEtatArrivee() == transition.getEtatArrivee()) nbrTrans++;
        }
        vueTransition.positionnerLabelEtiquette(nbrTrans - 1);
    }

    public void chargerFichier(String nomFichier) throws IOException {
        clear();
        getAutomate().chargerFichier(nomFichier);

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
        getAutomate().sauvegarder(nomFichier);

        Writer fileWriter = new FileWriter(nomFichier, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("###");
        bufferedWriter.newLine();

        Stage primaryStage = getVuePrincipale().getApp().getPrimaryStage();

        bufferedWriter
                .write("DIM: " + primaryStage.getWidth() + " " + primaryStage.getHeight() + " " + getWidth() + " " +
                        getHeight());
        bufferedWriter.newLine();

        for (Etat e : getAutomate().getEtats()) {
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
}




































