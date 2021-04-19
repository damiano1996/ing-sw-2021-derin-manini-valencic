package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;

import java.util.List;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.goToNextStateAfterLeaderAction;
import static it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid.COLORS;
import static it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid.LEVELS;

public class EndMatchCheckerTurnState extends TurnState {

    public EndMatchCheckerTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        checkMultiPlayerEnd(); // checks in any case

        if (!turn.getMatchController().getMatch().isMultiPlayerMode()) checkSinglePlayerEnd();

        goToNextStateAfterLeaderAction(turn, message);
    }

    /**
     * Method to check if the game has finished.
     * It checks the conditions and modifies the state of the phase of the match.
     */
    private void checkMultiPlayerEnd() {
        if (isSeventhCardDrawn())
            turn.getPlayingPhaseState().goToEndMatchPhaseState(
                    new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.SEVENTH_CARD_DRAWN));
        else if (isFinalTilePosition())
            turn.getPlayingPhaseState().goToEndMatchPhaseState(
                    new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.FINAL_TILE_POSITION));
    }

    /**
     * Method to check if the game has finished with the additional rules of multiplayer mode.
     * It checks the conditions and modifies the state of the phase of the match.
     */
    private void checkSinglePlayerEnd() {
        if (isNoMoreColumnOfDevelopmentCards())
            turn.getPlayingPhaseState().goToEndMatchPhaseState(
                    new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.NO_MORE_COLUMN_DEVELOPMENT_CARDS));
        else if (isBlackCrossFinalPosition())
            turn.getPlayingPhaseState().goToEndMatchPhaseState(
                    new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.BLACK_CROSS_FINAL_POSITION));
    }

    /**
     * Method to check if the seventh card has been drawn.
     *
     * @return true if player has seven cards, false otherwise
     */
    private boolean isSeventhCardDrawn() {
        return turn.getTurnPlayer().getPersonalBoard().getDevelopmentCardsSlots()
                .stream()
                .map(List::size)
                .reduce(0, Integer::sum) >= 7;
    }

    /**
     * Method to check if the marker is on the last position of the faith track.
     *
     * @return true if marker on the last position, false otherwise.
     */
    private boolean isFinalTilePosition() {
        return turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getMarkerPosition() ==
                turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getFinalPosition();
    }

    /**
     * Method to check if the black cross has reached the end of the faith track.
     *
     * @return true if the black cross is on the final position, false otherwise
     */
    private boolean isBlackCrossFinalPosition() {
        return turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getBlackCrossPosition() ==
                turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getFinalPosition();
    }

    /**
     * Method to check if there is an empty column of the development grid.
     *
     * @return true if there is an empty column, false otherwise
     */
    private boolean isNoMoreColumnOfDevelopmentCards() {
        for (Color color : COLORS) {
            try {
                if (!isCardOnColumn(color)) return true;
            } catch (ColorDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Method to check if there is at least one card available on a column of the development card grid.
     *
     * @param color color of the column
     * @return true if there is a card, false otherwise
     * @throws ColorDoesNotExistException if the given color does not exist
     */
    private boolean isCardOnColumn(Color color) throws ColorDoesNotExistException {
        for (Level level : LEVELS) {
            try {
                if (turn.getMatchController().getMatch().getDevelopmentGrid().isAvailable(color, level)) {
                    return true;
                }
            } catch (LevelDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
