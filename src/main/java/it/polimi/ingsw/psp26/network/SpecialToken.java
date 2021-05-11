package it.polimi.ingsw.psp26.network;

public enum SpecialToken {

    BROADCAST("FF:FF:FF:FF:FF:FF");

    private final String token;

    SpecialToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
