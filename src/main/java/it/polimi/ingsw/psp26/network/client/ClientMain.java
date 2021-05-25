package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.view.ViewInterface;
import it.polimi.ingsw.psp26.view.cli.CLI;
import it.polimi.ingsw.psp26.view.gui.GUI;

public class ClientMain {

    /**
     * Starts the Client
     */
    public static void main(String[] args) {
        boolean cliMode = false;

        if (args.length > 0) {
            for (String arg : args) {
                if (arg.contains("-playingViewMode")) {
                    String[] argSplit = arg.split("=");
                    cliMode = argSplit[1].equals("cli");
                    break;
                }
            }
        }

        ViewInterface viewInterface = (cliMode) ? new CLI() : new GUI();
        viewInterface.start();
    }
}
