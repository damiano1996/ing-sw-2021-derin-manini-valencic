package it.polimi.ingsw.psp26.application.messages.specialpayloads;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;

import java.util.List;

public class MarketPayload {
    private final MarketTray market;
    private final int[] rowColumnInts = {0, 1, 2, 3, 4, 5, 6};

    public MarketPayload(MarketTray market) {
        this.market = market;
    }

    public MarketTray getMarket() {
        return this.market;
    }

    public int[] getResourcesToAdd() {
        return rowColumnInts;
    }
}
