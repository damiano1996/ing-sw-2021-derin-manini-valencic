package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {

    FaithTrack faithTrack;
    VaticanReportSection[] vaticanReportSections;

    @Before
    public void setUp() {
        VirtualView virtualView = new VirtualView();
        vaticanReportSections = new VaticanReportSection[3];

        vaticanReportSections[0] = new VaticanReportSection(virtualView, 5, 8, 2);
        vaticanReportSections[1] = new VaticanReportSection(virtualView, 12, 16, 3);
        vaticanReportSections[2] = new VaticanReportSection(virtualView, 19, 24, 4);

        faithTrack = new FaithTrack(virtualView);
    }

    @Test
    public void testGetVaticanReportSections() {
        for (int i = 0; i < 3; i++)
            assertEquals(vaticanReportSections[i], faithTrack.getVaticanReportSections()[i]);
    }

    @Test
    public void testGetMarkerPosition() {
        assertEquals(0, faithTrack.getMarkerPosition());
    }

    @Test
    public void testGetMarkerPosition_CasePositionIncreased() {
        faithTrack.moveMarkerPosition(5);
        assertEquals(5, faithTrack.getMarkerPosition());
    }

    @Test
    public void testGetBlackCrossPosition() {
        assertEquals(0, faithTrack.getBlackCrossPosition());
    }

    @Test
    public void testGetBlackCrossPosition_CasePositionIncreased() {
        faithTrack.moveBlackCrossPosition(2);
        assertEquals(2, faithTrack.getBlackCrossPosition());
    }

    @Test
    public void testGetFaithPoints() {
        assertEquals(0, faithTrack.getFaithPoints());

    }

    @Test
    public void testGetFaithPoints_CasePointsIncreased() {
        faithTrack.addFaithPoints(7);
        assertEquals(7, faithTrack.getFaithPoints());
    }

    @Test
    public void testGetFaithPoints_CasePointsIncreasedWithPosition() {
        faithTrack.moveMarkerPosition(3);
        assertEquals(3, faithTrack.getFaithPoints());
    }

    @Test
    public void testEquals_TrueCase() {
        FaithTrack faithTrack1 = new FaithTrack(new VirtualView());
        assertEquals(faithTrack, faithTrack1);
    }

    @Test
    public void testEquals_FalseCase() {
        FaithTrack faithTrack1 = new FaithTrack(new VirtualView());
        faithTrack.addFaithPoints(5);
        faithTrack.moveBlackCrossPosition(7);
        assertNotEquals(faithTrack, faithTrack1);
    }
}