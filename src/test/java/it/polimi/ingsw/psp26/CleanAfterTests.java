package it.polimi.ingsw.psp26;

import it.polimi.ingsw.psp26.application.files.Files;
import it.polimi.ingsw.psp26.network.server.ServerTest;
import it.polimi.ingsw.psp26.network.server.memory.GameSaverTest;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_FILES;

@RunWith(Suite.class)
@Suite.SuiteClasses({ServerTest.class, GameSaverTest.class})
public class CleanAfterTests extends TestCase {

    @AfterClass
    public static void cleanGameDirectory() {
        Files.deleteDirectory(GAME_FILES);
    }
}