package it.polimi.ingsw.psp26.network.client.cache;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.ModelUpdateMessage;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains a local copy of the Objects sent by the Model.
 */
public class CachedModel {

    private final String myNickname;
    private final CachedObject<Player> myPlayerCached;
    private final Map<String, CachedObject<Player>> opponentsCached;
    private final CachedObject<MarketTray> marketTrayCached;
    private final CachedObject<DevelopmentCardsGrid> developmentGridCached;

    /**
     * Constructor of the class.
     * It sets the local Player nickname in the myNickname attribute; then initialize all the cached objects.
     *
     * @param myNickname the nickname of the Player the CachedModel belongs to
     */
    public CachedModel(String myNickname) {
        this.myNickname = myNickname;

        myPlayerCached = new CachedObject<>();
        opponentsCached = new HashMap<>();
        marketTrayCached = new CachedObject<>();
        developmentGridCached = new CachedObject<>();
    }


    /**
     * Updates the local Objects copies when receiving a ModelUpdateMessage.
     *
     * @param message the message containing the Object copy
     * @throws EmptyPayloadException the message doesn't have a payload
     */
    public synchronized void updateCachedModel(Message message) throws EmptyPayloadException {
        if (message.getMessageType().equals(MessageType.MODEL_UPDATE)) {
            ModelUpdateMessage modelUpdateMessage = (ModelUpdateMessage) message;

            switch (modelUpdateMessage.getPayloadMessageType()) {

                case PLAYER_MODEL:
                    Player player = (Player) modelUpdateMessage.getModelPayload();

                    if (player.getNickname().equals(myNickname)) {
                        myPlayerCached.updateObject(player);
                    } else {
                        if (opponentsCached.containsKey(player.getNickname())) {
                            opponentsCached.get(player.getNickname()).updateObject(player);
                        } else {
                            CachedObject<Player> cachedOpponent = new CachedObject<>();
                            cachedOpponent.updateObject(player);
                            opponentsCached.put(player.getNickname(), cachedOpponent);
                        }
                    }
                    break;

                case MARKET_TRAY_MODEL:
                    MarketTray marketTray = (MarketTray) modelUpdateMessage.getModelPayload();
                    marketTrayCached.updateObject(marketTray);
                    break;

                case DEVELOPMENT_GRID_MODEL:
                    DevelopmentCardsGrid developmentCardsGrid = (DevelopmentCardsGrid) modelUpdateMessage.getModelPayload();
                    developmentGridCached.updateObject(developmentCardsGrid);
                    break;

                default:
                    break;
            }
        }

        notifyAll();
    }


    /**
     * Getter of the cached Player object.
     *
     * @return cached object containing the Player
     */
    public synchronized CachedObject<Player> getMyPlayerCached() {
        return myPlayerCached;
    }


    /**
     * Getter of the cached opponent player object.
     *
     * @param index index of the player in the hashmap
     * @return cached object containing the opponent player
     * @throws InterruptedException if unable to execute wait method
     */
    public synchronized CachedObject<Player> getOpponentCached(int index) throws InterruptedException {
        while (index >= opponentsCached.size()) wait();
        return opponentsCached.get(new ArrayList<>(opponentsCached.keySet()).get(index));
    }


    /**
     * Getter of the cached MarketTray object.
     *
     * @return cached object containing the MarketTray
     */
    public synchronized CachedObject<MarketTray> getMarketTrayCached() {
        return marketTrayCached;
    }


    /**
     * Getter of the cached object containing the DevelopmentGrid.
     *
     * @return cached object containing the DevelopmentGrid
     */
    public synchronized CachedObject<DevelopmentCardsGrid> getDevelopmentGridCached() {
        return developmentGridCached;
    }


    /**
     * Getter of the number of cached opponents.
     *
     * @return The number of cached opponents
     */
    public synchronized int getNumberOfOpponents() {
        return opponentsCached.keySet().size();
    }
}
