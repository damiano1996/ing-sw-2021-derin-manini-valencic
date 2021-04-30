package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.io.Serializable;

/**
 * Class that extends the SessionMessage adding the possibility to set parameters,
 * that are useful when the user has to choose among multiple payloads.
 */
public class MultipleChoicesMessage extends SessionMessage implements Serializable {

    private final String question;
    private final int minChoices;
    private final int maxChoices;
    private final boolean hasQuitOption;

    /**
     * Constructor of the class.
     *
     * @param sessionToken  session token
     * @param messageType   message type
     * @param question      question to ask to the player
     * @param minChoices    min number of items to choose
     * @param maxChoices    max number of items to choose
     * @param payloads      payloads to send (choices for the players)
     * @param hasQuitOption the choices can be undone
     * @throws InvalidPayloadException if payloads are not serializable
     */
    public MultipleChoicesMessage(
            String sessionToken,
            MessageType messageType,
            String question,
            int minChoices,
            int maxChoices,
            boolean hasQuitOption,
            Object... payloads) throws InvalidPayloadException {

        super(sessionToken, messageType, payloads);
        this.question = question;
        this.minChoices = minChoices;
        this.maxChoices = maxChoices;
        this.hasQuitOption = hasQuitOption;
    }

    /**
     * Getter of the question.
     *
     * @return question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Getter of the min number of items to select.
     *
     * @return min number
     */
    public int getMinChoices() {
        return minChoices;
    }

    /**
     * Getter of the max number of items to select.
     *
     * @return max number
     */
    public int getMaxChoices() {
        return maxChoices;
    }

    /**
     * Getter of the boolean that tells if an additional String will be printed for undoing the choice
     * 
     * @return hasQuitOption
     */
    public boolean getHasQuitOption() {
        return hasQuitOption;
    }

    /**
     * To string method, to print the message.
     *
     * @return string version of the message
     */
    @Override
    public String toString() {
        return "MultipleChoicesMessage{" +
                "minChoices=" + minChoices +
                ", maxChoices=" + maxChoices +
                "} " + super.toString();
    }
}
