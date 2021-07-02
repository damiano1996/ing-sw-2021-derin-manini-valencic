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

/**
 * Class that is used to ask a player each resource type they want, one at time, for a list of resource spots.
 */
public class OneResourceTurnState extends TurnState {

    private final TurnState nextState;
    private final int numOfResources;
    private final MessageType resourceSource;
    private final List<Resource> resources;

    private List<Resource> resourcesOptions;

    /**
     * Constructor of the class.
     *
     * @param turn             the current turn
     * @param nextState        the following turn state
     * @param numOfResources   the number of resources
     * @param toPay            if true the resource are taken from the player, otherwise the resources are taken from the supply
     * @param resourcesOptions the list of resources available
     */
    public OneResourceTurnState(Turn turn, TurnState nextState, int numOfResources, boolean toPay, List<Resource> resourcesOptions) {
        super(turn);
        this.nextState = nextState;
        this.numOfResources = numOfResources;
        this.resourceSource = getResourceSource(toPay);
        resources = new ArrayList<>();
        this.resourcesOptions = resourcesOptions;
    }

    /**
     * Constructor of the class.
     *
     * @param turn           the current turn
     * @param nextState      the following turn state
     * @param numOfResources the number of resources
     * @param toPay          if true the resource are taken from the player, otherwise the resources are taken from the supply
     */
    public OneResourceTurnState(Turn turn, TurnState nextState, int numOfResources, boolean toPay) {
        super(turn);
        this.nextState = nextState;
        this.numOfResources = numOfResources;
        this.resourceSource = getResourceSource(toPay);
        resources = new ArrayList<>();
        this.resourcesOptions = Arrays.asList(RESOURCES_SLOTS);
    }

    /**
     * Method to define the source of resources.
     *
     * @param toPay if true the resource are taken from the player, otherwise the resources are taken from the supply
     * @return message type of the corresponding source
     */
    private MessageType getResourceSource(boolean toPay) {
        return (toPay) ? MessageType.CHOICE_RESOURCE_FROM_WAREHOUSE : MessageType.CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY;
    }

    /**
     * Method that ask recursively to the current player a resource to choose until the number of resources requested is
     * reached. Then it passes the list of resources as a message to the next state.
     * <p>
     * The first time the method sends to the player a message to choose a resource and waits for the answer. When the
     * answer arrives if the number of resources requested is equal at the number of chosen resources it stops and go to
     * the next phase sending the list of resource as the payload. If not it sends again to the player a message to
     * choose a resource.
     *
     * @param message the message that is played
     */
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

    /**
     * Method that send a message to the current player the list of which resource to choose.
     */
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
                            "Choose resource:",
                            1, 1,
                            false,
                            resourcesOptions.toArray(new Object[0])
                    )
            );
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * It creates a map with each type of resources as a key and one as the integer.
     *
     * @return a map of resources and integers
     */
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

    /**
     * Method that check which resource type of the player are available.
     *
     * @return list of resource type that are available
     */
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
