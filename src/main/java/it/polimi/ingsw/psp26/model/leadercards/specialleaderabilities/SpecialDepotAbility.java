package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;

/**
 * Class that represent the SpecialDepotAbility.
 * This Ability adds a new LeaderDepot in the Player's Warehouse.
 * The Resource the added LeaderDepot can contain is the characteristic Resource of the LeaderCard.
 */
public class SpecialDepotAbility extends SpecialAbility {

    /**
     * Constructor of the class.
     * It calls the super() constructor passing it the characteristic Resource.
     *
     * @param resource the characteristic Resource type of the LeaderCard
     */
    public SpecialDepotAbility(Resource resource) {
        super(resource);
    }


    /**
     * Activates the ability of the LeaderCard.
     * In this case, it adds a new LeaderDepot into the Player's Warehouse.
     * The Resource Type that the new LeaderDepot can contain is taken from the resource attribute.
     *
     * @param player the Player that will get the new LeaderDepot
     */
    @Override
    public void activate(Player player) {
        super.activate(player);

        LeaderDepot leaderDepot = new LeaderDepot(player.getVirtualView(), resource);
        player.getPersonalBoard().getWarehouse().addLeaderDepot(leaderDepot);
    }

}
