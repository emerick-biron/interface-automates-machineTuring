package machines.logique.mt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;

public class Ruban {
    private ArrayList<Character> ruban;
    private int teteLecture;

    public Ruban() {
        teteLecture = 1;
        ruban = new ArrayList<>(Arrays.asList('#', '#', '#'));
    }

    public Ruban(String mot) {
        teteLecture = 1;
        ruban = new ArrayList<>();
        ruban.add('#');
        for (int i = 0; i < mot.length(); i++) {
            ruban.add(mot.charAt(i));
        }
        if (mot.length() == 0) ruban.add('#');
        ruban.add('#');
    }

    public void setRuban(String mot){
        ruban.clear();
        teteLecture = 1;
        ruban.add('#');
        if (mot.length() == 0){
            ruban.add('#');
        }else {
            for (int i = 0; i < mot.length(); i++) {
                ruban.add(mot.charAt(i));
            }
        }
        ruban.add('#');
    }

    public char lire() {
        return ruban.get(teteLecture);
    }

    public int getTeteLecture() {
        return teteLecture;
    }

    public void ecrire(char nouvelleLettre, Mouvement mouvement) {
        ruban.set(teteLecture, nouvelleLettre);
        switch (mouvement) {
            case DROITE:
                teteLecture++;
                if (teteLecture == ruban.size() - 1) ruban.add('#');
                break;
            case GAUCHE:
                teteLecture--;
                if (teteLecture == 0) {
                    ruban.add(0, '#');
                    teteLecture++;
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


}
