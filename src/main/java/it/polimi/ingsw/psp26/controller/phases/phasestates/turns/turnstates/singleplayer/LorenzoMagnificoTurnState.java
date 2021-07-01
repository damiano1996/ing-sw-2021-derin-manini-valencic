package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.network.SpecialToken;

import java.util.ArrayList;
import java.util.List;


public class LorenzoMagnificoTurnState extends TurnState {

    /**
     * Constructor of the class.
     *
     * @param turn      the turn in which this turn state is in
     * @param turnPhase the turn phase in which this turn state is in
     */
    public LorenzoMagnificoTurnState(Turn turn, TurnPhase turnPhase) {
        super(turn);
        turn.setTurnPhase(turnPhase);
    }

    /**
     * Method that draws an action token, executes it, notify the player and then change to the next turn state.
     *
     * @param message the message that is played
     */
    @Override
    public void play(SessionMessage message) {
        super.play(message);

        List<ActionToken> allTokens = new ArrayList<>(turn.getMatchController().getMatch().getActionTokens());
        ActionToken actionToken = turn.getMatchController().getMatch().drawActionTokens(1).get(0);

        try {
            actionToken.execute(turn.getTurnPlayer().getPersonalBoard().getFaithTrack(),
                    turn.getMatchController().getMatch().getDevelopmentGrid());

            afterExecute(allTokens, actionToken.toString());

        } catch (ColorDoesNotExistException | LevelDoesNotExistException | LorenzoWinException | InvalidPayloadException e) {
            e.printStackTrace();
        } catch (MustShuffleActionTokenStackException e) {
            try {
                afterExecute(allTokens, actionToken.toString());
            } catch (InvalidPayloadException ignored) {
            }
            turn.getMatchController().getMatch().initializeActionTokenStack();
        }

        turn.changeState(new CheckVaticanReportTurnState(turn));
        turn.play(message);
    }

    /**
     * Method that notifies the player about the token played and all other ones.
     *
     * @param allTokens   all the lorenzo's tokens
     * @param tokenPlayed the token played this turn
     * @throws InvalidPayloadException if payload of the message is not admissible
     */
    private void afterExecute(List<ActionToken> allTokens, String tokenPlayed) throws InvalidPayloadException {
        turn.getMatchController().notifyObservers(new NotificationUpdateMessage(SpecialToken.BROADCAST.getToken(), "Lorenzo played " + tokenPlayed + "."));

        turn.getMatchController().notifyObservers(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.LORENZO_PLAY,
                        allTokens.toArray()
                )
        );
    }


}
