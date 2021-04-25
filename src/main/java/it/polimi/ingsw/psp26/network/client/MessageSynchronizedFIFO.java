package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MessageSynchronizedFIFO extends Observable<Message> implements Observer<Message> {

    private static MessageSynchronizedFIFO instance;
    private final List<Message> messages;

    private MessageSynchronizedFIFO() {
        messages = new ArrayList<>();
    }

    public static MessageSynchronizedFIFO getInstance() {
        if (instance == null)
            instance = new MessageSynchronizedFIFO();
        return instance;
    }

    @Override
    public synchronized void update(Message message) {
        messages.add(message);
        notifyAll();
    }

    public synchronized Message getNext() {
        while (messages.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        return messages.remove(0);
    }
}
