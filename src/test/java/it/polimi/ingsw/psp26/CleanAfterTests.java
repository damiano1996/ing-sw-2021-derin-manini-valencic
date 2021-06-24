package it.polimi.ingsw.psp26;

import it.polimi.ingsw.psp26.application.files.Files;
import it.polimi.ingsw.psp26.controller.MatchControllerTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.EndMatchPhaseStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.InitializationPhaseStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.BenefitsTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.LeaderCardsAssignmentTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.ResourcesWarehousePlacerTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ActivateOrDiscardLeaderTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ActivateProductionNormalActionTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.BuyCardNormalActionTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.MarketResourceNormalActionTurnStateTest;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer.LorenzoMagnificoTurnStateTest;
import it.polimi.ingsw.psp26.network.server.ServerTest;
import it.polimi.ingsw.psp26.network.server.memory.GameSaverTest;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_FILES;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ServerTest.class,
        ResourcesWarehousePlacerTurnStateTest.class,
        EndMatchCheckerTurnStateTest.class,
        ActivateOrDiscardLeaderTurnStateTest.class,
        ChooseLeaderActionTurnStateTest.class,
        ActivateProductionNormalActionTurnStateTest.class,
        BuyCardNormalActionTurnStateTest.class,
        ChooseNormalActionTurnStateTest.class,
        MarketResourceNormalActionTurnStateTest.class,
        LorenzoMagnificoTurnStateTest.class,
        BenefitsTurnStateTest.class,
        CheckVaticanReportTurnStateTest.class,
        LeaderCardsAssignmentTest.class,
        TurnTest.class,
        EndMatchPhaseStateTest.class,
        InitializationPhaseStateTest.class,
        MatchControllerTest.class,
        GameSaverTest.class
})
public class CleanAfterTests extends TestCase {

    @AfterClass
    public static void cleanGameDirectory() {
        Files.deleteDirectory(GAME_FILES);
    }

}