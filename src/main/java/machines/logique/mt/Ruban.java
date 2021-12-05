package machines.logique.mt;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;

public class Ruban {
    private ArrayList<Character> ruban;
    private IntegerProperty teteLecture;

    public Ruban() {
        teteLecture = new SimpleIntegerProperty(1);
        ruban = new ArrayList<>(Arrays.asList('#', '#', '#'));
    }

    public Ruban(String mot) {
        teteLecture = new SimpleIntegerProperty(1);
        ruban = new ArrayList<>();
        ruban.add('#');
        for (int i = 0; i < mot.length(); i++) {
            ruban.add(mot.charAt(i));
        }
        if (mot.length() == 0) ruban.add('#');
        ruban.add('#');
    }

    public void setRuban(String mot) {
        ruban.clear();
        teteLecture = new SimpleIntegerProperty(1);
        ruban.add('#');
        if (mot.length() == 0) {
            ruban.add('#');
        } else {
            for (int i = 0; i < mot.length(); i++) {
                ruban.add(mot.charAt(i));
            }
        }
        ruban.add('#');
    }

    public char lire() {
        return ruban.get(teteLecture.get());
    }

    public void ecrire(char nouvelleLettre, Mouvement mouvement) {
        ruban.set(teteLecture.get(), nouvelleLettre);
        switch (mouvement) {
            case DROITE:
                teteLecture.set(teteLecture.get() + 1);
                if (teteLecture.get() == ruban.size() - 1) ruban.add('#');
                break;
            case GAUCHE:
                teteLecture.set(teteLecture.get() - 1);
                if (teteLecture.get() == 0) {
                    ruban.add(0, '#');
                    teteLecture.set(teteLecture.get() + 1);
                }
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (char c : ruban) {
            result.append(c);
        }
        return result.toString();
    }

    public int getTeteLecture() {
        return teteLecture.get();
    }

    public ReadOnlyIntegerProperty teteLectureProperty() {
        return teteLecture;
    }
}
