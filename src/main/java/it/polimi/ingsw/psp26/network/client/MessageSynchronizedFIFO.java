package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent a FIFO containing Messages sent by the Server.
 */
public class MessageSynchronizedFIFO extends Observable<Message> implements Observer<Message> {

    private static MessageSynchronizedFIFO instance;
    private final List<Message> messages;

    /**
     * Constructor of the class.
     * Initialize a new messages List.
     */
    private MessageSynchronizedFIFO() {
        messages = new ArrayList<>();
    }


    /**
     * Getter of the MessageSynchronisedFIFO instance.
     *
     * @return the MessageSynchronisedFIFO instance
     */
    public synchronized static MessageSynchronizedFIFO getInstance() {
        if (instance == null)
            instance = new MessageSynchronizedFIFO();
        return instance;
    }


    /**
     * When a new Message arrives, adds it to the FIFO.
     *
     * @param message the Message to add
     */
    @Override
    public synchronized void update(Message message) {
        messages.add(message);
        notifyAll();
    }


    /**
     * Returns and removes the first Message contained in the FIFO.
     * If the FIFO size is 0, makes the calling Thread wait.
     *
     * @return the first FIFO Message
     */
    public synchronized Message getNext() {
        while (messages.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        return messages.remove(0);
    }


    /**
     * Method to stop the calling thread if there aren't new messages.
     * It is similar to getNext(), but without returning or removing messages from the FIFO.
     */
    public synchronized void lookingForNext() {
        while (messages.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

}
