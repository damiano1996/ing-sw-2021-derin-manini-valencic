package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.MatchDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchesControllers implements Observer<Message> {

    private final List<MatchesController> matchesControllers;
    private final HashMap<Player, Integer> leaderboard;

    public MatchesControllers(VirtualView virtualView) {
        matchesControllers = new ArrayList<>();
        leaderboard = new HashMap<>();
    }

    @Override
    public void update(Message message) {

    }

    private MatchesController getMatchController(String sessionToken) throws PlayerDoesNotExistException {
        for (MatchesController matchesController : matchesControllers)
            for (Player player : matchesController.getMatch().getPlayers())
                if (player.getSessionToken().equals(sessionToken))
                    return matchesController;

        throw new PlayerDoesNotExistException();
    }

    private MatchesController getMatchController(int idMatch) throws MatchDoesNotExistException {
        return matchesControllers
                .stream()
                .filter(x -> x.getMatch().getId() == idMatch)
                .reduce((a, b) -> b)
                .orElseThrow(MatchDoesNotExistException::new);
    }

    private void updateLeaderboard(String sessionToken) {
    }

    private void updateLeaderboard(int idMatch) {
    }

    private void createMatchController(VirtualView virtualView, List<Player> players) {
        int idMatch = matchesControllers.get(matchesControllers.size() - 1).getMatch().getId() + 1;
        Match match = new Match(idMatch, players);
        MatchesController matchesController = new MatchesController(match);
        matchesControllers.add(matchesController);
    }

}
