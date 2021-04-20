package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;

public class SpecialDepotAbility extends SpecialAbility {

    public SpecialDepotAbility(Resource resource) {
        super(resource);
    }

    @Override
    public void activate(Player player) {
        super.activate(player);

        LeaderDepot leaderDepot = new LeaderDepot(player.getVirtualView(), resource);
        player.getPersonalBoard().getWarehouse().addLeaderDepot(leaderDepot);
    }

    /**
     * @return The Production name
     */
    public String getName() {
        return "DepotLeader";
    }

}
