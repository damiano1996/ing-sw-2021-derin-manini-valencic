package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.server.memory.LeaderBoard;

import java.util.List;
import java.util.Map;

/**
 * Interface for the views.
 */
public interface ViewInterface {

    /**
     * To initialize and to start the view.
     */
    void start();

    /**
     * Method used to display the login form.
     */
    void displayLogIn();

    /**
     * Method used to display the view that shows the warehouse and the resources to add to it.
     * Implementations should develop the logic to place resources in the warehouse.
     *
     * @param warehouse      warehouse object of the player
     * @param resourcesToAdd list of resources received by the player that must be added
     */
    void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourcesToAdd);

    /**
     * Method to show the development card that can be bought.
     * Implementations should develop the logic to allow players to choose the card to buy.
     *
     * @param developmentCardsGrid object modelling the grid with the development cards
     * @param playerResources      the resources of the player
     */
    void displayDevelopmentCardBuyAction(DevelopmentCardsGrid developmentCardsGrid, List<Resource> playerResources);

    /**
     * Method used to display the market.
     * Implementations should develop the logic to send to the server the selected row or grid.
     *
     * @param marketTray      market object
     * @param playerResources resources of the player
     */
    void displayMarketAction(MarketTray marketTray, List<Resource> playerResources);

    /**
     * Method that allows to select one or more items from the list of objects.
     * Implementations should develop the logic to allow selection and to send items to the server.
     *
     * @param messageType   type of the message in which choices were present
     * @param question      question to display to player
     * @param choices       list of admissible choices
     * @param minChoices    minimum number of items that must be selected
     * @param maxChoices    maximum number of items that can be selected
     * @param hasUndoOption if true, implementations should develop a way to allow players to communicate to the server the will
     *                      to un-do the last operation
     */
    void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption);

    /**
     * Method used to display action tokens.
     *
     * @param unusedTokens list of tokens
     */
    void displayActionTokens(List<ActionToken> unusedTokens);

    /**
     * Method to display a text message.
     *
     * @param text string to show
     */
    void displayText(String text);

    /**
     * Method used to display a view representing the end of the game.
     * It has to contain a leaderboard with the score of the player.
     *
     * @param leaderboard   map with player - points association
     * @param winningPlayer username of the player that has won
     */
    void displayEndGame(Map<String, Integer> leaderboard, String winningPlayer);

    /**
     * Method to display a text message of error.
     *
     * @param error string to display
     */
    void displayError(String error);

    /**
     * Method used to display a waiting screen.
     *
     * @param message message to display with the waiting screen
     */
    void displayWaitingScreen(Message message);

    /**
     * Method used to stop the waiting screen.
     */
    void stopDisplayingWaitingScreen();

    /**
     * Method to communicate to the player that the current turn is played by an opponent.
     */
    void waitForYourTurn();

    /**
     * Method used to display the global leaderboard.
     *
     * @param leaderBoard leaderboard object
     */
    void displayGlobalLeaderboard(LeaderBoard leaderBoard);

    /**
     * Method to reset the views.
     */
    void reset();

}
