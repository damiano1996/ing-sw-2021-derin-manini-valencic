package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.LeaderBoard;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

public class EndMatchPhaseState extends PhaseState {

    private String winnerName;

    public EndMatchPhaseState(Phase phase) {
        super(phase);
    }

    @Override
    public void execute(SessionMessage message) {
        super.execute(message);

        if (message.getMessageType() == BLACK_CROSS_FINAL_POSITION || message.getMessageType() == NO_MORE_COLUMN_DEVELOPMENT_CARDS) {
            winnerName = "Lorenzo il Magnifico";
        }

        showEndGameResult();
    }

    private void showEndGameResult() {

        if (winnerName != "Lorenzo il Magnifico")
            computePlayersPoints();

        for (Player player : phase.getMatchController().getMatch().getPlayers()) {
            try {
                phase.getMatchController().notifyObservers(
                        new SessionMessage(
                                player.getSessionToken(),
                                ENDGAME_RESULT,
                                winnerName
                        ));
            } catch (InvalidPayloadException e) {
                e.printStackTrace();
            }
        }

    }

    private String computePlayersPoints() {

        int playerPoints;
        int winnerPoints = 0;
        LeaderBoard leaderboard = LeaderBoard.getInstance();

        for (Player player : phase.getMatchController().getMatch().getPlayers()) {

            playerPoints = 0;

            playerPoints += computeDevelopmentCardPoints(player);
            playerPoints += player.getPersonalBoard().getFaithTrack().getVictoryPoints();
            playerPoints += player.getPersonalBoard().getAllAvailableResources().size() / 5;
            playerPoints += computeLeaderPoints(player);
            playerPoints += computePopeFavorTilePoints(player);

            player.addPoints(playerPoints);

            winnerPoints = Math.max(winnerPoints, playerPoints);

            leaderboard.addPlayerVictoryPoints(player.getNickname(), playerPoints);

            if (winnerPoints == playerPoints)
                winnerName = player.getNickname();

        }
        return winnerName;
    }


    private int computeDevelopmentCardPoints(Player player) {

        int developmentCardVictoryPoints = 0;

        try {
            for (int i = 0; i < player.getPersonalBoard().getDevelopmentCardsSlots().size(); i++) {
                for (DevelopmentCard playerCard : player.getPersonalBoard().getDevelopmentCardsSlot(i)) {

                    developmentCardVictoryPoints += playerCard.getVictoryPoints();

                }
            }
        } catch (DevelopmentCardSlotOutOfBoundsException e) {
            e.printStackTrace();
        }

        return developmentCardVictoryPoints;
    }

    private int computeLeaderPoints(Player player) {
        int leaderPoints = 0;
        for (LeaderCard leaderCard : player.getLeaderCards()) {
            if (leaderCard.isActive())
                leaderPoints += leaderCard.getVictoryPoints();
        }
        return leaderPoints;
    }

    private int computePopeFavorTilePoints(Player player) {
        int popeFavorTilePoints = 0;

        for (VaticanReportSection section : player.getPersonalBoard().getFaithTrack().getVaticanReportSections()) {

            if (section.isPopesFavorTileActive())
                popeFavorTilePoints += section.getValue();

        }

        return popeFavorTilePoints;
    }
}
