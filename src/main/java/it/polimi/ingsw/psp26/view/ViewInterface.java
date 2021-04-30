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

import java.util.HashMap;
import java.util.List;

public interface ViewInterface {

    void displayLogIn();


    void displayLeaderCards(List<LeaderCard> leaderCards);


    void displayInkwell(boolean isPrintable);


    void displayPersonalBoard(Player player, boolean isMultiplayerMode);


    void displayWarehouseDepots(Warehouse warehouse);


    void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourceToAdd);


    void displayDevelopmentCardBuyAction(DevelopmentGrid developmentGrid);


    void displayStrongbox(List<Resource> strongbox);


    void displayMarketAction(MarketTray marketTray);


    void displayFaithTrack(FaithTrack faithTrack, boolean isMultiplayerMode);


    void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots);


    void displayMarketTray(MarketTray marketTray);


    void displayDevelopmentGrid(DevelopmentGrid developmentGrid);


    void displayResourceSupply(ResourceSupply resourceSupply);


    void displayProductionActivation(List<Production> productions);


    void displayMarketScreen(MarketTray marketTray);


    void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasQuitOption);


    void displayActionTokens(List<ActionToken> unusedTokens);


    void displayText(String text);


    void displayEndGame(HashMap<String, Integer> playersVictoryPoints);


    void displayError(String error);


    void displayWaitingScree(Message message);


    void stopDisplayingWaitingScreen();
}
