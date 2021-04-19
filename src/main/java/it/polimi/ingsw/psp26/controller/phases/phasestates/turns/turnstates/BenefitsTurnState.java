package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.ResourcesWarehousePlacer;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class BenefitsTurnState extends TurnState {


    public BenefitsTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(SessionMessage message) {
        System.out.println("BenefitsTurnState - " + message.toString());

        // TODO: give leader cards...

        switch (turn.getTurnNumber()) {
            case 3:
                turn.getTurnPlayer().giveInkwell();
                break;

            case 1:
                assignResources(message, 1, false);
                break;

            case 2:
                assignResources(message, 1, true);
                break;

            case 0:
                assignResources(message, 2, true);
                break;
        }

        // next state is...
        // step 2: leader action (-> check vatican report -> check endgame)
        // turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_TO_NORMAL_ACTION));
    }

    public void assignResources(SessionMessage message, int numOfResources, boolean faithPoint) {
        System.out.println("BenefitsTurnState - assigning resources " + message.toString());

        if (message.getMessageType().equals(CHOICE_RESOURCE)) {
            List<Resource> resources = castElements(Resource.class, message.getListPayloads());

            if (faithPoint)
                turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(1);

            turn.changeState(new ResourcesWarehousePlacer(turn, resources));
            turn.play(message);

        } else {
            turn.changeState(new OneResourceTurnState(turn, this, numOfResources));
            turn.play(message);
        }
    }

}
