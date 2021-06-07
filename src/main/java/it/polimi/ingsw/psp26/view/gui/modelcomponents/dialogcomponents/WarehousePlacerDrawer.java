package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.SoundManager;
import it.polimi.ingsw.psp26.view.gui.drag.DraggedObject;
import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.DepotTargetContainer;
import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.TargetContainer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.switchdepots.DepotsSwitchableGroup;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.switchdepots.SwitchableDepot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
    private final DepotsSwitchableGroup switchableGroup;
    private Text errorMessage;

    public WarehousePlacerDrawer(Client client, Warehouse warehouse, List<Resource> resourcesToAdd, int maxWidth) {
        super((int) (maxWidth * 1.2f));

        this.client = client;
        this.warehouse = warehouse;
        this.resourcesToAdd = resourcesToAdd;
        targets = new ArrayList<>();

        resourcesToAddPane = new Pane();
        switchableGroup = new DepotsSwitchableGroup(this);

        errorMessage = new Text();
        errorMessage.setId("error");
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public Pane draw() {

        VBox rootPane = new VBox(10 * ratio);
        Text text = new Text("Place Resources in the Warehouse");
        text.setId("title");
        text.setStyle("-fx-font-size: " + 120 * ratio + ";");
        rootPane.getChildren().add(text);

        Text description = new Text(
                "*Drag and drop resources in the depots.\n" +
                        "**Click over depots to switch content among them.");
        description.setId("title");
        description.setStyle("-fx-font-size: " + 70 * ratio + ";");
        rootPane.getChildren().add(description);

        Pane warehousePane = getWarehousePane();
        Pane leaderDepotPane = getLeaderDepotsPane();

        rootPane.getChildren().add(drawResourcesToAdd(
                new ArrayList<>() {{
                    add(warehousePane);
                    add(leaderDepotPane);
                }}
        ));

        errorMessage.setVisible(false);
        rootPane.getChildren().add(errorMessage);

        Button confirmationButton = new Button("Done");
        confirmationButton.setId("confirm-button");
        confirmationButton.setOnAction(actionEvent -> {
            SoundManager soundManager= SoundManager.getInstance();
            soundManager.setSoundEffect("button-21.mp3");
            try {
                client.notifyObservers(
                        new Message(
                                MessageType.PLACE_IN_WAREHOUSE,
                                createListToSend(warehouse).toArray()
                        )
                );
                closeParentStageOfActionEvent(actionEvent);
                client.viewNext();
            } catch (InvalidPayloadException ignored) {

            }
        });

        rootPane.getChildren().add(confirmationButton);

        rootPane.setAlignment(Pos.CENTER);

        return rootPane;
    }

    private Pane drawResourcesToAdd(List<Pane> panesToDraw) {

        int componentWidth = 600;

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
        int componentWidth = 600;

        Random random = new Random();

        Image resourceImage = getResourceImage(resource, ratio);
        ImageView imageView = getImageView(resourceImage, random.nextInt(componentWidth - 100) * ratio, random.nextInt(maxHeight) * ratio);
        new DraggedObject<>(resource, resourcesToAddPane, imageView, targets);
        resourcesToAddPane.getChildren().add(imageView);
    }

    private Pane getWarehousePane() {

        int[] leftOffsets = new int[]{220, 160, 80};
        int[] topOffsets = new int[]{0, 0, 0};
        int[] bottomOffsets = new int[]{0, 0, 0};

        StackPane warehousePane = new StackPane();

        SoundManager soundManager= SoundManager.getInstance();
        soundManager.setSoundEffect("button-22.mp3");

        Image warehouseImage = loadImage("warehouse/warehouse.png", (int) (650 * ratio));
        warehouseImage = setRoundedCorners(warehouseImage, ratio);
        warehouseImage = addLightEffects(warehouseImage, ratio);
        ImageView warehouseImageView = getImageView(warehouseImage, 0, 0);
        warehousePane.getChildren().add(warehouseImageView);

        VBox vBox = new VBox(0);

        for (Depot baseDepot : warehouse.getBaseDepots()) {
            int depotIndex = warehouse.getBaseDepots().indexOf(baseDepot);

            // Opacity zero image view. It is used to keep distance between depot-targets even if no resources are in the depots
            Image baseDepotImage = loadImage("warehouse/warehouse_" + depotIndex + ".png", (int) (warehouseImage.getWidth() - 50 * ratio));
            baseDepotImage = setRoundedCorners(baseDepotImage, ratio);
            ImageView baseDepotImageView = getImageView(baseDepotImage, 0, 0);
            baseDepotImageView.setOpacity(0.1);
            StackPane imageStack = new StackPane(baseDepotImageView);

            GridPane resourcesGridPane = new GridPane();
            resourcesGridPane.setVgap(0 * ratio);
            resourcesGridPane.setHgap(-10 * ratio);
            resourcesGridPane.setPadding(new Insets(topOffsets[depotIndex] * ratio, 0, bottomOffsets[depotIndex] * ratio, leftOffsets[depotIndex] * ratio)); // top / right / bottom / left
            drawResources(vBox, baseDepot, imageStack, resourcesGridPane);

            System.out.println("WarehousePlacerDrawer - index: " + depotIndex);
            DepotTargetContainer depotTargetContainer = new DepotTargetContainer(resourcesGridPane, imageStack, warehouse, depotIndex);
            SwitchableDepot switchableDepot = new SwitchableDepot(depotIndex, switchableGroup, imageStack, baseDepotImageView, resourcesGridPane);
            switchableGroup.addSwitchableDepot(switchableDepot);
            targets.add(depotTargetContainer);

        }

        warehousePane.getChildren().add(vBox);
        return warehousePane;
    }

    private void drawResources(VBox vBox, Depot baseDepot, StackPane imageStack, GridPane resourcesGridPane) {
        for (int i = 0; i < baseDepot.getResources().size(); i++)
            resourcesGridPane.add(getImageView(getResourceImage(baseDepot.getResources().get(i), ratio), 0, 0), i, 1, 1, 1);

        resourcesGridPane.setAlignment(Pos.CENTER_LEFT);
        imageStack.getChildren().add(resourcesGridPane);

        SoundManager soundManager= SoundManager.getInstance();
        soundManager.setSoundEffect("button-22.mp3");

        vBox.getChildren().add(imageStack);
    }

    private Pane getLeaderDepotsPane() {

        VBox vBox = new VBox(5 * ratio);

        for (LeaderDepot leaderDepot : warehouse.getLeaderDepots()) {
            Image leaderImage = loadImage("leader_depots/" + leaderDepot.toString() + ".png", (int) (400 * ratio));
            leaderImage = setRoundedCorners(leaderImage, ratio);
            leaderImage = addLightEffects(leaderImage, ratio);

            ImageView imageView = getImageView(leaderImage, 0, 0);
            StackPane imageStack = new StackPane(imageView);

            GridPane resourcesGridPane = new GridPane();
            resourcesGridPane.setPadding(new Insets(0, 0, 0, 30 * ratio)); // top / right / bottom / left
            resourcesGridPane.setHgap(30 * ratio);
            drawResources(vBox, leaderDepot, imageStack, resourcesGridPane);

            System.out.println("WarehousePlacerDrawer - index: " + warehouse.getAllDepots().indexOf(leaderDepot));
            DepotTargetContainer depotTargetContainer = new DepotTargetContainer(resourcesGridPane, imageStack, warehouse, warehouse.getAllDepots().indexOf(leaderDepot));
            targets.add(depotTargetContainer);

        }

        return vBox;
    }

    public void showMessage(String error) {
        errorMessage.setText(error);
        errorMessage.setVisible(true);
    }

    public void hideMessage() {
        errorMessage.setVisible(false);
    }
}
