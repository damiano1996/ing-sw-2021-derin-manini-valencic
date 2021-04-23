package it.polimi.ingsw.psp26.application.messages.specialpayloads;

import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;

import java.util.List;

public class WarehousePlacerPayload {

    private final Warehouse warehouse;
    private final List<Resource> resourcesToAdd;

    public WarehousePlacerPayload(Warehouse warehouse, List<Resource> resourcesToAdd) {
        this.warehouse = warehouse;
        this.resourcesToAdd = resourcesToAdd;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public List<Resource> getResourcesToAdd() {
        return resourcesToAdd;
    }
}
