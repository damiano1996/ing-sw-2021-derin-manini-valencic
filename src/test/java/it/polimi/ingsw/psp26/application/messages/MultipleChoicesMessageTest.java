package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultipleChoicesMessageTest {

    private String sessionToken;
    private String question;
    private int min, max;
    private MultipleChoicesMessage multipleChoicesMessage;

    @Before
    public void setUp() throws InvalidPayloadException {
        sessionToken = "sessionToken";
        question = "question";
        min = 1;
        max = 2;
        multipleChoicesMessage = new MultipleChoicesMessage(sessionToken, MessageType.GENERAL_MESSAGE, question, min, max, true);
    }

    @Test
    public void testGetQuestion() {
        assertEquals(question, multipleChoicesMessage.getQuestion());
    }

    @Test
    public void testGetMinChoices() {
        assertEquals(min, multipleChoicesMessage.getMinChoices());
    }

    @Test
    public void testGetMaxChoices() {
        assertEquals(max, multipleChoicesMessage.getMaxChoices());
    }
    
    @Test
    public void testGetHasQuitOption() {
        assertTrue(multipleChoicesMessage.getHasQuitOption());
    }
}