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

    public WarehousePlacerDrawer(Client client, Warehouse warehouse, List<Resource> resourcesToAdd, int maxWidth) {
        super(maxWidth);

        this.client = client;
        this.warehouse = warehouse;
        this.resourcesToAdd = resourcesToAdd;
        targets = new ArrayList<>();
    }

    @Override
    public Pane draw() {

        VBox rootPane = new VBox(10 * ratio);
        Text text = new Text("Place Resources in the Warehouse");
        text.setId("title");
        rootPane.getChildren().add(text);

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

        return rootPane;
    }

    private Pane drawResourcesToAdd(List<Pane> panesToDraw) {

        Pane resourcesPane = new Pane();
        Random random = new Random();
        int maxHeight = 400;
        int componentWidth = 500;

        for (int i = 0; i < panesToDraw.size(); i++) {
            Pane paneToDraw = panesToDraw.get(i);
            resourcesPane.getChildren().add(paneToDraw);
            paneToDraw.setLayoutX((componentWidth + componentWidth * i) * ratio);
        }

        for (Resource resource : resourcesToAdd) {

            Image resourceImage = getResourceImage(resource, ratio);
            ImageView imageView = getImageView(resourceImage, random.nextInt(componentWidth - 100) * ratio, random.nextInt(maxHeight) * ratio);
            new DraggedObject<>(resource, resourcesPane, imageView, targets);
            resourcesPane.getChildren().add(imageView);
        }

        return resourcesPane;
    }

    private Pane getWarehousePane() {

        StackPane warehousePane = new StackPane();

        Image warehouseImage = loadImage("warehouse/warehouse.png", (int) (600 * ratio));
        warehouseImage = setRoundedCorners(warehouseImage, ratio);
        warehouseImage = addLightEffects(warehouseImage, ratio);

        ImageView warehouseImageView = getImageView(warehouseImage, 0, 0);

        warehousePane.getChildren().add(warehouseImageView);

        VBox depotsTargets = new VBox();
        for (Depot depot : warehouse.getBaseDepots()) {
            StackPane targetAnchor = new StackPane();

            HBox depotBox = new HBox();
            targetAnchor.getChildren().add(depotBox);
            DepotTargetContainer depotTargetContainer = new DepotTargetContainer(targetAnchor, warehouse, warehouse.getBaseDepots().indexOf(depot));

            for (Resource resource : depot.getResources()) {
                ImageView resourceImageView = getImageView(getResourceImage(resource, ratio), 0, 0);
                depotBox.getChildren().add(resourceImageView);
            }
            targets.add(depotTargetContainer);
            depotsTargets.getChildren().add(depotBox);
        }

        warehousePane.getChildren().add(depotsTargets);

        return warehousePane;
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

            resourcesBox.setAlignment(Pos.CENTER_LEFT);
            imageStack.getChildren().add(resourcesBox);
            vBox.getChildren().add(imageStack);

            System.out.println("WarehousePlacerDrawer - index: " + warehouse.getAllDepots().indexOf(leaderDepot));
            DepotTargetContainer depotTargetContainer = new DepotTargetContainer(resourcesBox, warehouse, warehouse.getAllDepots().indexOf(leaderDepot));
            targets.add(depotTargetContainer);

        }

        return vBox;
    }
}
