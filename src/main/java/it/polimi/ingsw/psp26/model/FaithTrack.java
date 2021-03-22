package it.polimi.ingsw.psp26.model;

public class FaithTrack {

    private final int length;
    private final VaticanReportSection[] vaticanReportSections;
    private int markerPosition;
    private int faithPoints;
    private int blackCrossPosition;


    public FaithTrack() {
        this.length = 24;
        vaticanReportSections = new VaticanReportSection[3];
        vaticanReportSections[0] = new VaticanReportSection(5, 8);
        vaticanReportSections[1] = new VaticanReportSection(12, 16);
        vaticanReportSections[2] = new VaticanReportSection(19, 24);
        this.markerPosition = 0;
        this.blackCrossPosition = 0;
        this.faithPoints = 0;
    }

    public void addFaithPoints(int points) {
        this.faithPoints += points;
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
    }

    public void moveBlackCrossPosition(int numberOfSteps) {
        this.blackCrossPosition = this.blackCrossPosition + numberOfSteps;
    }
}
