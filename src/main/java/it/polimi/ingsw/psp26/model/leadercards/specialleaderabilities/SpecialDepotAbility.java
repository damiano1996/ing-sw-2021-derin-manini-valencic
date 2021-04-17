package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;

public class SpecialDepotAbility extends SpecialAbility {

    public SpecialDepotAbility(Resource resource) {
        super(resource);
    }

    @Override
    public void activate(Player player) {
        super.activate(player);

        LeaderDepot leaderDepot = new LeaderDepot(player.getVirtualView(), resource);
        player.getPersonalBoard().addLeaderDepot(leaderDepot);
    }

    public String getAbilityType() {
        return "   DEPOT LEADER   ";
    }

    @Override
    public String getPowerDescription(int linetoPrint) {
        if (linetoPrint == 0) return "| Add an extra Depot |";
        else if (linetoPrint == 1) return "| which can only     |";
        else if (linetoPrint == 2) return "| contains 2 Resour- |";
        else return "| ces of one type    |";
    }

    @Override
    public String getResourceInformation() {
        return " Resource allowed: " + getResource().getColor() + " \u25A0 " + Color.RESET;
    }
}
