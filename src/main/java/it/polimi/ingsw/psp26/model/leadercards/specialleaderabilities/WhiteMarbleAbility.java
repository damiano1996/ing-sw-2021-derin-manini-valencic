package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

public class WhiteMarbleAbility extends SpecialAbility {

    public WhiteMarbleAbility(Resource resource) {
        super(resource);
    }



    public String getAbilityType() {
        return "  MARBLE  LEADER  ";
    }

    @Override
    public String getPowerDescription(int linetoPrint) {
        if (linetoPrint == 0) return      "| Gives you an extra |";
        else if (linetoPrint == 1) return "| Resource when you  |";
        else if (linetoPrint == 2) return "| get a white Marble |";
        else return                       "| from the Market    |";
    }

    @Override
    public String getResourceInformation() {
        return " Resource obtained: " + getResource().getColor() + "\u25A0 " + Color.RESET;
    }
}
