package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.BuyCardNormalActionTurnState;
import it.polimi.ingsw.psp26.model.enums.Resource;

public class ResourceDiscountAbility extends SpecialAbility {

    public ResourceDiscountAbility(Resource resource) {
        super(resource);
    }

    @Override
    public void execute(TurnState turn) {
        super.execute(turn);

        try {

            ((BuyCardNormalActionTurnState) turn).getTempResources().add(resource);

        } catch (ClassCastException e) {
        }

    }

}
