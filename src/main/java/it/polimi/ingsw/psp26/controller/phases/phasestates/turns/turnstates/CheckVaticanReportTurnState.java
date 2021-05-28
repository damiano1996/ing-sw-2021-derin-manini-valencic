package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnState;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;
import it.polimi.ingsw.psp26.network.SpecialToken;

public class CheckVaticanReportTurnState extends TurnState {
    private boolean lastVaticanReportCalled = false;

    /**
     * Constructor of the class.
     *
     * @param turn Current turn.
     */
    public CheckVaticanReportTurnState(Turn turn) {
        super(turn);
    }

    /**
     * Method that calls the method activateVaticanReportSection for each player and then redirects the current player
     * to the next phase.
     *
     * @param message Session message
     */
    @Override
    public void play(SessionMessage message) {

        super.play(message);

        for (Player player : turn.getMatchController().getMatch().getPlayers()) {

            activateVaticanReport(player);

        }

        turn.changeState(new EndMatchCheckerTurnState(turn));
        turn.play(message);
    }


    /**
     * Method to check if a given player is inside or over a given vatican report section.
     *
     * @param player  The player to check.
     * @param section The section to check.
     * @return true if the player is inside or over the section, false otherwise.
     */
    private boolean isPlayerInVaticanSectionOrOver(Player player, VaticanReportSection section) {

        return player.getPersonalBoard().getFaithTrack().getFaithPoints() >= section.getStartSection();
    }

    /**
     * Method to check if the current player is the first one to reach or go beyond a pope space.
     *
     * @param player Current player.
     * @return True if they are the first player to pass over a pope space.
     */

    private boolean firstPlayerInPopeSpace(Player player) {
        return player.getPersonalBoard().getFaithTrack().getFaithPoints() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection();

    }

    /**
     * Method to check if the black cross is the first one to reach or go beyond a pope space.
     *
     * @param player To retrieve the black cross in the player faith track.
     * @return True if it is the first one to pass over a pope space.
     */
    private boolean isBlackCrossInPopeSpaceOrOver(Player player) {
        return player.getPersonalBoard().getFaithTrack().getBlackCrossPosition() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection();

    }

    /**
     * Method to check if a player during the current turn activated the vatican report. If yes it checks for each
     * player if they are inside the vatican report section and consequently activate or discard the favor tile and
     * sends for each one of the a message in broadcast to containing this information.
     *
     * @param currentPlayer the player is in this turn state.
     */
    private void activateVaticanReport(Player currentPlayer) {

        if (!lastVaticanReportCalled) {

            if (firstPlayerInPopeSpace(currentPlayer) || isBlackCrossInPopeSpaceOrOver(currentPlayer)) {

                if (getFirstActiveSectionIndex() == currentPlayer.getPersonalBoard().getFaithTrack().getVaticanReportSections().length)
                    lastVaticanReportCalled = true;

                try {

                    turn.getMatchController().notifyObservers(new NotificationUpdateMessage(SpecialToken.BROADCAST.getToken(), "A vatican report has been called."));

                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                }

                int sectionActivated = getFirstActiveSectionIndex();

                for (Player player : turn.getMatchController().getMatch().getPlayers()) {

                    VaticanReportSection[] playerSections = player.getPersonalBoard().getFaithTrack().getVaticanReportSections();

                    if (isPlayerInVaticanSectionOrOver(player, playerSections[sectionActivated])) {

                        playerSections[sectionActivated].activatePopesFavorTile();

                    } else {

                        playerSections[sectionActivated].discardPopesFavorTile();

                    }

                    sendNotification(player);
                }
            }
        }
    }

    /**
     * Method to send to all the player in the match, a message.
     *
     * @param player the player that is the object of the message.
     */
    private void sendNotification(Player player) {
        String reportResult;
        reportResult = turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive() ? "activated" : "deactivated";
        try {
            turn.getMatchController().notifyObservers(
                    new NotificationUpdateMessage(
                            SpecialToken.BROADCAST.getToken(),
                            "Faith points: " + player.getPersonalBoard().getFaithTrack().getFaithPoints() + " so the favor tile is " + reportResult + ".")
            );
        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to determine the section index of the first tile of the faith track that no one has passed.
     *
     * @return the index of the first tile that no one has passed.
     */
    private int getFirstActiveSectionIndex() {

        int sectionNumber = 0;

        for (int i = 0; i < turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections().length; i++) {
            if (turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].isPopesFavorTileActive()
                    || turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].isPopesFavorTileDiscarded()) {

                sectionNumber = i + 1;

            }
        }

        return sectionNumber;

    }

}
