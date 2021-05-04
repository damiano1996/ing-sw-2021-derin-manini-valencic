package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.BuyCardNormalActionTurnState;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;

public class ResourceDiscountAbility extends SpecialAbility {

    public ResourceDiscountAbility(Resource resource) {
        super(resource);
    }

    @Override
    public void execute(List<Resource> resourceList) {
        super.execute(resourceList);

        try {

           resourceList.add(resource);

        } catch (ClassCastException e) {
        }

    }

}
