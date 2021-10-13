package fr.umontpellier.iut.logique;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        Automate automate = new Automate();

        automate.chargerFichier("automates_txt/input.txt");
        Task<Void> taskLancer = automate.getTaskLancer("abb", 1000);

    }
}
