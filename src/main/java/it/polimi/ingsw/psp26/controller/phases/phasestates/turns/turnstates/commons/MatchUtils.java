package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

public class MatchUtils {


    public static void addFaithPointsToPlayers(Match match, Player looserPlayer, int faithPoints) {
        for (Player player : match.getPlayers()) {
            if (!player.getSessionToken().equals(looserPlayer.getSessionToken()))
                player.getPersonalBoard().getFaithTrack().addFaithPoints(faithPoints);
        }
    }
}
