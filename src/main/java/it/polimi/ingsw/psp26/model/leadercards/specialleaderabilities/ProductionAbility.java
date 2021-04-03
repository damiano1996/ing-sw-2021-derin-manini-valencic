package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

public class ProductionAbility extends SpecialAbility {

    public ProductionAbility(Resource resource) {
        super(resource);
    }


    public String getAbilityType() {
        return "PRODUCTION  LEADER";
    }

    @Override
    public String getPowerDescription(int linetoPrint) {
        if (linetoPrint == 0) return "| Pay one Resource   |";
        else if (linetoPrint == 1) return "| and get 1 Faith    |";
        else if (linetoPrint == 2) return "| Point and 1 Resou- |";
        else return "| rce of your choice |";
    }

    @Override
    public String getResourceInformation() {
        return " Resource to pay: " + getResource().getColor() + "  \u25A0 " + Color.RESET;
    }
}
