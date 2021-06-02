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
import it.polimi.ingsw.psp26.network.server.Server;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

public class EndMatchPhaseState extends PhaseState {

    private String winnerName = "";

    /**
     * Constructor of the class.
     *
     * @param phase current phase.
     */
    public EndMatchPhaseState(Phase phase) {
        super(phase);
    }

    /**
     * Method that calls a method to calculate the points of the game and notifies the player. Then
     * redirect to the start of the game.
     *
     * @param message that contains information on what caused the endgame.
     */
    @Override
    public void execute(SessionMessage message) {

        super.execute(message);

        showEndGameResult(message);

        // Deleting the directory containing the ended Match files
        GameSaver.getInstance().deleteDirectoryByMatchId(phase.getMatchController().getMatch().getId());

        // Making the VirtualView Thread finish its lifecycle
        for (Player player : phase.getMatchController().getMatch().getPlayers())
            phase.getMatchController().getVirtualView().stopListeningNetworkNode(player.getSessionToken(), true);

        // Removing the VirtualView from the Server VirtualViews
        Server.getInstance().removeVirtualView(phase.getMatchController().getVirtualView());

        // Checking if all went correct and no loops remain
        System.out.println("EndMatchPhaseState - VirtualView removed");
    }

    /**
     * Method that checks the winner, computes the points and sends to all player the message with the winner name.
     * <p>
     * it checks if the winner is "Lorenzo il Magnifico" (Only in single player mode), if he is, the method does not
     * calculate the point for the player and sends them the winner name. If he is not, it calculates the points for
     * each player and sends to them the winner name among the players.
     *
     * @param message Session message
     */
    private void showEndGameResult(SessionMessage message) {

        if (message.getMessageType() == BLACK_CROSS_FINAL_POSITION || message.getMessageType() == NO_MORE_COLUMN_DEVELOPMENT_CARDS) {

            winnerName = "Lorenzo il Magnifico";

        } else {

            computePlayersPoints();

        }

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

    /**
     * Method that computes for each player their victory points and establish the player with the most victory points.
     *
     * @return the string with the name of the player with most points.
     */
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

    /**
     * Method to calculate the victory points obtained by a player for their development cards.
     *
     * @param player the player for which it calculates the points.
     * @return the victory points obtained by this source.
     */
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

    /**
     * Method to calculate the victory points obtained by a player from their leader cards.
     *
     * @param player the player for which it calculates the points.
     * @return the victory points obtained by this source.
     */
    private int computeLeaderPoints(Player player) {
        int leaderPoints = 0;
        for (LeaderCard leaderCard : player.getLeaderCards()) {
            if (leaderCard.isActive())
                leaderPoints += leaderCard.getVictoryPoints();
        }
        return leaderPoints;
    }

    /**
     * Method to calculate the victory points obtained by a player for the number of favor tile they activated.
     *
     * @param player the player for which it calculates the points.
     * @return the victory points obtained by this source.
     */
    private int computePopeFavorTilePoints(Player player) {
        int popeFavorTilePoints = 0;

        for (VaticanReportSection section : player.getPersonalBoard().getFaithTrack().getVaticanReportSections()) {

            if (section.isPopesFavorTileActive())
                popeFavorTilePoints += section.getValue();

        }

        return popeFavorTilePoints;
    }
}
