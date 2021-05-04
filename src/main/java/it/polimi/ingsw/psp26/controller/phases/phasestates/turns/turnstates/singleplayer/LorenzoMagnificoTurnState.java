package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnState;
import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LorenzoWinException;
import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;

public class LorenzoMagnificoTurnState extends TurnState {

    public LorenzoMagnificoTurnState(Turn turn) { super(turn);  }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        ActionToken actionToken = turn.getMatchController().getMatch().drawActionTokens(1).get(0);

        try {

            actionToken.execute(turn.getTurnPlayer().getPersonalBoard().getFaithTrack(),
                    turn.getMatchController().getMatch().getDevelopmentGrid());

        } catch (MustShuffleActionTokenStackException | ColorDoesNotExistException | LevelDoesNotExistException | LorenzoWinException e) {
            e.printStackTrace();
        }

        turn.changeState(new CheckVaticanReportTurnState(turn));


        turn.play(message);

    }
}
