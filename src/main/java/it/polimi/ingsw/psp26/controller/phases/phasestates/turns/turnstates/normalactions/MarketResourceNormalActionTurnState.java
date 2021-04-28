package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.ResourcesWarehousePlacerTurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;


public class MarketResourceNormalActionTurnState extends TurnState {
    List<Resource> tempResources;


    public MarketResourceNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(SessionMessage message) {
        // TODO: to implement sub-states
        try {
            switch (message.getMessageType()) {
                case CHOICE_NORMAL_ACTION:
                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    MessageType.CHOICE_ROW_COLUMN,
                                    turn.getMatchController().getMatch().getMarketTray()
                            ));

                    break;
                case CHOICE_ROW_COLUMN:
                    boolean doubleActivation = false;
                    int RowColumnInt = (int) message.getPayload();
                    if (RowColumnInt <= 2) {

                        tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow((RowColumnInt)));
                        turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow((RowColumnInt));

                    } else {

                        tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnColumn((RowColumnInt + 1) % 4));
                        turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToColumn((RowColumnInt + 1) % 4);

                    }

                    if (tempResources.contains(Resource.EMPTY)) doubleActivation = applyMarbleLeaderEffect(message);

                    if (!doubleActivation) {
                        refactorResourceAndChangeTurn(message);

                    }
                    break;

                case CHOICE_RESOURCE:
                    List<Resource> playerResourceChoice = castElements(Resource.class, message.getListPayloads());

                    for (int i = 0; i < tempResources.size(); i++) {
                        if (tempResources.get(i).equals(Resource.EMPTY)) {
                            tempResources.set(i, playerResourceChoice.get(0));
                            playerResourceChoice.remove(0);
                        }
                    }

                    refactorResourceAndChangeTurn(message);

                    break;
            }
        } catch (EmptyPayloadException | InvalidPayloadException ignored) {

        }
    }

    private List<Resource> parseResource() {
        return tempResources = tempResources.stream()
                .filter(x -> !x.equals(Resource.EMPTY))
                .filter(x -> !x.equals(Resource.FAITH_MARKER))
                .collect(Collectors.toList());
    }


    private void refactorResourceAndChangeTurn(SessionMessage message) throws InvalidPayloadException {

        isRedMarblePresent();
        tempResources = parseResource();

//        turn.getMatchController().notifyObservers(
//                new SessionMessage(
//                        turn.getTurnPlayer().getSessionToken(),
//                        MessageType.CHOICE_ROW_COLUMN,
//                        tempResources.toArray(new Object[0])
//                ));

        turn.changeState(new ResourcesWarehousePlacerTurnState(turn, tempResources));
        turn.play(message);
    }

    private void isRedMarblePresent() {
        tempResources.stream()
                .filter(x -> x.equals(Resource.FAITH_MARKER))
                .forEach(x -> turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(1));
    }

    public List<Resource> getTempResources() {
        return tempResources;
    }

    private boolean applyMarbleLeaderEffect(SessionMessage message) {
        List<Resource> tempResourceBeforeLeaders = new ArrayList<>();
        List<Resource> tempResourceLeader = new ArrayList<>();

        tempResourceBeforeLeaders.addAll(tempResources);

        for (LeaderCard leader : turn.getTurnPlayer().getLeaderCards()) {
            leader.execute(this);

            if (tempResourceLeader.size() == 0) {
                tempResourceLeader.addAll(tempResources);

                tempResources = new ArrayList<>();

                tempResources.addAll(tempResourceBeforeLeaders);
            }
        }

        if (tempResourceLeader.size() != 0 && !tempResources.contains(Resource.EMPTY)) {

            List<Resource> leaderResources = new ArrayList<>();
            leaderResources.add(tempResources.get(tempResourceBeforeLeaders.indexOf(Resource.EMPTY)));
            leaderResources.add(tempResourceLeader.get(tempResourceBeforeLeaders.indexOf(Resource.EMPTY)));
            tempResources = tempResourceBeforeLeaders;

            turn.changeState(new OneResourceTurnState(turn,
                    this,
                    (int) tempResources.stream().filter(x -> x.equals(Resource.EMPTY)).count(),
                    leaderResources
            ));
            turn.play(message);
            return true;

        } else {

            if (tempResourceLeader.size() != 0) tempResources = tempResourceLeader;

        }
        return false;

    }
}

