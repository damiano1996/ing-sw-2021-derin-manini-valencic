package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

import java.util.HashMap;
import java.util.List;

public interface ViewInterface {

    void displayLogIn();


    void displayLeaderCards(List<LeaderCard> leaderCards);


    void displayInkwell(boolean isPrintable);


    void displayInitialResources(List<Resource> resources);


    void displayPersonalBoard(Player player);


    void displayWarehouseDepots(List<Depot> warehouseDepot);


    void displayStrongbox(List<Resource> strongbox);


    void displayFaithTrack(FaithTrack faithTrack);


    void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots);


    void displayMarketTray(MarketTray marketTray);


    void displayDevelopmentGrid(DevelopmentGrid developmentGrid);


    void displayResourceSupply(ResourceSupply resourceSupply);


    void displayProductionActivation(Player player);


    void displayMarketScreen(MarketTray marketTray);


    void displayDevelopmentGridCardSelection(DevelopmentGrid developmentGrid);


    void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices);


    void displayActionTokens(ActionToken actionToken);


    void displayDevelopmentCardDiscard(DevelopmentGrid developmentGrid, DevelopmentCardType developmentCardType);


    void displayText(String text);


    void displayEndGame(HashMap<String, Integer> playersVictoryPoints);


    void displayError(String error);
}
