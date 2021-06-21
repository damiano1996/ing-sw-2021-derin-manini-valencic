package it.polimi.ingsw.psp26.network.server.memory;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.psp26.application.messages.serialization.GsonConverter;
import it.polimi.ingsw.psp26.exceptions.NicknameAlreadyExistsException;
import it.polimi.ingsw.psp26.exceptions.NicknameTooShortException;
import it.polimi.ingsw.psp26.exceptions.PasswordTooShortException;
import it.polimi.ingsw.psp26.exceptions.SessionTokenDoesNotExistsException;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.psp26.application.files.Files.*;
import static it.polimi.ingsw.psp26.configurations.Configurations.*;

/**
 * Class used to track correspondence between the nickname-password and nickname-sessionToken of all Players.
 */
public class Users {

    private static final String USERS_FILE_PASSWORDS = GAME_FILES + "nickname-password.json";
    private static final String USERS_FILE_SESSION_TOKENS = GAME_FILES + "nickname-sessionToken.json";

    private static Users instance;

    private Map<String, String> nicknamePasswords;
    private Map<String, String> nicknameSessionTokens;

    /**
     * Constructor of the class.
     * It creates a new GAME_FILES directory (if not already present).
     * It initializes the nicknamePassword and nicknameSessionTokens Maps and tries to load the saved version of these
     * Maps (if they are present in the GAME_FILES directory).
     */
    public Users() {
        createNewDirectory(GAME_FILES);

        nicknamePasswords = new HashMap<>();
        nicknameSessionTokens = new HashMap<>();

        try {
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();

            nicknamePasswords = GsonConverter.getInstance().getGson().fromJson(readFromFile(USERS_FILE_PASSWORDS), type);
            nicknameSessionTokens = GsonConverter.getInstance().getGson().fromJson(readFromFile(USERS_FILE_SESSION_TOKENS), type);
        } catch (FileNotFoundException ignored) {
        }
    }

    /**
     * Getter of the instance of the class.
     *
     * @return the instance of the class
     */
    public synchronized static Users getInstance() {
        if (instance == null) instance = new Users();
        return instance;
    }


    /**
     * Checks if the nickname entered by the Player satisfies the requirements.
     *
     * @param nickname the nickname entered by the Player
     * @throws NicknameTooShortException      thrown if nickname length < MIN_NICKNAME_LENGTH
     * @throws NicknameAlreadyExistsException thrown if the nickname has already been entered by another Player
     */
    public synchronized static void checkNicknameRequirements(String nickname) throws NicknameTooShortException, NicknameAlreadyExistsException {
        if (nickname.length() < MIN_NICKNAME_LENGTH) throw new NicknameTooShortException();
        if (Users.getInstance().getNicknamePasswords().containsKey(nickname))
            throw new NicknameAlreadyExistsException();
    }


    /**
     * Checks if the password entered by the Player satisfies the requirements.
     *
     * @param password the password entered by the Player
     * @throws PasswordTooShortException thrown if password length < MIN_PASSWORD_LENGTH
     */
    public synchronized static void checkPasswordRequirements(String password) throws PasswordTooShortException {
        if (password.length() < MIN_PASSWORD_LENGTH) throw new PasswordTooShortException();
    }


    /**
     * Adds a new user nickname and password in the nicknamePasswords Map and writes it to File.
     *
     * @param nickname     the nickname of the Player
     * @param password     the password of the Player
     * @param sessionToken the sessionToken of the Player
     */
    public synchronized void addUser(String nickname, String password, String sessionToken) {
        nicknamePasswords.put(nickname, password);
        writeToFile(USERS_FILE_PASSWORDS, GsonConverter.getInstance().getGson().toJson(nicknamePasswords));

        nicknameSessionTokens.put(nickname, sessionToken);
        writeToFile(USERS_FILE_SESSION_TOKENS, GsonConverter.getInstance().getGson().toJson(nicknameSessionTokens));
    }


    /**
     * Getter of the nickname of a Player from the nicknamePasswords Map. Retrieves it by using the Player sessionToken.
     *
     * @param sessionToken the sessionToken of the wanted Player
     * @return the nickname of the Player
     * @throws SessionTokenDoesNotExistsException thrown if nicknameSessionTokens doesn't contain the given sessionToken
     */
    public synchronized String getNickname(String sessionToken) throws SessionTokenDoesNotExistsException {
        for (String nickname : nicknameSessionTokens.keySet()) {
            if (nicknameSessionTokens.get(nickname).equals(sessionToken)) return nickname;
        }
        throw new SessionTokenDoesNotExistsException();
    }


    /**
     * Getter of the nicknamePasswords Map.
     *
     * @return an unmodifiable nicknamePasswords Map
     */
    public synchronized Map<String, String> getNicknamePasswords() {
        return Collections.unmodifiableMap(nicknamePasswords);
    }


    /**
     * Getter of the nicknameSessionTokens Map.
     *
     * @return an unmodifiable nicknameSessionTokens Map
     */
    public synchronized Map<String, String> getNicknameSessionTokens() {
        return Collections.unmodifiableMap(nicknameSessionTokens);
    }

}
