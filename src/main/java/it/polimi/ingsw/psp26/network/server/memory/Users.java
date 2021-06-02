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

    private static String USERS_FILE_PASSWORDS = GAME_FILES + "nickname-password.json";
    private static String USERS_FILE_SESSION_TOKENS = GAME_FILES + "nickname-sessionToken.json";

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

    public synchronized static Users getInstance() {
        if (instance == null) instance = new Users();
        return instance;
    }

    public synchronized static void checkNicknameRequirements(String nickname) throws NicknameTooShortException, NicknameAlreadyExistsException {
        if (nickname.length() < MIN_NICKNAME_LENGTH) throw new NicknameTooShortException();
        if (Users.getInstance().getNicknamePasswords().containsKey(nickname))
            throw new NicknameAlreadyExistsException();
    }

    public synchronized static void checkPasswordRequirements(String password) throws PasswordTooShortException {
        if (password.length() < MIN_PASSWORD_LENGTH) throw new PasswordTooShortException();
    }

    public synchronized void addUser(String nickname, String password, String sessionToken) {
        nicknamePasswords.put(nickname, password);
        writeToFile(USERS_FILE_PASSWORDS, GsonConverter.getInstance().getGson().toJson(nicknamePasswords));

        nicknameSessionTokens.put(nickname, sessionToken);
        writeToFile(USERS_FILE_SESSION_TOKENS, GsonConverter.getInstance().getGson().toJson(nicknameSessionTokens));
    }

    public synchronized String getNickname(String sessionToken) throws SessionTokenDoesNotExistsException {
        for (String nickname : nicknameSessionTokens.keySet()) {
            if (nicknameSessionTokens.get(nickname).equals(sessionToken)) return nickname;
        }
        throw new SessionTokenDoesNotExistsException();
    }

    public synchronized Map<String, String> getNicknamePasswords() {
        return Collections.unmodifiableMap(nicknamePasswords);
    }

    public synchronized Map<String, String> getNicknameSessionTokens() {
        return Collections.unmodifiableMap(nicknameSessionTokens);
    }
}
