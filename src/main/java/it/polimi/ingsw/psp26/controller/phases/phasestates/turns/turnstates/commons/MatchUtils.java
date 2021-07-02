package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

/**
 * Class that offers some generic method to the turn states.
 */
public class MatchUtils {


    /**
     * Method to add faith points to players (and Lorenzo), except the looser one.
     *
     * @param match        match in which the looserPlayer is playing
     * @param looserPlayer player that discarded the resources (the one that is not obtaining points)
     * @param faithPoints  number of faith points to assign to opponents
     */
    public static void addFaithPointsToPlayers(Match match, Player looserPlayer, int faithPoints) {
        for (Player player : match.getPlayers()) {
            if (!player.getSessionToken().equals(looserPlayer.getSessionToken()))
                player.getPersonalBoard().getFaithTrack().addFaithPoints(faithPoints);
        }

        if (!match.isMultiPlayerMode())
            looserPlayer.getPersonalBoard().getFaithTrack().moveBlackCrossPosition(faithPoints);
    }
}
