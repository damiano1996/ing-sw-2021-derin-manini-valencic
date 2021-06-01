package it.polimi.ingsw.psp26.configurations;

public class Configurations {

    public static final String GAME_NAME = "Master of Renaissance";

    public static final String RESOURCES_PATH = "src/main/resources/";
    public static final String GAME_FILES = RESOURCES_PATH + "game_files/";

    public static final String DEFAULT_SERVER_IP = "127.0.0.1";
    public static final int DEFAULT_SERVER_PORT = 2626;
    public static final int DEFAULT_CLIENT_PORT = 6262;

    public static final int SESSION_TOKEN_LENGTH = 32;

    public static final int MIN_NICKNAME_LENGTH = 8;
    public static final int MIN_PASSWORD_LENGTH = 8;

    // To print or not on the std out of the cli,
    // from the main classes of the package network.client
    public static final boolean PRINT_CLIENT_SIDE = true;
}
