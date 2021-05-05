package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;


import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

public class WhiteMarbleAbility extends SpecialAbility {

    public WhiteMarbleAbility(Resource resource) {
        super(resource);
    }

    @Override
    public void execute(List<Resource> resourceList) {
        super.execute(resourceList);

        try {

            resourceList.replaceAll(r -> r.equals(Resource.EMPTY) ? resource : r);


        } catch (ClassCastException ignored) {
        }

    }

}
