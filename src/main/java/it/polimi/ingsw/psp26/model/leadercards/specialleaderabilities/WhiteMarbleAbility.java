package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;


import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

public class WhiteMarbleAbility extends SpecialAbility {

    public WhiteMarbleAbility(Resource resource) {
        super(resource);
    }


    /**
     * Activates the ability of the Leader Card
     * In this case, it replaces all the EMPTY Resources in the given List with another Resource type
     * The new Resource type is taken from the resource attribute
     *
     * @param resourceList The List where to replace all its EMPTY Resources with a new resource type
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
