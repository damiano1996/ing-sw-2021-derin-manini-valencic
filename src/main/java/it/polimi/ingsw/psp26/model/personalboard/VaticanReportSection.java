package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.Objects;

import static it.polimi.ingsw.psp26.network.server.MessageUtils.getPlayerModelUpdateMessage;

/**
 * Class modeling the vatican report section.
 */
public class VaticanReportSection extends Observable<SessionMessage> {

    private final int value;
    private final int startSection;
    private final int endSection;
    private final String sessionToken;
    private boolean popesFavorTileStatus;
    private boolean popesFavorTileDiscarded;

    /**
     * Constructor of the class.
     *
     * @param virtualView  virtual view to be notified on model state changes
     * @param startSection initial position of the section
     * @param endSection   final position of the section
     */
    public VaticanReportSection(VirtualView virtualView, int startSection, int endSection, int value, String sessionToken) {
        super();
        addObserver(virtualView);

        this.popesFavorTileStatus = false;
        this.popesFavorTileDiscarded = false;
        this.startSection = startSection;
        this.endSection = endSection;
        this.value = value;
        this.sessionToken = sessionToken;
    }

    /**
     * Used when recovering a Match.
     * It resets the List of Observers and adds the new VirtualView passed as a parameter.
     *
     * @param virtualView the new VirtualView to add to the Observers List
     */
    public void restoreVirtualView(VirtualView virtualView) {
        resetObservers();
        addObserver(virtualView);
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
     * Checks if favor tile is discarded.
     *
     * @return true if active, false otherwise
     */
    public boolean isPopesFavorTileDiscarded() {
        return popesFavorTileDiscarded;
    }

    /**
     * Method to activate the favor tile.
     */
    public void activatePopesFavorTile() {
        popesFavorTileStatus = true;

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Method to discard the favor tile.
     */
    public void discardPopesFavorTile() {
        popesFavorTileDiscarded = true;

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
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
