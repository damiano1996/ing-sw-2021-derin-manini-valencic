package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

public class ResourceDiscountAbility extends SpecialAbility {

    public ResourceDiscountAbility(Resource resource) {
        super(resource);
    }


    /**
     * Activates the ability of the Leader Card
     * In this case, it adds a temporary Resource to the given List of Resources
     * The Resource to add is taken from the resource attribute
     *
     * @param tempResource The List of Resources where to add the temporary Resource
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
