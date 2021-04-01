package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;

public class VirtualView extends Observable<Message> implements Observer<Message> {

    @Override
    public void update(Message message) {

    }
}
