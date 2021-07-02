package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.NoImageException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.drag.DraggedObject;
import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.DepotTargetContainer;
import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.TargetContainer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.switchdepots.DepotsSwitchableGroup;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.switchdepots.SwitchableDepot;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
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
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.getResourceImage;

/**
 * Class to create the pane that will contain the warehouse and the resources that must be placed in.
 */
public class WarehousePlacerDrawer extends RatioDrawer {

    private final Client client;
    private final Warehouse warehouse;
    private final List<Resource> resourcesToAdd;
    private final List<TargetContainer<Resource>> targets;

    private final Pane resourcesToAddPane;
    private final DepotsSwitchableGroup switchableGroup;
    private final Text errorMessage;

    /**
     * Class constructor.
     *
     * @param client         client object
     * @param warehouse      warehouse object to draw
     * @param resourcesToAdd list of resources to add in the warehouse
     * @param maxWidth       maximum allowed width for the pane
     */
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

    /**
     * Getter of the warehouse
     *
     * @return warehouse object
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Method to draw the pane containing the warehouse and the resources to place in.
     *
     * @return pane
     */
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
            SoundManager soundManager = SoundManager.getInstance();
            soundManager.setSoundEffect(SoundManager.DIALOG_SOUND);
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

    /**
     * Method to draw the received resources, that must be placed in the warehouse, and
     * the other components that must be displayed in the pane (such as the warehouse and the leader depots).
     *
     * @param panesToDraw list of pane
     * @return pane
     */
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

    /**
     * Method to draw a resource in the pane.
     *
     * @param resource resource to add in the pane
     */
    public void placeResourceToAdd(Resource resource) {
        int maxHeight = 400;
        int componentWidth = 600;

        Random random = new Random();

        try {
            Image resourceImage = getResourceImage(resource, ratio);
            ImageView imageView = getImageView(resourceImage, random.nextInt(componentWidth - 100) * ratio, random.nextInt(maxHeight) * ratio);
            new DraggedObject<>(resource, resourcesToAddPane, imageView, targets);
            resourcesToAddPane.getChildren().add(imageView);
        } catch (NoImageException ignored) {
        }
    }

    /**
     * Method to draw the warehouse with the resources.
     * It initializes the warehouse and draws the resources that are already in.
     * Moreover, it sets the targets for the resources to add.
     * Targets for the draggable objects are image views placed over the image of the entire warehouse.
     *
     * @return pane containing the warehouse
     */
    private Pane getWarehousePane() {

        int[] leftOffsets = new int[]{240, 170, 100};
        int[] topOffsets = new int[]{0, 0, 0};
        int[] bottomOffsets = new int[]{0, 0, 0};

        StackPane warehousePane = new StackPane();

        Image warehouseImage = loadImage("warehouse/warehouse.png", (int) (650 * ratio));
        warehouseImage = setRoundedCorners(warehouseImage, ratio);
        warehouseImage = addLightEffects(warehouseImage, ratio);
        ImageView warehouseImageView = getImageView(warehouseImage, 0, 0);
        warehousePane.getChildren().add(warehouseImageView);

        VBox vBox = new VBox(0);

        for (Depot baseDepot : warehouse.getBaseDepots()) {
            int depotIndex = warehouse.getBaseDepots().indexOf(baseDepot);

            // Opacity zero image view. It is used to keep distance between depot-targets even if no resources are in the depots
            Image baseDepotImage = loadImage("warehouse/warehouse_" + depotIndex + ".png", (int) (warehouseImage.getWidth()));
            baseDepotImage = setRoundedCorners(baseDepotImage, ratio);
            ImageView baseDepotImageView = getImageView(baseDepotImage, 0, 0);
            baseDepotImageView.setOpacity(0.0);
            StackPane imageStack = new StackPane(baseDepotImageView);

            GridPane resourcesGridPane = new GridPane();
            resourcesGridPane.setVgap(0 * ratio);
            resourcesGridPane.setHgap(-10 * ratio);
            resourcesGridPane.setPadding(
                    // top / right / bottom / left
                    new Insets(topOffsets[depotIndex] * ratio,
                            0,
                            bottomOffsets[depotIndex] * ratio,
                            leftOffsets[depotIndex] * ratio)
            );
            drawResources(vBox, baseDepot, imageStack, resourcesGridPane);

            System.out.println("WarehousePlacerDrawer - index: " + depotIndex);
            DepotTargetContainer depotTargetContainer = new DepotTargetContainer(
                    resourcesGridPane, imageStack, warehouse, depotIndex
            );
            SwitchableDepot switchableDepot = new SwitchableDepot(
                    depotIndex, switchableGroup, imageStack, baseDepotImageView, resourcesGridPane
            );
            switchableGroup.addSwitchableDepot(switchableDepot);
            targets.add(depotTargetContainer);

        }

        warehousePane.getChildren().add(vBox);
        return warehousePane;
    }

    /**
     * Method to draw resources in the grid pane.
     *
     * @param vBox              vertical box that contains the depot
     * @param baseDepot         depot object to draw
     * @param imageStack        stack pane that contains the image of the depot
     * @param resourcesGridPane grid pane containing the resources
     */
    private void drawResources(VBox vBox, Depot baseDepot, StackPane imageStack, GridPane resourcesGridPane) {
        for (int i = 0; i < baseDepot.getResources().size(); i++) {
            try {
                resourcesGridPane.add(getImageView(getResourceImage(baseDepot.getResources().get(i), ratio), 0, 0), i, 1, 1, 1);
            } catch (NoImageException ignored) {
            }
        }

        resourcesGridPane.setAlignment(Pos.CENTER_LEFT);
        imageStack.getChildren().add(resourcesGridPane);

        vBox.getChildren().add(imageStack);
    }

    /**
     * Method to setup the leader depot pane.
     * It loads the images of the depots and draws the resources.
     *
     * @return pane
     */
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

    /**
     * Method to show a message in the root pane.
     *
     * @param message message to display
     */
    public void showMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }

    /**
     * Method to hide the message that is displayed in the root pane.
     */
    public void hideMessage() {
        errorMessage.setVisible(false);
    }
}
