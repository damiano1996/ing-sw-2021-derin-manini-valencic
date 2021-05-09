package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendSessionMessageToAllPlayers;

public class LorenzoMagnificoTurnState extends TurnState {

    public LorenzoMagnificoTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        List<ActionToken> allTokens = new ArrayList<>(turn.getMatchController().getMatch().getActionTokens());
        ActionToken actionToken = turn.getMatchController().getMatch().drawActionTokens(1).get(0);

        try {
            actionToken.execute(turn.getTurnPlayer().getPersonalBoard().getFaithTrack(),
                    turn.getMatchController().getMatch().getDevelopmentGrid());

            afterExecute(allTokens, actionToken.getTokenName());

        } catch (ColorDoesNotExistException | LevelDoesNotExistException | LorenzoWinException | InvalidPayloadException e) {
            e.printStackTrace();
        } catch (MustShuffleActionTokenStackException e) {
            try {
                afterExecute(allTokens, actionToken.getTokenName());
            } catch (InvalidPayloadException ignored) {
            }
            turn.getMatchController().getMatch().initializeActionTokenStack();
        }

        turn.changeState(new CheckVaticanReportTurnState(turn));
        //turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_TO_NORMAL_ACTION));
        turn.play(message);
    }


    private void afterExecute(List<ActionToken> allTokens, String tokenPlayed) throws InvalidPayloadException {
        sendSessionMessageToAllPlayers(turn.getMatchController(), new NotificationUpdateMessage(turn.getTurnPlayer().getSessionToken(), "Lorenzo played                " + tokenPlayed));

        turn.getMatchController().notifyObservers(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.LORENZO_PLAY,
                        allTokens.toArray()
                )
        );
    }


}
