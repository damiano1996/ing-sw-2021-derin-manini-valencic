package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import static java.lang.Thread.sleep;

/**
 * Class used to track the status of the connection of the clients.
 * It expects periodic signals from the client nodes.
 * If signals stop to arrive, this class changes the internal state allowing the client to re-establish
 * the connection in a specified amount of time.
 * If client establishes the connection again, the internal state will be reset.
 * Otherwise, the HeartbeatController will send a special message to the observers to notify the death of the client.
 */
public class HeartbeatController extends Observable<SessionMessage> implements Observer<SessionMessage> {

    public static final int MAX_TIME_TO_DIE = 5000; // 5000 ms
    public static final int MAX_TIME_TO_END_MATCH = 1000 * 60 * 5; // 5 minutes
    private static final int DELTA_TIME = 1000; // every 1000 ms checks countdown

    private final String sessionToken;
    private final VirtualView virtualView;
    private int countdown;
    private boolean running;
    private boolean isDeath;
    private Thread thread;

    /**
     * Class constructor.
     *
     * @param sessionToken    token of the tracked player
     * @param matchController in which the player is playing
     */
    public HeartbeatController(String sessionToken, MatchController matchController) {
        this.sessionToken = sessionToken;
        this.virtualView = matchController.getVirtualView();
        addObserver(matchController);
        running = true;
        reset(sessionToken);
    }

    /**
     * Method to start the background activity of the heartbeat controller.
     * It decreases periodically a counter. The counter can be reset by the signal received by the client.
     * If no signal will be received it will enter in the state of waiting for recovery.
     * After a certain amount of time, it will declare the death.
     */
    public synchronized void startMonitoringHeartbeat() {
        thread = new Thread(() -> {
            while (running) {

                checksForDeath();

                try {
                    //noinspection BusyWait
                    sleep(DELTA_TIME);
                } catch (InterruptedException ignored) {
                }
            }
        });
        thread.start();
    }

    /**
     * Method to reset the countdown.
     * Reset is performed only if passed session token is the same of the tracked player.
     *
     * @param sessionToken session token of the observed player
     */
    public synchronized void reset(String sessionToken) {
        if (this.sessionToken.equals(sessionToken)) {
            countdown = MAX_TIME_TO_DIE;
            running = true;
            isDeath = false;
        }
    }

    /**
     * Method to check and to update the countdown.
     * It sends messages (periodically) to the observers in case of death.
     * A final HEARTBEAT_INDEFINITE_SUSPENSION will be sent in case of missed recovery.
     */
    private synchronized void checksForDeath() {
        countdown -= DELTA_TIME;

        if (countdown < 0) {
            isDeath = true;
            try {

                virtualView.stopListeningNetworkNode(sessionToken, false);
                System.out.println("HeartbeatController - Sending DEATH message to the match controller." + sessionToken);
                notifyObservers(new SessionMessage(sessionToken, MessageType.DEATH));

            } catch (InvalidPayloadException ignored) {
            }
        }

        // If countdown exceed, we declare the end of the match
        if (countdown < -MAX_TIME_TO_END_MATCH) {
            try {
                notifyObservers(new SessionMessage(sessionToken, MessageType.HEARTBEAT_INDEFINITE_SUSPENSION));
                System.out.println("HeartbeatController - A message of INDEFINITE_SUSPENSION IS SENT");
            } catch (InvalidPayloadException ignored) {
            }
        }
    }

    /**
     * Method to update the class.
     * If called, and the session message is of type HEARTBEAT, it will reset this.
     *
     * @param message session message
     */
    @Override
    public synchronized void update(SessionMessage message) {
        if (message.getMessageType().equals(MessageType.HEARTBEAT))
            reset(message.getSessionToken());

    }

    /**
     * Boolean method to get the death status.
     *
     * @return true if heartbeat not received for too long time
     */
    public synchronized boolean isDeath() {
        return isDeath;
    }

    /**
     * Method to kill the thread, running in background, that is used to update the countdown.
     * Only if session token is the same of the tracked client.
     *
     * @param sessionToken session token of a player
     */
    public synchronized void kill(String sessionToken) {
        if (this.sessionToken.equals(sessionToken)) {
            this.running = false;

            try {
                if (thread != null)
                    thread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
