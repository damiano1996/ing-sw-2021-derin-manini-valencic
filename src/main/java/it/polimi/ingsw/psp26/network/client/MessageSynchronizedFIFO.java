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


    /**
     * @return The SynchronisedFIFO instance
     */
    public static MessageSynchronizedFIFO getInstance() {
        if (instance == null)
            instance = new MessageSynchronizedFIFO();
        return instance;
    }


    /**
     * When a new Message arrives, adds it to the FIFO
     *
     * @param message The Message to add
     */
    @Override
    public synchronized void update(Message message) {
        messages.add(message);
        newMessage = true;
        notifyAll();
    }


    /**
     * Returns and removes the first Message contained in the FIFO
     * If the FIFO size is 0, makes the calling Thread wait
     *
     * @return The first FIFO Message
     */
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


    /**
     * Checks if new messages arrive
     */
    public synchronized void lookingForNext() {
        while (!newMessage) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

}
