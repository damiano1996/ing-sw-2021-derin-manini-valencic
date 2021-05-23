package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.SpecialToken;

import static java.lang.Thread.sleep;

public class HeartbeatController extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final int MAX_TIME = 2000; // milliseconds
    private final int DELTA_TIME = 100; // every 100 ms checks countdown

    private final String sessionToken;
    private final MatchController matchController;
    private int countdown;
    private boolean running;

    public HeartbeatController(String sessionToken, MatchController matchController) {
        this.sessionToken = sessionToken;
        this.matchController = matchController;
        addObserver(this.matchController);
        running = true;
        reset();
    }

    public void startMonitoringHeartbeat() {
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

    private synchronized void reset() {
        System.out.println("HeartbeatController - Reset counter, player with sessionToken: " + sessionToken + " is alive.");
        countdown = MAX_TIME;
        running = true;
    }

    private synchronized void checksForDeath() {
        countdown -= DELTA_TIME;

        if (countdown < 0) {
            try {
                notifyObservers(
                        new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.DEATH, sessionToken)
                );
                System.out.println("HeartbeatController - No heartbeat from player with sessionToken: " + sessionToken);
                running = false;
            } catch (InvalidPayloadException ignored) {
            }
        }
    }

    @Override
    public synchronized void update(SessionMessage message) {
        if (message.getMessageType().equals(MessageType.HEARTBEAT) && message.getSessionToken().equals(sessionToken))
            reset();
    }
}
