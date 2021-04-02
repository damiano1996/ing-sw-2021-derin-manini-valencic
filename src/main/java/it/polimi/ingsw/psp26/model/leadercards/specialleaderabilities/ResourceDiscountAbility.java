package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

public class ResourceDiscountAbility extends SpecialAbility {

    public ResourceDiscountAbility(Resource resource) {
        super(resource);
    }



    public String getAbilityType() {
        return " DISCOUNT  LEADER ";
    }

    @Override
    public String getPowerDescription(int linetoPrint) {
        if (linetoPrint == 0) return      "| When buying a      |";
        else if (linetoPrint == 1) return "| DevelopmentCard    |";
        else if (linetoPrint == 2) return "| you pay one Resou- |";
        else return                       "| ce less            |";
    }

    @Override
    public String getResourceInformation() {
        return " Resource discount: " + getResource().getColor() + "\u25A0 " + Color.RESET;
    }
}
