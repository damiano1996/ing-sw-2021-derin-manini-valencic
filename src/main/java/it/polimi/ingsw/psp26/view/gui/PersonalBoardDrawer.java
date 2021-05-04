package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.loadImage;

public class PersonalBoardDrawer {

    public static GridPane drawPersonalBoard(PersonalBoard personalBoard, int canvasWidth) {
        GridPane gridPane = new GridPane();

        gridPane.setOnMouseClicked(event -> {
            System.out.println("PersonalBoard - Position: " + event.getSceneX() + ":" + event.getSceneY());
        });

        Image personalboardImage = loadImage("personalboard.png", canvasWidth);

        Canvas canvas = new Canvas(personalboardImage.getWidth(), personalboardImage.getHeight());
        gridPane.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(personalboardImage, 0, 0);

        gridPane.setStyle("-fx-background-color: #6989dc;");

        drawWarehouseBaseDepots(graphicsContext, personalBoard.getWarehouse().getBaseDepots());
        drawStrongbox(graphicsContext, personalBoard.getStrongbox());
        drawDevelopmentCards(graphicsContext, personalBoard.getDevelopmentCardsSlots());

        return gridPane;
    }

    private static void drawWarehouseBaseDepots(GraphicsContext graphicsContext, List<Depot> baseDepots) {
        int[] hOffsets = new int[]{350, 285, 220};
        int vOffsetFirstDepot = 900;

        for (int i = 0; i < baseDepots.size(); i++) {
            Depot depot = baseDepots.get(i);
            Image resourceImage = loadImage("resources/" + depot.getContainedResourceType() + ".png", 100);

            for (int j = 0; j < depot.getResources().size(); j++) {
                graphicsContext.drawImage(resourceImage, hOffsets[i] + j * 100, vOffsetFirstDepot + i * 180);
            }
        }
    }

    private static void drawStrongbox(GraphicsContext graphicsContext, List<Resource> resources) {
        int[] hOffsets = new int[]{150, 150, 400, 400};
        int[] vOffsets = new int[]{1750, 1600, 1750, 1600};
        int shift = 50;

        Random random = new Random();

        for (int i = 0; i < RESOURCES_SLOTS.length; i++) {
            Resource resource = RESOURCES_SLOTS[i];
            int multiplicity = Collections.frequency(resources, resource);

            Image resourceImage = loadImage("resources/" + resource + ".png", 100);

            for (int j = 0; j < multiplicity; j++) {
                graphicsContext.drawImage(
                        resourceImage,
                        hOffsets[i] + (shift - random.nextInt(2 * shift)),
                        vOffsets[i] + (shift - random.nextInt(2 * shift))
                );
            }
        }
    }

    private static void drawDevelopmentCards(GraphicsContext graphicsContext, List<List<DevelopmentCard>> developmentCardSlots) {
        int[] hOffsets = new int[]{1120, 1665, 2240};
        int vOffset = 800;
        int shift = 100;

        for (int i = 0; i < developmentCardSlots.size(); i++) {
            for (int j = 0; j < developmentCardSlots.get(i).size(); j++) {

                Image developmentCardImage = loadImage("developmentcards/" + developmentCardSlots.get(i).get(j).toString() + ".png", 700);
                graphicsContext.drawImage(developmentCardImage, hOffsets[i] - shift * 0.2 * j, vOffset + shift * j);
            }
        }
    }
}
