package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;

public class SpecialDepotAbility extends SpecialAbility {

    public SpecialDepotAbility(Resource resource) {
        super(resource);
    }


    /**
     * Activates the ability of the Leader Card
     * In this case, it adds a new Leader Depot into the Player's Warehouse
     * The Resource Type that the new Leader Depot can contain is taken from the resource attribute
     *
     * @param player The Player that will get the new Leader Depot
     */
    @Override
    public void activate(Player player) {
        super.activate(player);

        LeaderDepot leaderDepot = new LeaderDepot(player.getVirtualView(), resource);
        player.getPersonalBoard().getWarehouse().addLeaderDepot(leaderDepot);
    }

}
