package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.network.server.VirtualView;

public class FaithTrack extends Observable<Message> {

    private final int length;
    private final VaticanReportSection[] vaticanReportSections;
    private int markerPosition;
    private int faithPoints;
    private int blackCrossPosition;


    public FaithTrack(VirtualView virtualView) {
        super();
        addObserver(virtualView);

        this.length = 24;
        vaticanReportSections = new VaticanReportSection[3];
        vaticanReportSections[0] = new VaticanReportSection(virtualView, 5, 8);
        vaticanReportSections[1] = new VaticanReportSection(virtualView, 12, 16);
        vaticanReportSections[2] = new VaticanReportSection(virtualView, 19, 24);
        this.markerPosition = 0;
        this.blackCrossPosition = 0;
        this.faithPoints = 0;

        notifyObservers(new Message()); // TODO: to be completed
    }

    public void addFaithPoints(int points) {
        this.faithPoints += points;
        notifyObservers(new Message()); // TODO: to be completed
    }

    //maybe a useless method
    //public void discardPopesFavorTile(int position) { }

    public VaticanReportSection[] getVaticanReportSections() {
        return vaticanReportSections;
    }

    public int getMarkerPosition() {
        return markerPosition;
    }

    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    public void moveMarkerPosition(int numberOfSteps) {
        this.markerPosition = this.markerPosition + numberOfSteps;
        this.faithPoints = this.faithPoints + numberOfSteps;

        notifyObservers(new Message()); // TODO: to be completed
    }

    public void moveBlackCrossPosition(int numberOfSteps) {
        this.blackCrossPosition = this.blackCrossPosition + numberOfSteps;

        notifyObservers(new Message()); // TODO: to be completed
    }

    public boolean equals(FaithTrack faithTrack) {
        if (this.length != faithTrack.length) return false;
        if (this.markerPosition != faithTrack.markerPosition) return false;
        if (this.blackCrossPosition != faithTrack.blackCrossPosition) return false;
        if (this.faithPoints != faithTrack.faithPoints) return false;
        for (int i = 0; i < 3; i++) {
            if (this.vaticanReportSections[i].isPopesFavorTileActive() != faithTrack.vaticanReportSections[i].isPopesFavorTileActive())
                return false;
            if (this.vaticanReportSections[i].getStartSection() != faithTrack.vaticanReportSections[i].getStartSection())
                return false;
            if (this.vaticanReportSections[i].getEndSection() != faithTrack.vaticanReportSections[i].getEndSection())
                return false;
        }
        return true;
    }
}
