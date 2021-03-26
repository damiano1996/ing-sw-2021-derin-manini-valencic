package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.model.enums.Resource;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MarketTrayTest {

    private MarketTray Market;

    @Test
    public void testGetMarblesOnRow() {
        Market = new MarketTray();
        assertNotNull(Market.getMarblesOnRow(0));
    }

    @Test
    public void testGetMarbleOnColumn() {
        Market = new MarketTray();
        assertNotNull(Market.getMarbleOnColumn(0));
    }

    @Test
    public void testGetMarbleOnSlide() {
        Market = new MarketTray();
        assertNotNull(Market.getMarbleOnSlide());
    }

    @Test
    public void testPushMarbleFromSlideToRow_TrueCase() {
        Market = new MarketTray();
        Resource[] MarketRow = Market.getMarblesOnRow(0).clone();
        for (int i = 0; i < MarketRow.length + 1; i++) {
            Market.pushMarbleFromSlideToRow(0);
        }
        assertArrayEquals(MarketRow, Market.getMarblesOnRow(0));
    }

    @Test
    public void testPushMarbleFromSlideToRow_FalseCase() {
        Market = new MarketTray();
        Resource[] MarketRow = Market.getMarblesOnRow(0).clone();
        for (int i = 0; i < MarketRow.length; i++) {
            Market.pushMarbleFromSlideToRow(0);
        }
        assertFalse(Arrays.equals(MarketRow, Market.getMarblesOnRow(0)));
    }

    @Test
    public void testPushMarbleFromSlideToColumn_TrueCase() {
        Market = new MarketTray();
        Resource[] MarketColumn = Market.getMarbleOnColumn(0).clone();
        for (int i = 0; i < MarketColumn.length + 1; i++) {
            Market.pushMarbleFromSlideToColumn(0);
        }
        assertArrayEquals(MarketColumn, Market.getMarbleOnColumn(0));
    }

    @Test
    public void testPushMarbleFromSlideToColumn_FalseCase() {
        Market = new MarketTray();
        Resource[] MarketColumn = Market.getMarbleOnColumn(0).clone();
        for (int i = 0; i < MarketColumn.length; i++) {
            Market.pushMarbleFromSlideToColumn(0);
        }
        assertFalse(Arrays.equals(MarketColumn, Market.getMarbleOnColumn(0)));
    }
}