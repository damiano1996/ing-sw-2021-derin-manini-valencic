package it.polimi.ingsw.psp26.model.personalboard;

public class VaticanReportSection {

    private final int startSection;
    private final int endSection;
    private boolean popesFavorTileStatus;


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

    public int getStartSection() {
        return startSection;
    }

    public int getEndSection() {
        return endSection;
    }

}
