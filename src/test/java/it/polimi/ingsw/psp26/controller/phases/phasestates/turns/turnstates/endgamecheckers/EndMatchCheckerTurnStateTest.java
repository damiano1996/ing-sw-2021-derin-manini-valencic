package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.actiontokens.DiscardActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EndMatchCheckerTurnStateTest {

    private MitmObserver mitm;
    private Turn turn;

    @Before
    public void setUp() throws Exception {
        mitm = new MitmObserver();
        VirtualView virtualView = new VirtualView();
        Phase phase = new Phase(virtualView.getMatchController());
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname", "sessionToken"));
        phase.getMatchController().setMaxNumberOfPlayers(1);
        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                10
        );

        turn.changeState(new EndMatchCheckerTurnState(turn));
    }

    @After
    public void tearDown() {
        GameSaver.getInstance().deleteDirectoryByMatchId(turn.getMatchController().getMatch().getId());
    }


    @Test
    public void testIsSeventhCardDrawn() throws ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException, DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException, InvalidPayloadException {
        DevelopmentCardsGrid developmentCardsGrid = new DevelopmentCardsGrid(new VirtualView());

        for (int k = 0; k < 2; k++)
            for (int i = 0; i < 3; i++)
                turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(i, developmentCardsGrid.drawCard(Color.GREEN, k == 0 ? Level.FIRST : Level.SECOND));

        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(0, developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD));

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.SKIP_LEADER_ACTION));

        assertEquals(mitm.getMessages().get(0).getMessageType(), MessageType.NOTIFICATION_UPDATE);
        assertTrue(mitm.getMessages().get(0).toString().contains(turn.getTurnPlayer().getNickname() + " activated the endgame by drawing the seventh card."));
    }


    @Test
    public void isFinalTilePosition() throws InvalidPayloadException {
        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(25);

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.SKIP_LEADER_ACTION));

        assertEquals(mitm.getMessages().get(0).getMessageType(), MessageType.NOTIFICATION_UPDATE);
        assertTrue(mitm.getMessages().get(0).toString().contains(turn.getTurnPlayer().getNickname() + " activated the endgame by reaching the final tile in the faith track."));
    }


    @Test
    public void testIsBlackCrossFinalPosition() throws InvalidPayloadException {
        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().moveBlackCrossPosition(25);

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.SKIP_LEADER_ACTION));

        assertEquals(mitm.getMessages().get(0).getMessageType(), MessageType.NOTIFICATION_UPDATE);
        assertTrue(mitm.getMessages().get(0).toString().contains("Lorenzo activated the endgame by reaching the final the final tile in the faith track."));
    }


    @Test
    public void isNoMoreColumnsOfDevelopmentCards() throws ColorDoesNotExistException, LevelDoesNotExistException, InvalidPayloadException {
        DiscardActionToken discardActionToken = new DiscardActionToken(Color.GREEN);

        try {
            for (int i = 0; i < 6; i++)
                discardActionToken.execute(
                        turn.getTurnPlayer().getPersonalBoard().getFaithTrack(),
                        turn.getMatchController().getMatch().getDevelopmentGrid());
        } catch (LorenzoWinException ignored) {
        }

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.SKIP_LEADER_ACTION));

        assertEquals(mitm.getMessages().get(0).getMessageType(), MessageType.NOTIFICATION_UPDATE);
        assertTrue(mitm.getMessages().get(0).toString().contains("Lorenzo activated the endgame by removing a column of development cards."));
    }

}