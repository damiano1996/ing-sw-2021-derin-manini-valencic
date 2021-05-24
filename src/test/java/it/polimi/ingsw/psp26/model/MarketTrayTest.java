package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MarketTrayTest {

    private MarketTray marketTray;

    @Before
    public void setUp() {
        marketTray = new MarketTray(new VirtualView());
    }

    @Test
    public void testGetMarblesOnRow() {
        assertNotNull(marketTray.getMarblesOnRow(0));
    }

    @Test
    public void testGetMarbleOnColumn() {
        assertNotNull(marketTray.getMarblesOnColumn(0));
    }

    @Test
    public void testGetMarbleOnSlide() {
        assertNotNull(marketTray.getMarbleOnSlide());
    }

    @Test
    public void testPushMarbleFromSlideToRow_TrueCase() {
        Resource[] MarketRow = marketTray.getMarblesOnRow(0).clone();
        for (int i = 0; i < MarketRow.length + 1; i++) {
            marketTray.pushMarbleFromSlideToRow(0);
        }
        assertArrayEquals(MarketRow, marketTray.getMarblesOnRow(0));
    }

    @Test
    public void testPushMarbleFromSlideToRow_FalseCase() {
        Resource[] MarketRow = marketTray.getMarblesOnRow(0).clone();
        for (int i = 0; i < MarketRow.length; i++) {
            marketTray.pushMarbleFromSlideToRow(0);
        }
        assertFalse(Arrays.equals(MarketRow, marketTray.getMarblesOnRow(0)));
    }

    @Test
    public void testPushMarbleFromSlideToColumn_TrueCase() {
        Resource[] MarketColumn = marketTray.getMarblesOnColumn(0).clone();
        for (int i = 0; i < MarketColumn.length + 1; i++) {
            marketTray.pushMarbleFromSlideToColumn(0);
        }
        assertArrayEquals(MarketColumn, marketTray.getMarblesOnColumn(0));
    }

    @Test
    public void testPushMarbleFromSlideToColumn_FalseCase() {
        Resource[] MarketColumn = marketTray.getMarblesOnColumn(0).clone();
        for (int i = 0; i < MarketColumn.length; i++) {
            marketTray.pushMarbleFromSlideToColumn(0);
        }
        assertFalse(Arrays.equals(MarketColumn, marketTray.getMarblesOnColumn(0)));
    }

}