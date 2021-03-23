package it.polimi.ingsw.psp26.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {

    FaithTrack faithTrack;
    VaticanReportSection[] vaticanReportSections = new VaticanReportSection[3];

    @Before
    public void setUp() {
        vaticanReportSections[0] = new VaticanReportSection(5, 8);
        vaticanReportSections[1] = new VaticanReportSection(12, 16);
        vaticanReportSections[2] = new VaticanReportSection(19, 24);
    }

    @Test
    public void testGetVaticanReportSections() {
        faithTrack = new FaithTrack();
        for (int i = 0; i < 3; i++)
            assertEquals(vaticanReportSections[i].isPopesFavorTileActive(), faithTrack.getVaticanReportSections()[i].isPopesFavorTileActive());
        for (int i = 0; i < 3; i++)
            assertEquals(vaticanReportSections[i].getStartSection(), faithTrack.getVaticanReportSections()[i].getStartSection());
        for (int i = 0; i < 3; i++)
            assertEquals(vaticanReportSections[i].getEndSection(), faithTrack.getVaticanReportSections()[i].getEndSection());
    }

    @Test
    public void testGetMarkerPosition() {
        faithTrack = new FaithTrack();
        assertEquals(0, faithTrack.getMarkerPosition());
    }

    @Test
    public void testGetMarkerPosition_CasePositionIncreased() {
        faithTrack = new FaithTrack();
        faithTrack.moveMarkerPosition(5);
        assertEquals(5, faithTrack.getMarkerPosition());
    }

    @Test
    public void testGetBlackCrossPosition() {
        faithTrack = new FaithTrack();
        assertEquals(0, faithTrack.getBlackCrossPosition());
    }

    @Test
    public void testGetBlackCrossPosition_CasePositionIncreased() {
        faithTrack = new FaithTrack();
        faithTrack.moveBlackCrossPosition(2);
        assertEquals(2, faithTrack.getBlackCrossPosition());
    }

    @Test
    public void testGetFaithPoints() {
        faithTrack = new FaithTrack();
        assertEquals(0, faithTrack.getFaithPoints());

    }

    @Test
    public void testGetFaithPoints_CasePointsIncreased() {
        faithTrack = new FaithTrack();
        faithTrack.addFaithPoints(7);
        assertEquals(7, faithTrack.getFaithPoints());
    }

    @Test
    public void testGetFaithPoints_CasePointsIncreasedWithPosition() {
        faithTrack = new FaithTrack();
        faithTrack.moveMarkerPosition(3);
        assertEquals(3, faithTrack.getFaithPoints());
    }

    @Test
    public void testEquals_TrueCase() {
        faithTrack = new FaithTrack();
        FaithTrack faithTrack1 = new FaithTrack();
        assertTrue(faithTrack.equals(faithTrack1));
    }

    @Test
    public void testEquals_FalseCase() {
        faithTrack = new FaithTrack();
        FaithTrack faithTrack1 = new FaithTrack();
        faithTrack.addFaithPoints(5);
        faithTrack.moveBlackCrossPosition(7);
        assertFalse(faithTrack.equals(faithTrack1));

    }
}