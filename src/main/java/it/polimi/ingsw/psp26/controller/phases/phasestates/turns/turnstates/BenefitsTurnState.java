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


    public BenefitsTurnState(Turn turn) {
        super(turn);
    }

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
                        //TODO usare solo per test poi rimuovere
//                        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(23);


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

    private void goToChooseLeaderAction(SessionMessage message) {
        turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_TO_NORMAL_ACTION));
        turn.play(message);
    }

}
