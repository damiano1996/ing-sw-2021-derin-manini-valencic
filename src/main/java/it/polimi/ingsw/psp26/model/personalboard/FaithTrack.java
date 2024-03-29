package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.Arrays;
import java.util.Objects;

import static it.polimi.ingsw.psp26.network.server.MessageUtils.getPlayerModelUpdateMessage;

/**
 * Class to model the faith track.
 */
public class FaithTrack extends Observable<SessionMessage> {

    private final int finalPosition;
    private final VaticanReportSection[] vaticanReportSections;
    private final String sessionToken;
    private final int[] victoryPoints;
    private int markerPosition;
    private int faithPoints;
    private int blackCrossPosition;

    /**
     * Constructor of the class.
     * It initialized the track with the vatican report sections.
     *
     * @param virtualView virtual view that must be notified in case of model changes
     */
    public FaithTrack(VirtualView virtualView, String sessionToken) {
        super();
        addObserver(virtualView);

        finalPosition = 24;
        vaticanReportSections = new VaticanReportSection[3];
        vaticanReportSections[0] = new VaticanReportSection(virtualView, 5, 8, 2, sessionToken);
        vaticanReportSections[1] = new VaticanReportSection(virtualView, 12, 16, 3, sessionToken);
        vaticanReportSections[2] = new VaticanReportSection(virtualView, 19, finalPosition, 4, sessionToken);
        markerPosition = 0;
        blackCrossPosition = 0;
        faithPoints = 0;
        this.sessionToken = sessionToken;
        victoryPoints = new int[]{0, 1, 2, 4, 6, 9, 12, 16, 20};
    }

    /**
     * Used when recovering a Match.
     * It resets the List of Observers and adds the new VirtualView passed as a parameter.
     * It also calls the restoreVirtualView() method on all the vaticanReportSections.
     *
     * @param virtualView the new VirtualView to add to the Observers List
     */
    public void recoverVirtualView(VirtualView virtualView) {
        resetObservers();
        addObserver(virtualView);

        for (VaticanReportSection vaticanReportSection : vaticanReportSections)
            vaticanReportSection.restoreVirtualView(virtualView);
    }

    /**
     * Method to add points to the FaithTrack.
     *
     * @param points points to add
     */
    public void addFaithPoints(int points) {
        faithPoints += points;
        markerPosition += points;
        if (markerPosition > finalPosition) markerPosition = finalPosition;

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Getter of the VaticanReportSections.
     *
     * @return an array containing the VaticanReportSections
     */
    public VaticanReportSection[] getVaticanReportSections() {
        return Arrays.copyOf(vaticanReportSections, vaticanReportSections.length);
    }

    /**
     * Getter of the marker position.
     *
     * @return the marker position
     */
    public int getMarkerPosition() {
        return markerPosition;
    }

    /**
     * Getter of the black cross position.
     *
     * @return the position of the black cross
     */
    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }

    /**
     * Getter of the faith points.
     *
     * @return number of faith points
     */
    public int getFaithPoints() {
        return faithPoints;
    }

    /**
     * Method to move the marker position given the number of steps to perform.
     *
     * @param numberOfSteps number of steps
     */
    public void moveMarkerPosition(int numberOfSteps) {
        markerPosition += numberOfSteps;
        faithPoints += numberOfSteps;
        if (markerPosition > finalPosition) markerPosition = finalPosition;

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Method to move the black cross position given the number of steps to perform.
     *
     * @param numberOfSteps number of steps
     */
    public void moveBlackCrossPosition(int numberOfSteps) {
        blackCrossPosition += numberOfSteps;
        if (blackCrossPosition > finalPosition) blackCrossPosition = finalPosition;

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Getter of the final position.
     *
     * @return final position of the track
     */
    public int getFinalPosition() {
        return finalPosition;
    }

    /**
     * Getter of the victory points.
     *
     * @return final position of the track
     */
    public int getVictoryPoints() {
        return victoryPoints[Math.min((faithPoints / 3), (victoryPoints.length - 1))];
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
        FaithTrack that = (FaithTrack) o;
        return finalPosition == that.finalPosition &&
                markerPosition == that.markerPosition &&
                faithPoints == that.faithPoints &&
                blackCrossPosition == that.blackCrossPosition &&
                Arrays.equals(vaticanReportSections, that.vaticanReportSections);
    }

    /**
     * Hashing method.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(finalPosition, markerPosition, faithPoints, blackCrossPosition);
        result = 31 * result + Arrays.hashCode(vaticanReportSections);
        return result;
    }

}
