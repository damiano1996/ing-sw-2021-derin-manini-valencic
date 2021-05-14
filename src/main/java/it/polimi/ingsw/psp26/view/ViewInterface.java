package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;

import java.util.List;
import java.util.Map;

public interface ViewInterface {

    void start();


    void displayLogIn();


    void displayLeaderCards(List<LeaderCard> leaderCards);


    void displayNotifications(List<String> notifications);


    void displayPersonalBoard(Player player, boolean isMultiplayerMode);


    void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourceToAdd);


    void displayDevelopmentCardBuyAction(DevelopmentGrid developmentGrid, List<Resource> playerResources);


    void displayMarketAction(MarketTray marketTray, List<Resource> playerResources);


    void displayFaithTrack(FaithTrack faithTrack, boolean isMultiplayerMode);


    void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots);


    void displayDevelopmentGrid(DevelopmentGrid developmentGrid);


    void displayResourceSupply(ResourceSupply resourceSupply, List<Resource> resourcesTypes);


    void displayProductionActivation(List<Production> productions, List<Resource> playerResources);


    void displayMarketScreen(MarketTray marketTray);


    void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption);


    void displayActionTokens(List<ActionToken> unusedTokens);


    void displayText(String text);


    void displayEndGame(Map<String, Integer> leaderboard);


    void displayError(String error);


    void displayWaitingScreen(Message message);


    void stopDisplayingWaitingScreen();
    
    
    void waitForYourTurn();
            
}
