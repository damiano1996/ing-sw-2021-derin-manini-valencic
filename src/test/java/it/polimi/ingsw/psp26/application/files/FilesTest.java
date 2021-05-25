package it.polimi.ingsw.psp26.application.files;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.CannotReadMessageFileException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Test;

import java.io.FileNotFoundException;

import static it.polimi.ingsw.psp26.application.files.Files.*;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;
import static org.junit.Assert.assertEquals;

public class FilesTest {

    @Test
    public void testWriteAndRead() throws FileNotFoundException {
        String sessionToken = generateSessionToken(32);
        String fileName = "tmpFile.txt";
        writeToFile(fileName, sessionToken);
        String readContent = readFromFile(fileName);
        assertEquals(sessionToken, readContent);
        deleteFile(fileName);
    }

    @Test(expected = FileNotFoundException.class)
    public void testDeleteFile() throws FileNotFoundException {
        String fileName = "tmpFile.txt";
        String content = "content";
        writeToFile(fileName, content);
        assertEquals(content, readFromFile(fileName));
        deleteFile(fileName);
        readFromFile(fileName);
    }

    @Test
    public void testWriteAndReadMessageObject() throws InvalidPayloadException, CannotReadMessageFileException {
        Match match = new Match(new VirtualView(), 0);
        Message message = new Message(MessageType.GENERAL_MESSAGE, match);
        String fileName = "match.ser";
        writeMessageToFile(fileName, message);
        Message readMessage = readMessageFromFile(fileName);
        assert readMessage != null;
        assertEquals(message.getMessageType(), readMessage.getMessageType());
        deleteFile(fileName);
    }
}