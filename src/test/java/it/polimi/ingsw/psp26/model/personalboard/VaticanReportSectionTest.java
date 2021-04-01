package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VaticanReportSectionTest {

    private VaticanReportSection section;
    private VirtualView virtualView;

    @Before
    public void setUp() {
        virtualView = new VirtualView();
    }

    @Test
    public void testIsPopesFavorTileActive_FalseCase() {
        section = new VaticanReportSection(virtualView, 1, 5);
        assertFalse(section.isPopesFavorTileActive());
    }

    @Test
    public void testIsPopesFavorTileActive_TrueCase() {
        section = new VaticanReportSection(virtualView, 1, 5);
        section.activatePopesFavorTile();
        assertTrue(section.isPopesFavorTileActive());
    }

    @Test
    public void testIsInVaticanReportSection_FalseCase() {
        section = new VaticanReportSection(virtualView, 5, 9);
        assertFalse(section.isInVaticanReportSection(10));
    }

    @Test
    public void testIsinVaticanReportSection_TrueCase() {
        section = new VaticanReportSection(virtualView, 5, 9);
        assertTrue(section.isInVaticanReportSection(6));
    }

    @Test
    public void testGetStartSection() {
        section = new VaticanReportSection(virtualView, 2, 5);
        assertEquals(2, section.getStartSection());
    }

    @Test
    public void testGetEndSection() {
        section = new VaticanReportSection(virtualView, 2, 5);
        assertEquals(5, section.getEndSection());
    }
}