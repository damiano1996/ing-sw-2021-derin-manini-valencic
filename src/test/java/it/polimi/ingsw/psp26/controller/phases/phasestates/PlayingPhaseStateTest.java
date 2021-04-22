package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;

public class PlayingPhaseStateTest {

    private Phase phase;

    private String nickname1;
    private String sessionToken1;
    private String nickname2;
    private String sessionToken2;

    @Before
    public void setUp() {

        phase = new Phase(new MatchController(new VirtualView(), 0));

        nickname1 = "nick1";
        sessionToken1 = "sess1";
        nickname2 = "nick2";
        sessionToken2 = "sess2";
        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), nickname1, sessionToken1));
        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), nickname2, sessionToken2));

        phase.changeState(new PlayingPhaseState(phase));
    }

    // TODO: how to test: updateCurrentTurn?

    // TODO: how to test: goToEndMatchPhaseState?
}