package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import static java.lang.Thread.sleep;

public class HeartbeatController extends Observable<SessionMessage> implements Observer<SessionMessage> {

    public static final int MAX_TIME_TO_DIE = 5000; // 5 ms
    public static final int MAX_TIME_TO_END_MATCH = 1000 * 60 * 5; // 5 minutes
    private static final int DELTA_TIME = 1000; // every 100 ms checks countdown

    private final String sessionToken;
    private final VirtualView virtualView;
    private int countdown;
    private boolean running;
    private boolean isDeath;
    private Thread thread;

    public HeartbeatController(String sessionToken, MatchController matchController) {
        this.sessionToken = sessionToken;
        this.virtualView = matchController.getVirtualView();
        addObserver(matchController);
        running = true;
        reset(sessionToken);
    }

    public synchronized void startMonitoringHeartbeat() {
        thread = new Thread(() -> {
            while (running) {

                checksForDeath();

                try {
                    //no inspection BusyWait
                    sleep(DELTA_TIME);
                } catch (InterruptedException ignored) {
                }
            }
        });
        thread.start();
    }

    public synchronized void reset(String sessionToken) {
        if (this.sessionToken.equals(sessionToken)) {
            countdown = MAX_TIME_TO_DIE;
            running = true;
            isDeath = false;
        }
    }

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
                System.out.println("A message of INDEFINITE_SUSPENSION IS SENT");
            } catch (InvalidPayloadException ignored) {
            }
        }
    }

    @Override
    public synchronized void update(SessionMessage message) {
        if (message.getMessageType().equals(MessageType.HEARTBEAT))
                reset(message.getSessionToken());

    }

    public synchronized boolean isDeath() {
        return isDeath;
    }

    public synchronized void kill(String sessionToken) {
        if (this.sessionToken.equals(sessionToken))
            this.running = false;

        try {
        if(thread != null)
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
