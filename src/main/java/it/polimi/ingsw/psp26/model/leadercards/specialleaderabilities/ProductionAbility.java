package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.HashMap;

/**
 * Class that represent the ProductionAbility.
 * This Ability adds a new Production to the available Player's Productions.
 */
public class ProductionAbility extends SpecialAbility {

    /**
     * Constructor of the class.
     * It calls the super() constructor passing it the characteristic Resource.
     *
     * @param resource the characteristic Resource type of the LeaderCard
     */
    public ProductionAbility(Resource resource) {
        super(resource);
    }


    /**
     * Activates the ability of the LeaderCard.
     * In this case, it adds a new Production Power to the Player Productions.
     * The Production power cost is taken from the resource attribute.
     * The Production power return is the same for all the LeaderCards.
     *
     * @param player the Player that activates the LeaderCard
     */
    @Override
    public void activate(Player player) {
        super.activate(player);

        Production production = new Production(
                new HashMap<>() {{
                    put(resource, 1);
                }},
                new HashMap<>() {{
                    put(Resource.UNKNOWN, 1);
                    put(Resource.FAITH_MARKER, 1);
                }});

        player.getPersonalBoard().addProduction(production);
    }

}
