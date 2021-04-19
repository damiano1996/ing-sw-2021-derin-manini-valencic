package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.List;
import java.util.stream.Collectors;

public class MatchUtils {

    public static List<Resource> getAllPlayerResources(Player player) {
        List<Resource> resources = getWarehouseResources(player);
        resources.addAll(player.getPersonalBoard().getStrongbox());
        return resources;
    }

    public static List<Resource> getWarehouseResources(Player player) {
        return player.getPersonalBoard().getWarehouseDepots()
                .stream()
                .map(Depot::getResources)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static void addFaithPointsToPlayers(Match match, Player looserPlayer, int faithPoints) {
        for (Player player : match.getPlayers()) {
            if (!player.getSessionToken().equals(looserPlayer.getSessionToken()))
                player.getPersonalBoard().getFaithTrack().addFaithPoints(faithPoints);
        }
    }
}
