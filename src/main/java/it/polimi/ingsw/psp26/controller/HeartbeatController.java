package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import static java.lang.Thread.sleep;

public class HeartbeatController extends Observable<SessionMessage> implements Observer<SessionMessage> {

    public final int MAX_TIME_TO_DIE = 5000; // 5 ms
    public final int MAX_TIME_TO_END_MATCH = 1000 * 60 * 5; // 5 minutes
    private final int DELTA_TIME = 1000; // every 100 ms checks countdown

    private final String sessionToken;
    private final VirtualView virtualView;
    private int countdown;
    private boolean running;
    private boolean isDeath;

    public HeartbeatController(String sessionToken, MatchController matchController) {
        this.sessionToken = sessionToken;
        this.virtualView = matchController.getVirtualView();
        addObserver(matchController);
        running = true;
        reset(sessionToken);
    }

    public synchronized void startMonitoringHeartbeat() {
        new Thread(() -> {
            while (running) {

                checksForDeath();

                try {
                    sleep(DELTA_TIME);
                } catch (InterruptedException ignored) {
                }

            }
        }).start();
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
                virtualView.stopListeningNetworkNode(sessionToken);
                notifyObservers(
                        new SessionMessage(sessionToken, MessageType.DEATH)
                );
//                System.out.println("HeartbeatController - No heartbeat from player with sessionToken: " + sessionToken);
//                running = false;
            } catch (InvalidPayloadException ignored) {
            }
        }

        // If countdown exceed, we declare the end of the match
        if (countdown < -MAX_TIME_TO_END_MATCH) {
            try {
                notifyObservers(
                        new SessionMessage(sessionToken, MessageType.INDEFINITE_SUSPENSION)
                );
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
}
