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

public class Users {

    private static final String USERS_FILE_PASSWORDS = GAME_FILES + "nickname-password.json";
    private static final String USERS_FILE_SESSION_TOKENS = GAME_FILES + "nickname-sessionToken.json";

    private static Users instance;

    private Map<String, String> nicknamePasswords;
    private Map<String, String> nicknameSessionTokens;

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
     * Getter of the instance of the class
     *
     * @return The instance of the class
     */
    public synchronized static Users getInstance() {
        if (instance == null) instance = new Users();
        return instance;
    }


    /**
     * Checks if the nickname entered by the Player satisfies the requirements
     *
     * @param nickname The nickname entered by the Player
     * @throws NicknameTooShortException      Thrown if nickname length < MIN_NICKNAME_LENGTH
     * @throws NicknameAlreadyExistsException Thrown if the nickname has already been entered by another Player
     */
    public synchronized static void checkNicknameRequirements(String nickname) throws NicknameTooShortException, NicknameAlreadyExistsException {
        if (nickname.length() < MIN_NICKNAME_LENGTH) throw new NicknameTooShortException();
        if (Users.getInstance().getNicknamePasswords().containsKey(nickname))
            throw new NicknameAlreadyExistsException();
    }


    /**
     * Checks if the password entered by the Player satisfies the requirements
     *
     * @param password The password entered by the Player
     * @throws PasswordTooShortException Thrown if password length < MIN_PASSWORD_LENGTH
     */
    public synchronized static void checkPasswordRequirements(String password) throws PasswordTooShortException {
        if (password.length() < MIN_PASSWORD_LENGTH) throw new PasswordTooShortException();
    }


    /**
     * Adds a new user nickname and password in the nicknamePasswords Map and writes it to File
     *
     * @param nickname     The nickname of the Player
     * @param password     The password of the Player
     * @param sessionToken The sessionToken of the Player
     */
    public synchronized void addUser(String nickname, String password, String sessionToken) {
        nicknamePasswords.put(nickname, password);
        writeToFile(USERS_FILE_PASSWORDS, GsonConverter.getInstance().getGson().toJson(nicknamePasswords));

        nicknameSessionTokens.put(nickname, sessionToken);
        writeToFile(USERS_FILE_SESSION_TOKENS, GsonConverter.getInstance().getGson().toJson(nicknameSessionTokens));
    }


    /**
     * Getter of the nickname of a Player from the nicknamePasswords Map. Retrieves it by using the Player sessionToken
     *
     * @param sessionToken The sessionToken of the wanted Player
     * @return The nickname of the Player
     * @throws SessionTokenDoesNotExistsException Thrown if nicknameSessionTokens doesn't contain the given sessionToken
     */
    public synchronized String getNickname(String sessionToken) throws SessionTokenDoesNotExistsException {
        for (String nickname : nicknameSessionTokens.keySet()) {
            if (nicknameSessionTokens.get(nickname).equals(sessionToken)) return nickname;
        }
        throw new SessionTokenDoesNotExistsException();
    }


    /**
     * Getter of the nicknamePasswords Map
     *
     * @return An unmodifiable nicknamePasswords Map
     */
    public synchronized Map<String, String> getNicknamePasswords() {
        return Collections.unmodifiableMap(nicknamePasswords);
    }


    /**
     * Getter of the nicknameSessionTokens Map
     *
     * @return An unmodifiable nicknameSessionTokens Map
     */
    public synchronized Map<String, String> getNicknameSessionTokens() {
        return Collections.unmodifiableMap(nicknameSessionTokens);
    }
}
