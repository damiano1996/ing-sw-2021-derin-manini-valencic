package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.network.server.VirtualView;

public class VaticanReportSection extends Observable<Message> {

    private final int startSection;
    private final int endSection;
    private boolean popesFavorTileStatus;


    public VaticanReportSection(VirtualView virtualView, int startSection, int endSection) {
        super();
        addObserver(virtualView);

        this.popesFavorTileStatus = false;
        this.startSection = startSection;
        this.endSection = endSection;

        notifyObservers(new Message()); // TODO: to be completed
    }

    public boolean isPopesFavorTileActive() {
        return popesFavorTileStatus;
    }

    public void activatePopesFavorTile() {
        popesFavorTileStatus = true;
        notifyObservers(new Message()); // TODO: to be completed
    }

    public boolean isInVaticanReportSection(int markerPosition) {
        return (startSection <= markerPosition && markerPosition <= endSection);
    }

    public int getStartSection() {
        return startSection;
    }

    public int getEndSection() {
        return endSection;
    }

}
