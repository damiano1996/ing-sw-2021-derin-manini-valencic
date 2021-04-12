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
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;

import java.util.HashMap;
import java.util.List;

public interface ViewInterface {

    void displayLogIn();

    /**
     * @deprecated use {@link #displayChoices} instead.
     */
    @Deprecated
    void displayMatchSelection();

    @Deprecated
    void displayLeaderChoice(List<LeaderCard> leaderCards);

    void displayLeaderCardsDrawn(List<LeaderCard> leaderCards);

    void displayInkwell(boolean isPrintable, int startingRow, int startingColumn);

    void displayInitialResources(List<Resource> resources);

    void displayPersonalBoard(Player player);

    void displayWarehouseDepots(List<Depot> warehouseDepot, int startingRow, int startingColumn);

    void displayStrongbox(List<Resource> strongbox, int startingRow, int startingColumn);

    void displayFaithTrack(FaithTrack faithTrack, int startingRow, int startingColumn);

    void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots, int startingRow, int startingColumn);

    @Deprecated
    void displayNormalActionsSelection();

    void displayMarketTray(MarketTray marketTray, int startingRow, int startingColumn);

    @Deprecated
    void displayMarketResourcesSelection(List<Depot> depots, List<Resource> resources);

    void displayDevelopmentGrid(DevelopmentGrid developmentGrid);

    void displayResourceSupply(ResourceSupply resourceSupply);

    void displayProductionActivation(PersonalBoard personalBoard);

    @Deprecated
    void displayLeaderActionSelection();

    @Deprecated
    void displayLeaderCardDiscardSelection(List<LeaderCard> leaderCards);

    void displayActionToken(ActionToken actionToken);

    void displayDevelopmentCardDiscard(DevelopmentGrid developmentGrid, DevelopmentCardType developmentCardType);

    void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices);

    void displayText(String text);

    void displayEndGame(HashMap<String, Integer> playersVictoryPoints);

    void displayError(String error);
}
