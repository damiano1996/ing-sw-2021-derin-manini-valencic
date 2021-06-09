package it.polimi.ingsw.psp26.view.gui.manualtesting;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.gui.GUI;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.WarehousePlacerDrawer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.DialogStage.getDialog;

public class WarehousePlacerDrawerTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Warehouse warehouse = new Warehouse(new VirtualView(), 3, "");
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), Resource.STONE));
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), Resource.SERVANT));

        try {
            warehouse.addResourceToDepot(0, Resource.SHIELD);
//            warehouse.addResourceToDepot(1, Resource.STONE);
            warehouse.addResourceToDepot(2, Resource.COIN);
            warehouse.addResourceToDepot(2, Resource.COIN);
        } catch (CanNotAddResourceToDepotException canNotAddResourceToWarehouse) {
            canNotAddResourceToWarehouse.printStackTrace();
        }

        List<Resource> resourcesToAdd = new ArrayList<>() {{
            add(Resource.COIN);
            add(Resource.STONE);
            add(Resource.STONE);
            add(Resource.COIN);
            add(Resource.SERVANT);
            add(Resource.SERVANT);
            add(Resource.SHIELD);
            add(Resource.SHIELD);
        }};

        Stage dialog = getDialog(stage, new WarehousePlacerDrawer(new Client(new GUI()), warehouse, resourcesToAdd, 1920).draw());
        dialog.show();
    }
}
