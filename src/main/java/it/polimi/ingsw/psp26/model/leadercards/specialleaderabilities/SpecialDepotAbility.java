package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

public class SpecialDepotAbility extends SpecialAbility {

    public SpecialDepotAbility(Resource resource) {
        super(resource);
    }



    public String getAbilityType() {
        return "   DEPOT LEADER   ";
    }

    @Override
    public String getPowerDescription(int linetoPrint) {
        if (linetoPrint == 0) return      "| Add an extra Depot |";
        else if (linetoPrint == 1) return "| which can only     |";
        else if (linetoPrint == 2) return "| contains 2 Resour- |";
        else return                       "| ces of one type    |";
    }

    @Override
    public String getResourceInformation() {
        return " Resource allowed: " + getResource().getColor() + " \u25A0 " + Color.RESET;
    }
}
