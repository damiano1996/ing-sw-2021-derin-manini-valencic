package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnState;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendSessionMessageToAllPlayers;

public class CheckVaticanReportTurnState extends TurnState {
    public CheckVaticanReportTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(SessionMessage message) {

        super.play(message);

        for(Player player : turn.getMatchController().getMatch().getPlayers()) {

            activateVaticanReport(player);

        }

        turn.changeState(new EndMatchCheckerTurnState(turn));
        turn.play(message);
    }


    private boolean isPlayerInVaticanSection(Player player, VaticanReportSection section) {

        return player.getPersonalBoard().getFaithTrack().getFaithPoints() > section.getStartSection();
    }

    private boolean firstPlayerInPopeSpace(Player player) {
        return player.getPersonalBoard().getFaithTrack().getFaithPoints() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection();

    }

    private boolean blackCrossInPopeSpace(Player player) {
        return player.getPersonalBoard().getFaithTrack().getBlackCrossPosition() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection();

    }

    private void activateVaticanReport(Player currentPlayer) {

        if (firstPlayerInPopeSpace(currentPlayer) || blackCrossInPopeSpace(currentPlayer)) {

            try {
                sendSessionMessageToAllPlayers(turn.getMatchController(), new NotificationUpdateMessage(turn.getTurnPlayer().getSessionToken(), "A vatican report has been called" ));
            } catch (InvalidPayloadException e) {
                e.printStackTrace();
            }

            int sectionActivated = getFirstActiveSectionIndex();

            for (Player player : turn.getMatchController().getMatch().getPlayers()) {

                VaticanReportSection[] playerSections = player.getPersonalBoard().getFaithTrack().getVaticanReportSections();

                if (isPlayerInVaticanSection(player, playerSections[sectionActivated]))
                    playerSections[sectionActivated].activatePopesFavorTile();

                sendNotification(player);
            }
        }
    }

    private void sendNotification(Player player){
        String reportResult;
        reportResult = turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive() ? "activated" : "deactivated";
        try {
            sendSessionMessageToAllPlayers(turn.getMatchController(),
                    new NotificationUpdateMessage(player.getSessionToken(),
                            "Faith points: " + player.getPersonalBoard().getFaithTrack().getFaithPoints() +  " so the favor tile is " + reportResult));
        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        }

    }

    private int getFirstActiveSectionIndex() {

        int sectionNumber = 0;
        VaticanReportSection activeSection = turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[sectionNumber];

        for (Player player : turn.getMatchController().getMatch().getPlayers()) {
            for (int i = 0; i < player.getPersonalBoard().getFaithTrack().getVaticanReportSections().length; i++) {
                if (player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].isPopesFavorTileActive()
                        && player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].getStartSection()
                        > activeSection.getStartSection()) {

                    activeSection = player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[i];
                    sectionNumber = i + 1;
                }

            }
        }

        return Math.max(sectionNumber, getBlackCrossSectionIndex());

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
