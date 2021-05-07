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

    public synchronized Player getMyPlayerCached() throws InterruptedException {
        return myPlayerCached.getObject();
    }

    public synchronized Player getOpponentCached(int index) throws InterruptedException {
        return opponentsCached.get(new ArrayList<>(opponentsCached.keySet()).get(index)).getObject();
    }

    public synchronized MarketTray getMarketTrayCached() throws InterruptedException {
        return marketTrayCached.getObject();
    }

    public synchronized DevelopmentGrid getDevelopmentGridCached() throws InterruptedException {
        return developmentGridCached.getObject();
    }

    private static class CachedObject<T> {
        private T object;

        public synchronized void updateObject(T object) {
            this.object = object;
            notifyAll();
        }

        public synchronized T getObject() throws InterruptedException {
            while (object == null) wait();
            T tmpObject = object;
            object = null;
            return tmpObject;
        }
    }
}
