package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;
import static org.junit.Assert.assertEquals;

public class MarketResourceNormalActionTurnStateTest {
    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;

    @Before
    public void setUp() throws Exception {

        mitm = new MitmObserver();
        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname", "sessionToken"));
        phase.getMatchController().setMaxNumberOfPlayers(1);
        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                10
        );

        turn.changeState(new MarketResourceNormalActionTurnState(turn));

    }

    @Test
    public void testSendMarketResourceMessage() throws InvalidPayloadException {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION, MARKET_RESOURCE));

        assertEquals(MessageType.CHOICE_ROW_COLUMN, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playSendChoiceRowColumn() throws InvalidPayloadException {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));

        assertEquals(MessageType.CHOICE_ROW_COLUMN, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playSendToWareHousePlacer() throws InvalidPayloadException {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE, Resource.STONE));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE, Resource.COIN));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE, Resource.SHIELD));

        assertEquals(MessageType.PLACE_IN_WAREHOUSE, mitm.getMessages().get(5).getMessageType());
    }

    @Test
    public void playSendChoiceRowColumnLeaderActive() throws InvalidPayloadException {


        List<Resource> expectedResources = leaderCardSetter(1);
        if (expectedResources.size() > 0) {
            Resource changedResource = expectedResources.get(0);
            expectedResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow((2)));

            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));

            expectedResources = expectedResources.stream().filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());
            expectedResources.replaceAll(r -> r.equals(Resource.EMPTY) ? changedResource : r);

            assertEquals(MessageType.CHOICE_ROW_COLUMN, mitm.getMessages().get(0).getMessageType());
            assertEquals(expectedResources, castElements(Resource.class, mitm.getMessages().get(0).getListPayloads()));
        }
    }

    @Test
    public void playSendChoiceRowColumn2LeaderActive() throws InvalidPayloadException {

        List<Resource> marblecolors = leaderCardSetter(2);
        List<Resource> expectedResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow((2)));
        int numberOfEmpty = (int) expectedResources.stream().filter(x -> x.equals(Resource.EMPTY)).count();
        if(marblecolors.size() == 2 && numberOfEmpty != 0) {

            expectedResources = expectedResources.stream().filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());
            expectedResources.replaceAll(r -> r.equals(Resource.EMPTY) ? marblecolors.get(0) : r);

            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));
            for(int i = 0; i < numberOfEmpty; i++){
                turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE, marblecolors.get(0)));
            }

            assertEquals(CHOICE_RESOURCE, mitm.getMessages().get(0).getMessageType());
            assertEquals(CHOICE_ROW_COLUMN, mitm.getMessages().get(1 + (numberOfEmpty - 1)*2).getMessageType());
           assertEquals(expectedResources, castElements(Resource.class, mitm.getMessages().get(1 + (numberOfEmpty - 1)*2).getListPayloads()));


        }
    }


    private List<Resource> leaderCardSetter(int leaderNumber) {
        List<LeaderCard> leaderCards = new ArrayList<>();
        List<LeaderCard> leaderCardsAdded = new ArrayList<>();
        List<Resource> marbleTypeSubstitute = new ArrayList<>();

        leaderCards.addAll(phase.getMatchController().getMatch().drawLeaders(8));
        leaderCardsAdded.addAll(leaderCards.stream().filter(x -> x.getAbilityToString().contains("WhiteMarbleAbility")).collect(Collectors.toList()));

        if (leaderCardsAdded.size() >= leaderNumber) {

            turn.getTurnPlayer().setLeaderCards(leaderCardsAdded.subList(0, leaderNumber));

            for (int i = 0; i < leaderNumber; i++) {

                turn.getTurnPlayer().getLeaderCards().get(i).activate(turn.getTurnPlayer());
                marbleTypeSubstitute.add(turn.getTurnPlayer().getLeaderCards().get(i).getAbilityResource());

            }
        }

        return marbleTypeSubstitute;

    }
}