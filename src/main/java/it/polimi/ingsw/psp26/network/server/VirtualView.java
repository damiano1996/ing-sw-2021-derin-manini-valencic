package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.MatchController;

import java.io.IOException;

public class VirtualView extends Observable<Message> implements Observer<Message> {

    private final MatchController matchController;
    private final Server server;

    public VirtualView(Server server) {
        this.server = server;
        matchController = new MatchController(this, getMatchId());
    }

    @Override
    public void update(Message message) {
        // it receives notification from model/controller and it has to notify the "real" view
        try {
            server.sendToClient(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forwardToMatchController(Message message) {
        // it receives message from the communication channel and it has to forward the message to the controller
        matchController.update(message);
    }

    private int getMatchId() {
        return 0; // TODO: if we want an incremental id we should implement a way to retrieve the last assigned id
    }

    public MatchController getMatchController() {
        return matchController;
    }
}
