package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.ModelUpdateMessage;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains a local copy of the Objects sent by the Model
 */
public class CachedModel {

    private final String myNickname;
    private final CachedObject<Player> myPlayerCached;
    private final Map<String, CachedObject<Player>> opponentsCached;
    private final CachedObject<MarketTray> marketTrayCached;
    private final CachedObject<DevelopmentGrid> developmentGridCached;

    public CachedModel(String myNickname) {
        this.myNickname = myNickname;

        myPlayerCached = new CachedObject<>();
        opponentsCached = new HashMap<>();
        marketTrayCached = new CachedObject<>();
        developmentGridCached = new CachedObject<>();
    }


    /**
     * Updates the local Objects copies when receiving a ModelUpdateMessage
     * 
     * @param message The message containing the Object copy
     * @throws EmptyPayloadException The message doesn't have a payload
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
                    
                    
                case MARKET_MODEL:
                    MarketTray marketTray = (MarketTray) modelUpdateMessage.getModelPayload();
                    marketTrayCached.updateObject(marketTray);
                    break;
                    
                    
                case GRID_MODEL:
                    DevelopmentGrid developmentGrid = (DevelopmentGrid) modelUpdateMessage.getModelPayload();
                    developmentGridCached.updateObject(developmentGrid);
                    break;
                    
                    
                default:
                    break;
                    
            }
        }
    }


    /**
     * Getter of the myPlayerCached attribute. Returns its updated version
     * 
     * @return An updated version of myPlayerCached
     * @throws InterruptedException If myPlayerCached is null, calls wait() on the current thread
     */
    public synchronized Player getUpdatedMyPlayerCached() throws InterruptedException {
        return myPlayerCached.getUpdatedObject();
    }


    /**
     * Getter of the myPlayerCached attribute. Returns its obsolete version
     *
     * @return An obsolete version of myPlayerCached
     * @throws InterruptedException If myPlayerCached is null, calls wait() on the current thread
     */
    public synchronized Player getObsoleteMyPlayerCached() throws InterruptedException {
        return myPlayerCached.getObsoleteObject();
    }


    /**
     * Getter of the opponentsCached attribute. Returns an updated version of a Player contained in this List
     *
     * @param index The index of the wanted Player
     * @return An updated version of a Player contained in this List
     * @throws InterruptedException If the Player is null, calls wait() on the current thread
     */
    public synchronized Player getUpdatedOpponentCached(int index) throws InterruptedException {
        return opponentsCached.get(new ArrayList<>(opponentsCached.keySet()).get(index)).getUpdatedObject();
    }


    /**
     * Getter of the opponentsCached attribute. Returns an obsolete version of a Player contained in this List
     *
     * @param index The index of the wanted Player
     * @return An obsolete version of a Player contained in this List
     * @throws InterruptedException If the Player is null, calls wait() on the current thread
     */
    public synchronized Player getObsoleteOpponentCached(int index) throws InterruptedException {
        return opponentsCached.get(new ArrayList<>(opponentsCached.keySet()).get(index)).getObsoleteObject();
    }


    /**
     * Getter of the marketTrayCached attribute. Returns its updated version
     *
     * @return An updated version of marketTrayCached
     * @throws InterruptedException If marketTrayCached is null, calls wait() on the current thread
     */
    public synchronized MarketTray getUpdatedMarketTrayCached() throws InterruptedException {
        return marketTrayCached.getUpdatedObject();
    }


    /**
     * Getter of the marketTrayCached attribute. Returns its obsolete version
     *
     * @return An obsolete version of marketTrayCached
     * @throws InterruptedException If marketTrayCached is null, calls wait() on the current thread
     */
    public synchronized MarketTray getObsoleteMarketTrayCached() throws InterruptedException {
        return marketTrayCached.getObsoleteObject();
    }


    /**
     * Getter of the developmentGridCached attribute. Returns its updated version
     *
     * @return An updated version of developmentGridCached
     * @throws InterruptedException If developmentGridCached is null, calls wait() on the current thread
     */
    public synchronized DevelopmentGrid getUpdatedDevelopmentGridCached() throws InterruptedException {
        return developmentGridCached.getUpdatedObject();
    }


    /**
     * Getter of the developmentGridCached attribute. Returns its obsolete version
     *
     * @return An obsolete version of developmentGridCached
     * @throws InterruptedException If developmentGridCached is null, calls wait() on the current thread
     */
    public synchronized DevelopmentGrid getObsoleteDevelopmentGridCached() throws InterruptedException {
        return developmentGridCached.getObsoleteObject();
    }


    /**
     * Inner class that defines the new type CachedObject, used to store the Models copies
     */
    private static class CachedObject<T> {
        private T object;
        private boolean obsolete;

        public CachedObject() {
            obsolete = true;
        }

        
        /**
         * When receiving a new Object from the Model, change the current object value with the new one
         * 
         * @param object The new Object version
         */
        public synchronized void updateObject(T object) {
            this.object = object;
            obsolete = false;
            notifyAll();
        }


        /**
         * Getter of an updated version of object
         * 
         * @return An updated version of object
         * @throws InterruptedException If object is null, calls wait() on the current thread
         */
        public synchronized T getUpdatedObject() throws InterruptedException {
            while (obsolete) wait();
            obsolete = true;
            return object;
        }

        
        /**
         * Getter of an updated version of object
         * By assumption it will never stop the calling thread
         *
         * @return An obsolete version of object
         * @throws InterruptedException If object is null, calls wait() on the current thread
         */
        public synchronized T getObsoleteObject() throws InterruptedException {
            if (object == null) return getUpdatedObject();
            return object;
        }

    }
    
}
