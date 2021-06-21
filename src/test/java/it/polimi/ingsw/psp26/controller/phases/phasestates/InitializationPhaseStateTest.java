package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.files.Files;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;
import it.polimi.ingsw.psp26.network.server.memory.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_FILES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InitializationPhaseStateTest {

    private Phase phase;

    @Before
    public void setUp() {
        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.changeState(new InitializationPhaseState(phase));
    }

    @After
    public void tearDown() {
        GameSaver.getInstance().deleteDirectoryByMatchId(phase.getMatchController().getMatch().getId());
        Files.deleteFile(GAME_FILES + "nickname-password.json");
        Files.deleteFile(GAME_FILES + "nickname-sessionToken.json");
    }

    @Test
    public void testAddPlayer() throws InvalidPayloadException {
        String nickname = "nickname";
        String password = "password";
        String sessionToken = "sessionToken";

        Users.getInstance().addUser(nickname, password, sessionToken);

        phase.execute(new SessionMessage(
                sessionToken,
                MessageType.ADD_PLAYER,
                nickname
        ));

        assertEquals(phase.getMatchController().getMatch().getPlayers().get(0).getNickname(), nickname);
        assertEquals(phase.getMatchController().getMatch().getPlayers().get(0).getSessionToken(), sessionToken);
    }

    @Test
    public void testPhaseTransition() throws InvalidPayloadException {
        int numOfPlayers = 1;
        phase.getMatchController().setMaxNumberOfPlayers(numOfPlayers);
        testAddPlayer();
        assertFalse(phase.getMatchController().isWaitingForPlayers());
        testAddPlayer();
        // if the number of players remains the specified one,
        // we can implicitly understand that the phase state has changed.
        assertEquals(numOfPlayers, phase.getMatchController().getMatch().getPlayers().size());
    }

}