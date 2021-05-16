package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MessageSynchronizedFIFO extends Observable<Message> implements Observer<Message> {

    private static MessageSynchronizedFIFO instance;
    private final List<Message> messages;
    private boolean newMessage;

    private MessageSynchronizedFIFO() {
        messages = new ArrayList<>();
        newMessage = false;
    }

    public static MessageSynchronizedFIFO getInstance() {
        if (instance == null)
            instance = new MessageSynchronizedFIFO();
        return instance;
    }

    @Override
    public synchronized void update(Message message) {
        messages.add(message);
        newMessage = true;
        notifyAll();
    }

    public synchronized Message getNext() {
        while (messages.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        newMessage = false;
        return messages.remove(0);
    }

    public synchronized void lookingForNext() {
        while (!newMessage) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
