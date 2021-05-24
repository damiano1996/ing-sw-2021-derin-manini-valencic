package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

public class ResourceDiscountAbility extends SpecialAbility {

    public ResourceDiscountAbility(Resource resource) {
        super(resource);
    }

    @Override
    public void execute(List<Resource> tempResource) {
        super.execute((List<Resource>) tempResource);

        try {

            tempResource.add(resource);

        } catch (ClassCastException | UnsupportedOperationException ignored) {
        }

    }

}
