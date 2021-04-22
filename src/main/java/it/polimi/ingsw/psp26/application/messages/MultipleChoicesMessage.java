package it.polimi.ingsw.psp26.application.messages;

import java.io.Serializable;

public class MultipleChoicesMessage extends SessionMessage implements Serializable {

    private final String question;
    private final int minChoices;
    private final int maxChoices;

    public MultipleChoicesMessage(
            String sessionToken,
            MessageType messageType,
            String question,
            int minChoices,
            int maxChoices,
            Object... objects) {

        super(sessionToken, messageType, objects);
        this.question = question;
        this.minChoices = minChoices;
        this.maxChoices = maxChoices;
    }

    public String getQuestion() {
        return question;
    }

    public int getMinChoices() {
        return minChoices;
    }

    public int getMaxChoices() {
        return maxChoices;
    }

    @Override
    public String toString() {
        return "MultipleChoicesMessage{" +
                "minChoices=" + minChoices +
                ", maxChoices=" + maxChoices +
                "} " + super.toString();
    }
}
