package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.Objects;

/**
 * Class modeling the vatican report section.
 */
public class VaticanReportSection extends Observable<Message> {

    private final int value;
    private final int startSection;
    private final int endSection;
    private boolean popesFavorTileStatus;

    /**
     * Constructor of the class.
     *
     * @param virtualView  virtual view to be notified on model state changes
     * @param startSection initial position of the section
     * @param endSection   final position of the section
     */
    public VaticanReportSection(VirtualView virtualView, int startSection, int endSection, int value) {
        super();
        addObserver(virtualView);

        this.popesFavorTileStatus = false;
        this.startSection = startSection;
        this.endSection = endSection;
        this.value = value;

        notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * Checks if favor tile is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isPopesFavorTileActive() {
        return popesFavorTileStatus;
    }

    /**
     * Method to activate the favor tile.
     */
    public void activatePopesFavorTile() {
        popesFavorTileStatus = true;
        notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * Checks if the marker position is in the report section.
     *
     * @param markerPosition position of the marker
     * @return true if in the section, false otherwise
     */
    public boolean isInVaticanReportSection(int markerPosition) {
        return (startSection <= markerPosition && markerPosition <= endSection);
    }

    /**
     * Getter of the index in which the section starts.
     *
     * @return position in which the section starts
     */
    public int getStartSection() {
        return startSection;
    }

    /**
     * Getter of the index in which the section ends.
     *
     * @return position in which the section ends
     */
    public int getEndSection() {
        return endSection;
    }

    /**
     * Getter of the value of the tile.
     *
     * @return the value of the tile
     */
    public int getValue() {
        return value;
    }

    /**
     * Equals method.
     *
     * @param o object to be compared
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VaticanReportSection that = (VaticanReportSection) o;
        return startSection == that.startSection &&
                endSection == that.endSection &&
                popesFavorTileStatus == that.popesFavorTileStatus && value == that.value;
    }

    /**
     * Hashing method.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(startSection, endSection, popesFavorTileStatus);
    }
}
