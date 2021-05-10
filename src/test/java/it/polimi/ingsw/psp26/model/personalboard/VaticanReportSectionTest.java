package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VaticanReportSectionTest {

    private VaticanReportSection section;

    @Before
    public void setUp() {
        VirtualView virtualView = new VirtualView();
        Player player = new Player(virtualView, "nickname", "sessionToken");
        virtualView.getMatchController().getMatch().addPlayer(player);
        section = new VaticanReportSection(virtualView, 1, 5, 1, "sessionToken");
    }

    @Test
    public void testIsPopesFavorTileActive_FalseCase() {
        assertFalse(section.isPopesFavorTileActive());
    }

    @Test
    public void testIsPopesFavorTileActive_TrueCase() {
        section.activatePopesFavorTile();
        assertTrue(section.isPopesFavorTileActive());
    }

    @Test
    public void testIsInVaticanReportSection_FalseCase() {
        assertFalse(section.isInVaticanReportSection(10));
    }

    @Test
    public void testIsinVaticanReportSection_TrueCase() {
        assertTrue(section.isInVaticanReportSection(3));
    }

    @Test
    public void testGetStartSection() {
        assertEquals(1, section.getStartSection());
    }

    @Test
    public void testGetEndSection() {
        assertEquals(5, section.getEndSection());
    }

    @Test
    public void testGetValue() {
        assertEquals(1, section.getValue());
    }
}