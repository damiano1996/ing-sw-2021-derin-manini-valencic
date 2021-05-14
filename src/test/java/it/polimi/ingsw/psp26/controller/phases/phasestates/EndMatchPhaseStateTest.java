package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.LeaderBoard;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EndMatchPhaseStateTest {

    private MitmObserver mitm;
    private Phase phase;

    @Before
    public void setUp() throws Exception {
        mitm = new MitmObserver();
        VirtualView virtualView = new VirtualView();
        phase = new Phase(virtualView.getMatchController());
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname", "sessionToken"));
        phase.getMatchController().setMaxNumberOfPlayers(1);

        phase.changeState(new EndMatchPhaseState(phase));

    }

    @Test
    public void executeTest() throws InvalidPayloadException, CanNotAddResourceToStrongboxException, EmptyPayloadException, NoMoreDevelopmentCardsException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {

        Player player = phase.getMatchController().getMatch().getPlayers().get(0);
        Integer expectedVictoryPoints = 0;

        for (int i = 0; i < 13; i++) {
            player.getPersonalBoard().addResourceToStrongbox(Resource.STONE);
        }
        expectedVictoryPoints += 2;


        player.getPersonalBoard().getFaithTrack().addFaithPoints(19);
        expectedVictoryPoints += 12;

        List<LeaderCard> playerLeaderCard = phase.getMatchController().getMatch().drawLeaders(1);
        player.setLeaderCards(playerLeaderCard);
        player.getLeaderCards().get(0).activate(player);
        expectedVictoryPoints += playerLeaderCard.get(0).getVictoryPoints();

        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].activatePopesFavorTile();
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[1].activatePopesFavorTile();
        expectedVictoryPoints += 5;

        DevelopmentCard playerDevelopmentCard = phase.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).drawCard();
        player.getPersonalBoard().addDevelopmentCard(1, playerDevelopmentCard);
        expectedVictoryPoints += playerDevelopmentCard.getVictoryPoints();

        phase.execute(new SessionMessage(player.getSessionToken(),
                MessageType.SEVENTH_CARD_DRAWN));

        assertEquals(MessageType.ENDGAME_RESULT, mitm.getMessages().get(0).getMessageType());
        LeaderBoard leaderboard = (LeaderBoard) mitm.getMessages().get(0).getPayload();
        assertTrue(leaderboard.getLeaderboard().containsKey(player.getNickname()));
        assertEquals(expectedVictoryPoints, leaderboard.getLeaderboard().get(player.getNickname()));


    }
}