package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.view.cli.CLI;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args) {
        try {
            Client client = new Client();
            CLI cli = new CLI(client);
            client.setViewInterface(cli);

            cli.displayLogIn();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
