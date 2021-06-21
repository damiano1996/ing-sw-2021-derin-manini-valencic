package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

/**
 * Class that represent the ResourceDiscountAbility.
 * This Ability makes DevelopmentCards cost 1 Resource less.
 * The Resource discounted is the characteristic Resource of the LeaderCard.
 */
public class ResourceDiscountAbility extends SpecialAbility {

    /**
     * Constructor of the class.
     * It calls the super() constructor passing it the characteristic Resource.
     *
     * @param resource the characteristic Resource type of the LeaderCard
     */
    public ResourceDiscountAbility(Resource resource) {
        super(resource);
    }


    /**
     * Activates the ability of the LeaderCard.
     * In this case, it adds a temporary Resource to the given List of Resources.
     * The Resource to add is taken from the resource attribute.
     *
     * @param tempResource the List of Resources where to add the temporary Resource
     */
    @Override
    public void execute(List<Resource> tempResource) {
        super.execute(tempResource);

        try {

            tempResource.add(resource);

        } catch (ClassCastException | UnsupportedOperationException ignored) {
        }
    }

}
