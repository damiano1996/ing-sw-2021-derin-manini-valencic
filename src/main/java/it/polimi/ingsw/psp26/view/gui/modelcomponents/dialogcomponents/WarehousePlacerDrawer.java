package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.drag.DraggedObject;
import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.DepotTargetContainer;
import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.TargetContainer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.switchdepots.SwitchableDepot;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.switchdepots.SwitchableGroup;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.psp26.view.ViewUtils.createListToSend;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getResourceImage;

public class WarehousePlacerDrawer extends RatioDrawer {

    private final Client client;
    private final Warehouse warehouse;
    private final List<Resource> resourcesToAdd;
    private final List<TargetContainer<Resource>> targets;

    private final Pane resourcesToAddPane;

    private final SwitchableGroup switchableGroup;

    public WarehousePlacerDrawer(Client client, Warehouse warehouse, List<Resource> resourcesToAdd, int maxWidth) {
        super(maxWidth);

        this.client = client;
        this.warehouse = warehouse;
        this.resourcesToAdd = resourcesToAdd;
        targets = new ArrayList<>();

        resourcesToAddPane = new Pane();
        switchableGroup = new SwitchableGroup(warehouse, this);
    }

    @Override
    public Pane draw() {

        VBox rootPane = new VBox(10 * ratio);
        Text text = new Text("Place Resources in the Warehouse");
        text.setId("title");
        rootPane.getChildren().add(text);

        Text description = new Text(
                "*Drag and drop resources in the depots.\n" +
                        "**Click over depots to switch content among them.");
        description.setId("title");
        description.setStyle("-fx-font-size: " + 40 * ratio + ";");
        rootPane.getChildren().add(description);

        Pane warehousePane = getWarehousePane();
        Pane leaderDepotPane = getLeaderDepotsPane();

        rootPane.getChildren().add(drawResourcesToAdd(
                new ArrayList<>() {{
                    add(warehousePane);
                    add(leaderDepotPane);
                }}
        ));

        Button confirmationButton = new Button("Done");
        confirmationButton.setId("confirm-button");
        confirmationButton.setOnAction(actionEvent -> {
            try {
                client.notifyObservers(
                        new Message(
                                MessageType.PLACE_IN_WAREHOUSE,
                                createListToSend(warehouse).toArray()
                        )
                );
            } catch (InvalidPayloadException ignored) {

            }
        });

        rootPane.getChildren().add(confirmationButton);

        rootPane.setAlignment(Pos.CENTER);

        return rootPane;
    }

    private Pane drawResourcesToAdd(List<Pane> panesToDraw) {

        int componentWidth = 550;

        for (int i = 0; i < panesToDraw.size(); i++) {
            Pane paneToDraw = panesToDraw.get(i);
            resourcesToAddPane.getChildren().add(paneToDraw);
            paneToDraw.setLayoutX((componentWidth + componentWidth * i) * ratio);
        }

        for (Resource resource : resourcesToAdd) placeResourceToAdd(resource);

        return resourcesToAddPane;
    }

    public void placeResourceToAdd(Resource resource) {
        int maxHeight = 400;
        int componentWidth = 550;

        Random random = new Random();

        Image resourceImage = getResourceImage(resource, ratio);
        ImageView imageView = getImageView(resourceImage, random.nextInt(componentWidth - 100) * ratio, random.nextInt(maxHeight) * ratio);
        new DraggedObject<>(resource, resourcesToAddPane, imageView, targets);
        resourcesToAddPane.getChildren().add(imageView);
    }

    public void addResourceToAdd(Resource resource) {
        resourcesToAdd.add(resource);
    }

    private Pane getWarehousePane() {

        VBox vBox = new VBox();

        for (Depot baseDepot : warehouse.getBaseDepots()) {
            Image baseDepotImage = loadImage("warehouse/warehouse_" + warehouse.getBaseDepots().indexOf(baseDepot) + ".png", (int) (500 * ratio));
            baseDepotImage = setRoundedCorners(baseDepotImage, ratio);
            baseDepotImage = addLightEffects(baseDepotImage, ratio);

            ImageView imageView = getImageView(baseDepotImage, 0, 0);
            StackPane imageStack = new StackPane(imageView);

            HBox resourcesBox = new HBox();
            for (Resource resource : baseDepot.getResources())
                resourcesBox.getChildren().add(getImageView(getResourceImage(resource, ratio), 0, 0));

            resourcesBox.setAlignment(Pos.CENTER);
            imageStack.getChildren().add(resourcesBox);
            vBox.getChildren().add(imageStack);

            System.out.println("WarehousePlacerDrawer - index: " + warehouse.getAllDepots().indexOf(baseDepot));
            DepotTargetContainer depotTargetContainer = new DepotTargetContainer(resourcesBox, warehouse, warehouse.getAllDepots().indexOf(baseDepot));
            SwitchableDepot switchableDepot = new SwitchableDepot(baseDepot, switchableGroup, imageStack, imageView, resourcesBox);
            switchableGroup.addSwitchableDepot(switchableDepot);
            targets.add(depotTargetContainer);

        }

        return vBox;
    }

    private Pane getLeaderDepotsPane() {

        VBox vBox = new VBox(5 * ratio);

        for (LeaderDepot leaderDepot : warehouse.getLeaderDepots()) {
            Image leaderImage = loadImage("leader_depots/" + leaderDepot.toString() + ".png", (int) (400 * ratio));
            leaderImage = setRoundedCorners(leaderImage, ratio);
            leaderImage = addLightEffects(leaderImage, ratio);

            ImageView imageView = getImageView(leaderImage, 0, 0);
            StackPane imageStack = new StackPane(imageView);

            HBox resourcesBox = new HBox(50 * ratio);
            for (Resource resource : leaderDepot.getResources())
                resourcesBox.getChildren().add(getImageView(getResourceImage(resource, ratio), 0, 0));

            resourcesBox.setAlignment(Pos.CENTER);
            imageStack.getChildren().add(resourcesBox);
            vBox.getChildren().add(imageStack);

            System.out.println("WarehousePlacerDrawer - index: " + warehouse.getAllDepots().indexOf(leaderDepot));
            DepotTargetContainer depotTargetContainer = new DepotTargetContainer(resourcesBox, warehouse, warehouse.getAllDepots().indexOf(leaderDepot));
            targets.add(depotTargetContainer);

        }

        return vBox;
    }
}
