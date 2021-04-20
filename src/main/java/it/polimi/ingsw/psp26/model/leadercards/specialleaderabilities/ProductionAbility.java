package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.HashMap;

public class ProductionAbility extends SpecialAbility {

    public ProductionAbility(Resource resource) {
        super(resource);
    }

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

    /**
     * @return The Production name
     */
    public String getName() {
        return "ProductionLeader";
    }
}
