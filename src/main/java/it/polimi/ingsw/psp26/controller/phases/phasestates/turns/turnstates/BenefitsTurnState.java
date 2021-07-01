package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.ResourcesWarehousePlacerTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendGeneralMessage;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;

public class BenefitsTurnState extends TurnState {

    /**
     * Class constructor.
     *
     * @param turn the turn which state is in the benefits turn state
     */
    public BenefitsTurnState(Turn turn) {
        super(turn);
    }

    /**
     * Method that checks the turn number and gives to the corresponding player an amount of resources and possibly a
     * faith point.
     * <p>
     * Method that is activated only if the turn number is lower than the current size of players, in order to activate
     * it only one time. It gives them a predefined number of resources and faith points accordingly to the rules.
     *
     * @param message the message that is played
     */
    @Override
    public void play(SessionMessage message) {
        System.out.println("BenefitsTurnState - " + message.toString());

        // skipping if first turn played by all players
        if (turn.getTurnNumber() > turn.getMatchController().getMatch().getPlayers().size() - 1) {
            goToChooseLeaderAction(message);

        } else {

            try {
                switch (turn.getTurnNumber()) {
                    case 0:
                        sendGeneralMessage(turn, "You are the first player. Catch the inkwell!");
                        System.out.println("BenefitsTurnState - assigning the inkwell.");
                        turn.getTurnPlayer().giveInkwell();
                        System.out.println("BenefitsTurnState - going to ChooseLeaderAction.");
                        goToChooseLeaderAction(message);
                        break;

                    case 1:
                        assignResources(message, 1, false);
                        break;

                    case 2:
                        assignResources(message, 1, true);
                        break;

                    case 3:
                        assignResources(message, 2, true);
                        break;
                }
            } catch (EmptyPayloadException ignored) {
            }
        }

    }

    /**
     * Method that the first time that is called ask the player the resources, the second time, by checking the message
     * it allows them to place them in the warehouse.
     *
     * @param message        the message passed by play
     * @param numOfResources the number of resources that are given to the player
     * @param faithPoint     if true it gives to the player one faith point, zero otherwise
     * @throws EmptyPayloadException if the payload of the message is empty
     */
    private void assignResources(SessionMessage message, int numOfResources, boolean faithPoint) throws EmptyPayloadException {
        System.out.println("BenefitsTurnState - assigning resources " + message.toString());

        if (message.getMessageType().equals(CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY)) {
            List<Resource> resources = castElements(Resource.class, message.getListPayloads());

            if (faithPoint)
                turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(1);

            turn.changeState(new ResourcesWarehousePlacerTurnState(turn, resources));

        } else {
            sendGeneralMessage(turn,
                    "You are the player number: " + (turn.getTurnNumber() + 1) + ". " +
                            ((faithPoint) ? "You get 1 FP. " : "") +
                            "You can choose " + numOfResources + " resources."
            );
            turn.changeState(new OneResourceTurnState(turn, this, numOfResources, false));
        }

        turn.play(message);
    }

    /**
     * Method that change the turn state of to the next one (ChooseLeaderAction).
     *
     * @param message the message that is played by then next turn state
     */
    private void goToChooseLeaderAction(SessionMessage message) {
        turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_TO_NORMAL_ACTION));
        turn.play(message);
    }

}
