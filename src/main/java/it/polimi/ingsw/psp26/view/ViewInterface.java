package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.controller.turns.actions.leader.LeaderAction;
import it.polimi.ingsw.psp26.controller.turns.actions.normal.NormalAction;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;

import java.util.HashMap;
import java.util.List;

public interface ViewInterface {

    void displayLogIn();

    void displayMatchSelection();

    void displayLeaderCardsDrawn(List<LeaderCard> leaderCards);

    void displayInkwell();

    void displayInitialResources(List<Resource> resources);

    void displayPersonalBoard(PersonalBoard personalBoard);

    void displayWarehouseDepots(List<Depot> warehouseDepot);

    void displayStrongbox(List<Resource> strongbox);

    void displayFaithTrack(FaithTrack faithTrack);

    void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots);

    void displayNormalActionsSelection(List<NormalAction> normalActions);

    void displayMarketTray(MarketTray marketTray);

    void displayMarketTrayResourcesSelection(MarketTray marketTray);

    void displayDevelopmentGrid(DevelopmentGrid developmentGrid);

    void displayResourceSupply(ResourceSupply resourceSupply);

    void displayProductionActivation(PersonalBoard personalBoard);

    void displayVaticanReport(FaithTrack faithTrack);

    void displayLeaderActionSelection(List<LeaderAction> leaderActions);

    void displayLeaderCardDiscardSelection(List<LeaderCard> leaderCards);

    void displayLeaderCardActivation(List<LeaderCard> leaderCards);

    void displayActionTokens(ActionToken actionToken);

    void displayDevelopmentCardDiscard(DevelopmentGrid developmentGrid, DevelopmentCardType developmentCardType);

    void displayText(String text);

    void displayEndGame(HashMap<String, Integer> playersVictoryPoints);

    void displayError(String error);
}
