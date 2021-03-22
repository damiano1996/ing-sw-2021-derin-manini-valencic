package it.polimi.ingsw.psp26.model;

public class VaticanReportSection {

    private boolean popesFavorTileStatus;
    private int startSection;
    private int endSection;


    public VaticanReportSection(int startSection, int endSection) {
        this.popesFavorTileStatus = false;
        this.startSection = startSection;
        this.endSection = endSection;
    }

    public boolean isPopesFavorTileActive() {
        return popesFavorTileStatus;
    }

    public void activatePopesFavorTile() {
        popesFavorTileStatus = true;
    }

    public boolean isinVaticanReportSection(int markerPosition) {
        return (startSection <= markerPosition && markerPosition <= endSection);
    }

}
