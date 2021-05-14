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

        this.finalPosition = 24;
        vaticanReportSections = new VaticanReportSection[3];
        vaticanReportSections[0] = new VaticanReportSection(virtualView, 5, 8, 2, sessionToken);
        vaticanReportSections[1] = new VaticanReportSection(virtualView, 12, 16, 3, sessionToken);
        vaticanReportSections[2] = new VaticanReportSection(virtualView, 19, finalPosition, 4, sessionToken);
        this.markerPosition = 0;
        this.blackCrossPosition = 0;
        this.faithPoints = 0;
        this.sessionToken = sessionToken;
        this.victoryPoints = new int[]{0, 1, 2, 4, 6, 9, 12, 16, 20};
    }

    /**
     * Method to add points to the track.
     *
     * @param points points to add
     */
    public void addFaithPoints(int points) {
        this.faithPoints += points;
        this.markerPosition += points; //TODO ANDREBBE IMPLEMENTATO UN CONTROLLO CHE NON FACCIA SUPERARE LA MARKER POSITION OLTRE 24

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Getter of the vatican report sections
     *
     * @return an array containing the vatican report sections
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
        this.markerPosition = this.markerPosition + numberOfSteps;
        this.faithPoints = this.faithPoints + numberOfSteps;

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Method to move the black cross position given the number of steps to perform.
     *
     * @param numberOfSteps number of steps
     */
    public void moveBlackCrossPosition(int numberOfSteps) {
        this.blackCrossPosition = this.blackCrossPosition + numberOfSteps;

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
        return victoryPoints[(int) faithPoints / 3];
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
