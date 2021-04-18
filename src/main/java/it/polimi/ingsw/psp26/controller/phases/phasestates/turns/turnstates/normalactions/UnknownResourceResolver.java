package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;

public class UnknownResourceResolver extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private static UnknownResourceResolver instance;

    private Player player;

    private boolean isResourceDefined;
    private Resource resource;

    private UnknownResourceResolver(Observer<SessionMessage> observer) {
        super();
        addObserver(observer);
        isResourceDefined = false;
    }

    public static UnknownResourceResolver getInstance(Observer<SessionMessage> observer) {
        if (instance == null)
            instance = new UnknownResourceResolver(observer);

        return instance;
    }

    @Override
    public void update(SessionMessage message) {
        if (message.getMessageType().equals(MessageType.CHOICE_RESOURCE) &&
                player.getSessionToken().equals(message.getSessionToken())) {
            resource = (Resource) message.getPayload();
            isResourceDefined = true;
            notifyAll();
        }
    }

    public Resource getResourceDefined(Player player) throws InterruptedException {
        this.player = player;
        notifyObservers(
                new SessionMessage(
                        player.getSessionToken(),
                        MessageType.CHOICE_RESOURCE,
                        Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD
                )
        );

        while (!isResourceDefined) wait();

        isResourceDefined = false;
        return resource;
    }
}
