package it.polimi.ingsw.psp26.network.server.memory;

import static it.polimi.ingsw.psp26.configurations.Configurations.MIN_NICKNAME_LENGTH;

public enum CommonNicknamePasswordChecksEnums {

    NICKNAME_AND_PASSWORD_ARE_OK("Success!"),

    NICKNAME_TOO_SHORT("Nickname is too short (min length: " + MIN_NICKNAME_LENGTH + " characters)"),
    NICKNAME_ALREADY_EXISTS("Nickname already exists..."),
    PASSWORD_NOT_CORRECT("Password is not correct!"),
    PASSWORD_TOO_SHORT("Password is too short (min length: " + MIN_NICKNAME_LENGTH + " characters)");

    private final String description;

    CommonNicknamePasswordChecksEnums(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
