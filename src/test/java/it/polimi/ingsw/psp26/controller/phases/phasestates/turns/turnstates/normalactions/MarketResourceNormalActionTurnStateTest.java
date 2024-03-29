package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
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
import static org.junit.Assert.assertEquals;

public class MarketResourceNormalActionTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;
    private VirtualView virtualView;

    @Before
    public void setUp() throws Exception {
        virtualView = new VirtualView();
        mitm = new MitmObserver();
        phase = new Phase(virtualView.getMatchController());
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname", "sessionToken"));
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
        int marblesToPlace = (int) Arrays.stream(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow(2))
                .filter(x -> x != Resource.EMPTY && x != Resource.FAITH_MARKER).count();

        if (marblesToPlace == 0) {
            turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(2);
            turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(1);
            turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(2);
        }

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));

        assertEquals(PLACE_IN_WAREHOUSE, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playSendToWareHousePlacer() throws InvalidPayloadException {

        int marblesToPlace = (int) Arrays.stream(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow(2))
                .filter(x -> x != Resource.EMPTY && x != Resource.FAITH_MARKER).count();

        if (marblesToPlace == 0) {
            turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(2);
            turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(1);
            turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(2);
        }

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE, Resource.STONE));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE, Resource.COIN));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE, Resource.SHIELD));


        assertEquals(MessageType.PLACE_IN_WAREHOUSE, mitm.getMessages().get(3).getMessageType());
    }

    @Test
    public void playSendChoiceRowColumnLeaderActive() throws InvalidPayloadException {
        List<Resource> expectedResources = leaderCardSetter(1);
        if (expectedResources.size() > 0) {

            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));

            assertEquals(PLACE_IN_WAREHOUSE, mitm.getMessages().get(0).getMessageType());
        }
    }

    @Test
    public void playSendChoiceRowColumn2LeaderActive() throws InvalidPayloadException {
        List<Resource> marbleColors = leaderCardSetter(2);
        List<Resource> expectedResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow(2));

        int numberOfEmpty = (int) expectedResources.stream().filter(x -> x.equals(Resource.EMPTY)).count();

        if (marbleColors.size() == 2 && numberOfEmpty != 0) {

            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_ROW_COLUMN, 2));
            for (int i = 0; i < numberOfEmpty; i++) {
                turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY, marbleColors.get(0)));
            }

            assertEquals(CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY, mitm.getMessages().get(0).getMessageType());
            assertEquals(PLACE_IN_WAREHOUSE, mitm.getMessages().get(2 + (2 * (numberOfEmpty - 1))).getMessageType());
        }
    }


    private List<Resource> leaderCardSetter(int leaderNumber) {
        List<Resource> marbleTypeSubstitute = new ArrayList<>();

        List<LeaderCard> leaderCards = new ArrayList<>(phase.getMatchController().getMatch().drawLeaders(16));
        List<LeaderCard> leaderCardsAdded = leaderCards.stream().filter(x -> x.getAbilityToString().contains("WhiteMarbleAbility")).collect(Collectors.toList());

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