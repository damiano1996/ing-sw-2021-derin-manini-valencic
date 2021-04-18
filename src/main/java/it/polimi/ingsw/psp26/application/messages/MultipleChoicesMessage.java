package it.polimi.ingsw.psp26.application.messages;

import java.io.Serializable;

public class MultipleChoicesMessage extends SessionMessage implements Serializable {

    private final int minChoices;
    private final int maxChoices;

    public MultipleChoicesMessage(
            String sessionToken,
            MessageType messageType,
            int minChoices,
            int maxChoices,
            Object... objects) {

        super(sessionToken, messageType, objects);
        this.minChoices = minChoices;
        this.maxChoices = maxChoices;
    }

    public int getMinChoices() {
        return minChoices;
    }

    public int getMaxChoices() {
        return maxChoices;
    }
}
