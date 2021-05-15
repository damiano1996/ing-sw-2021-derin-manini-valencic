package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;

import java.util.*;

public class ViewUtils {

    public static String beautifyMessageType(MessageType messageType) {
        String message = messageType.name();
        message = message.replace('_', ' ');
        return toTitleStyle(message);
    }

    public static String toTitleStyle(String string) {
        StringBuilder newString = new StringBuilder();

        boolean space = true;
        for (Character character : string.toLowerCase(Locale.ROOT).toCharArray()) {
            newString.append((space) ? Character.toUpperCase(character) : character);
            space = character == ' ';
        }
        return newString.toString();
    }

    /**
     * Static method used to create the final leaderboard of Players-Points
     *
     * @param players The Players of the ended Match
     * @return The leaderboard to display as a Map
     */
    public static Map<String, Integer> createLeaderboard(List<Player> players) {
        Map<String, Integer> leaderboard = new HashMap<>();
        for (Player player : players) leaderboard.put(player.getNickname(), player.getPoints());
        return leaderboard;
    }

    /**
     * Static method used to create the ended Match Players List
     *
     * @param cachedModel The CachedModel from which get the Players information
     * @return The List of the ended Match Players
     */
    public static List<Player> createPlayersList(CachedModel cachedModel) {
        List<Player> players = new ArrayList<>();
        try {
            for (int i = 0; i < cachedModel.getNumberOfOpponents(); i++) {
                players.add(cachedModel.getOpponentCached(i).getObsoleteObject());
            }
            players.add(cachedModel.getMyPlayerCached().getObsoleteObject());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return players;
    }

}
