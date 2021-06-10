package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;

import java.util.List;
import java.util.Map;

public interface ViewInterface {

    void start();


    void displayLogIn();
    

    void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourcesToAdd);


    void displayDevelopmentCardBuyAction(DevelopmentCardsGrid developmentCardsGrid, List<Resource> playerResources);


    void displayMarketAction(MarketTray marketTray, List<Resource> playerResources);
    

    void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption);


    void displayActionTokens(List<ActionToken> unusedTokens);


    void displayText(String text);


    void displayEndGame(Map<String, Integer> leaderboard, String winningPlayer);


    void displayError(String error);


    void displayWaitingScreen(Message message);


    void stopDisplayingWaitingScreen();


    void waitForYourTurn();


    void displayGlobalLeaderboard();
    
}
