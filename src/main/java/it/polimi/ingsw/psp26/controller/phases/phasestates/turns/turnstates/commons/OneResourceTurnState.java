package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendGeneralMessage;
import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;

public class OneResourceTurnState extends TurnState {

    private final TurnState nextState;
    private final int numOfResources;
    private final List<Resource> resources;

    public OneResourceTurnState(Turn turn, TurnState nextState, int numOfResources) {
        super(turn);
        this.nextState = nextState;
        this.numOfResources = numOfResources;
        resources = new ArrayList<>();
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        try {
            System.out.println("OneResourceTurnState - " + message.toString());

            if (message.getMessageType().equals(MessageType.CHOICE_RESOURCE)) {

                Resource resource = (Resource) message.getPayload();
                resources.add(resource);

                if (resources.size() == numOfResources) {

                    turn.changeState(nextState);
                    turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_RESOURCE, resources.toArray()));
                } else {

                    sendGeneralMessage(turn, "Choose resource:");
                    sendChoiceResourceMessage();
                }

            } else {
                sendChoiceResourceMessage();
            }

        } catch (EmptyPayloadException ignored) {

        }

    }

    private void sendChoiceResourceMessage() {
        System.out.println("OneResourceTurnState - sending message to " + turn.getTurnPlayer().getNickname());

        turn.getMatchController().notifyObservers(
                new MultipleChoicesMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_RESOURCE,
                        "Choice resource:",
                        1, 1,
                        (Object[]) RESOURCES_SLOTS
                )
        );
    }
}
