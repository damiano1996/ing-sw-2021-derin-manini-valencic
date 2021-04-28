package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;


import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.MarketResourceNormalActionTurnState;
import it.polimi.ingsw.psp26.model.enums.Resource;

public class WhiteMarbleAbility extends SpecialAbility {

    public WhiteMarbleAbility(Resource resource) {
        super(resource);
    }

    @Override
    public void execute(TurnState turn) {
        super.execute(turn);

        try {

            ((MarketResourceNormalActionTurnState) turn).getTempResources()
                    .replaceAll(r -> r.equals(Resource.EMPTY) ? resource : r);


        } catch (ClassCastException e) {
        }

    }

}
