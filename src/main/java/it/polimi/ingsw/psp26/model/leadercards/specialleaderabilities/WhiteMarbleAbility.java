package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

/**
 * Class that represent the WhiteMarbleAbility.
 * This Ability gives an extra Resource when the Player takes white marbles from the MarketTray.
 * The Resource given are based on the characteristic resource of the LeaderCard.
 */
public class WhiteMarbleAbility extends SpecialAbility {

    /**
     * Constructor of the class.
     * It calls the super() constructor passing it the characteristic Resource.
     *
     * @param resource the characteristic Resource type of the LeaderCard
     */
    public WhiteMarbleAbility(Resource resource) {
        super(resource);
    }


    /**
     * Activates the ability of the LeaderCard.
     * In this case, it replaces all the EMPTY Resources in the given List with another Resource type.
     * The new Resource type is taken from the resource attribute.
     *
     * @param resourceList the List where to replace all its EMPTY Resources with a new resource type
     */
    @Override
    public void execute(List<Resource> resourceList) {
        super.execute(resourceList);

        try {

            resourceList.replaceAll(r -> r.equals(Resource.EMPTY) ? resource : r);

        } catch (ClassCastException ignored) {
        }
    }

}
