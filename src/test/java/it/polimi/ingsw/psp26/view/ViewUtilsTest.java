package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import org.junit.Test;

import static it.polimi.ingsw.psp26.view.ViewUtils.beautifyMessageType;
import static it.polimi.ingsw.psp26.view.ViewUtils.toTitleStyle;
import static org.junit.Assert.*;

public class ViewUtilsTest {

    @Test
    public void testBeautifyMessageType() {
        assertEquals("General Message", beautifyMessageType(MessageType.GENERAL_MESSAGE));
    }

    @Test
    public void testToTitleStyle() {
        assertEquals("This Is A Title", toTitleStyle("this IS a TITLE"));
    }
}