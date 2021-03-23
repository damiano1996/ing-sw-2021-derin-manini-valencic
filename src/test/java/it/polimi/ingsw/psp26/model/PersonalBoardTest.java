package it.polimi.ingsw.psp26.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonalBoardTest {

    PersonalBoard personalBoard;

    @Before
    public void setUp() {
    }

    @Test
    public void testGetFaithTrack() {
        personalBoard = new PersonalBoard();
        FaithTrack faithTrack = new FaithTrack();
        assertTrue(faithTrack.equals(personalBoard.getFaithTrack()));
    }

    @Test
    public void testGetDevelopmentCardsSlots() {
    }

    @Test
    public void testGetDevelopmentCardsSlot() {
    }

    @Test
    public void testGetVisibleDevelopmentCards() {
    }

    @Test
    public void testGetWarehouseDepots() {
    }

    @Test
    public void testGetWarehouseDepot() {
    }

    @Test
    public void testGetStrongbox() {
    }

    @Test
    public void testCanPlaceCard() {
    }
}