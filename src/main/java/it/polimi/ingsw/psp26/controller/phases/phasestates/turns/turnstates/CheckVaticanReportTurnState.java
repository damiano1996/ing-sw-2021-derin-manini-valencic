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
    public CheckVaticanReportTurnState(Turn turn) {
        super(turn);
    }

    private boolean lastVaticanReportCalled = false;

    @Override
    public void play(SessionMessage message) {

        super.play(message);

        for (Player player : turn.getMatchController().getMatch().getPlayers()) {

            activateVaticanReport(player);

        }

        turn.changeState(new EndMatchCheckerTurnState(turn));
        turn.play(message);
    }


    private boolean isPlayerInVaticanSectionOrOver(Player player, VaticanReportSection section) {

        return player.getPersonalBoard().getFaithTrack().getFaithPoints() > section.getStartSection();
    }

    private boolean firstPlayerInPopeSpace(Player player) {
        return player.getPersonalBoard().getFaithTrack().getFaithPoints() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection();

    }

    private boolean isBlackCrossInPopeSpaceOrOver(Player player) {
        return player.getPersonalBoard().getFaithTrack().getBlackCrossPosition() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection();

    }

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

    private int getBlackCrossSectionIndex() {

        int blackCrossIndex = 0;

        for (int i = 0; i < turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections().length; i++) {

            if (turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getBlackCrossPosition() >=
                    turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].getEndSection()) {
                blackCrossIndex = i + 1;

            }

        }

        return blackCrossIndex;
    }

}
