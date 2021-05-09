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
            }
        }
    }

    public synchronized Player getUpdatedMyPlayerCached() throws InterruptedException {
        return myPlayerCached.getUpdatedObject();
    }

    public synchronized Player getObsoleteMyPlayerCached() throws InterruptedException {
        return myPlayerCached.getObsoleteObject();
    }

    
    // TODO fare i metodi doppi updated eobsolete anche di questi
    
    public synchronized Player getOpponentCached(int index) throws InterruptedException {
        return opponentsCached.get(new ArrayList<>(opponentsCached.keySet()).get(index)).getUpdatedObject();
    }

    public synchronized MarketTray getMarketTrayCached() throws InterruptedException {
        return marketTrayCached.getUpdatedObject();
    }

    public synchronized DevelopmentGrid getDevelopmentGridCached() throws InterruptedException {
        return developmentGridCached.getUpdatedObject();
    }

    private static class CachedObject<T> {
        private T object;
        private boolean obsolete;

        public CachedObject() {
            obsolete = true;
        }
        
        public synchronized void updateObject(T object) {
            this.object = object;
            obsolete = false;
            notifyAll();
        }

        public synchronized T getUpdatedObject() throws InterruptedException {
            while (obsolete) wait();
            obsolete = true;
            return object;
        }

        /**
         * By assumption it will never stop the calling thread
         */
        public synchronized T getObsoleteObject() throws InterruptedException {
            if (object == null) return getUpdatedObject();
            return object;
        }
        
    }
}
