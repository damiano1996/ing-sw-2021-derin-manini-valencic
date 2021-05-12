package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.*;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendGeneralMessage;
import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;

public class OneResourceTurnState extends TurnState {

    private final TurnState nextState;
    private final int numOfResources;
    private final MessageType resourceSource;
    private final List<Resource> resources;

    private List<Resource> resourcesOptions;

    public OneResourceTurnState(Turn turn, TurnState nextState, int numOfResources, boolean toPay, List<Resource> resourcesOptions) {
        super(turn);
        this.nextState = nextState;
        this.numOfResources = numOfResources;
        this.resourceSource = getResourceSource(toPay);
        resources = new ArrayList<>();
        this.resourcesOptions = resourcesOptions;
    }

    public OneResourceTurnState(Turn turn, TurnState nextState, int numOfResources, boolean toPay) {
        super(turn);
        this.nextState = nextState;
        this.numOfResources = numOfResources;
        this.resourceSource = getResourceSource(toPay);
        resources = new ArrayList<>();
        this.resourcesOptions = Arrays.asList(RESOURCES_SLOTS);
    }

    private MessageType getResourceSource(boolean toPay) {
        return (toPay) ? MessageType.CHOICE_RESOURCE_FROM_WAREHOUSE : MessageType.CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY;
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        try {
            System.out.println("OneResourceTurnState - " + message.toString());

            if (message.getMessageType().equals(this.resourceSource)) {

                Resource resource = (Resource) message.getPayload();
                if (resourcesOptions.contains(resource)) {
                    resources.add(resource);

                    if (resources.size() == numOfResources) {

                        turn.changeState(nextState);
                        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), this.resourceSource, resources.toArray()));

                    } else {

                        sendGeneralMessage(turn, "Choose resource:");
                        sendChoiceResourceMessage();
                    }
                } else {
                    sendGeneralMessage(turn, "Resource is not admissible. Choose among: " + resourcesOptions);
                    sendChoiceResourceMessage();
                }

            } else {
                sendChoiceResourceMessage();
            }

        } catch (EmptyPayloadException | InvalidPayloadException ignored) {
        }

    }

    private void sendChoiceResourceMessage() {
        System.out.println("OneResourceTurnState - sending message to " + turn.getTurnPlayer().getNickname());

        if (resourceSource.equals(MessageType.CHOICE_RESOURCE_FROM_WAREHOUSE)) {
            resourcesOptions = getAvailableResources();
        }

        try {
            turn.getMatchController().notifyObservers(
                    new MultipleChoicesMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            resourceSource,
                            "Choice resource:",
                            1, 1,
                            false,
                            resourcesOptions.toArray(new Object[0])
                    )
            );
        } catch (InvalidPayloadException ignored) {
        }
    }

    private Map<Resource, Integer> getResourcesMultiplicity() {

        Map<Resource, Integer> multiplicity = new HashMap<>();
        for (Resource resource : turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources()) {

            if (multiplicity.containsKey(resource)) {
                multiplicity.put(resource, multiplicity.get(resource) + 1);
            } else {
                multiplicity.put(resource, 1);
            }
        }

        return multiplicity;
    }

    private List<Resource> getAvailableResources() {
        Map<Resource, Integer> multiplicity = getResourcesMultiplicity();
        for (Resource resource : resources) {
            if (multiplicity.containsKey(resource))
                multiplicity.put(resource, multiplicity.get(resource) - 1);
        }

        List<Resource> availableResources = new ArrayList<>();
        for (Resource resource : multiplicity.keySet()) {
            if (multiplicity.get(resource) > 0) availableResources.add(resource);
        }

        return availableResources;
    }
}
