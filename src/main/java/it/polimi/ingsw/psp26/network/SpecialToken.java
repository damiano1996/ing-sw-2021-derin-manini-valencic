package it.polimi.ingsw.psp26.network;

/**
 * Class that represent a special sessionToken.
 */
public enum SpecialToken {

    BROADCAST("FF:FF:FF:FF:FF:FF");

    private final String token;

    /**
     * Constructor of the class.
     * It initializes the token attribute.
     *
     * @param token the token to set as an attribute
     */
    SpecialToken(String token) {
        this.token = token;
    }


    /**
     * Getter of the special token
     *
     * @return The special token
     */
    public String getToken() {
        return token;
    }

}
