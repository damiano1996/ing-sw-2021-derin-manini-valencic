package it.polimi.ingsw.psp26.testutils;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MitmObserver implements Observer<SessionMessage> {

    private final List<SessionMessage> messages;

    public MitmObserver() {
        messages = new ArrayList<>();
    }

    @Override
    public void update(SessionMessage message) {
        messages.add(message);
    }

    public List<SessionMessage> getMessages() {
        return messages;
    }

}