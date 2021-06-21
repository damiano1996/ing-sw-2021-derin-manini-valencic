package it.polimi.ingsw.psp26.network.server.memory;

import static it.polimi.ingsw.psp26.configurations.Configurations.MIN_NICKNAME_LENGTH;

/**
 * Enumeration containing messages related to the nickname-password insertion.
 */
public enum CommonNicknamePasswordChecksEnums {

    NICKNAME_AND_PASSWORD_ARE_OK("Success!"),

    NICKNAME_TOO_SHORT("Nickname is too short (min length: " + MIN_NICKNAME_LENGTH + " characters)"),
    NICKNAME_ALREADY_EXISTS("Nickname already exists..."),
    PASSWORD_NOT_CORRECT("Password is not correct!"),
    PASSWORD_TOO_SHORT("Password is too short (min length: " + MIN_NICKNAME_LENGTH + " characters)");

    private final String description;

    /**
     * Constructor of the class.
     * Sets the description of the Enum.
     *
     * @param description the description of the Enum
     */
    CommonNicknamePasswordChecksEnums(String description) {
        this.description = description;
    }


    /**
     * Getter of the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
}
