package it.polimi.ingsw.psp26.configurations;

/**
 * General configurations that are used among several packages.
 */
public class Configurations {

    public static final String GAME_NAME = "Masters of Renaissance";

    public static final String GAME_FILES = "game_files/";

    public static final int DEFAULT_SERVER_PORT = 2626;

    public static final int CLI_WIDTH = 237;

    public static final int SESSION_TOKEN_LENGTH = 32;

    public static final int MIN_NICKNAME_LENGTH = 8;
    public static final int MIN_PASSWORD_LENGTH = 8;

    // To print or not on the std out of the cli,
    // from the main classes of the package network.client
    public static final boolean PRINT_CLIENT_SIDE = false;
}
