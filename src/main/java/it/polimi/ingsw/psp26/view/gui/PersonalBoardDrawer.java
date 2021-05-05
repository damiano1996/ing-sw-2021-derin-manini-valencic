package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseClickAnimation;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseOverAnimation;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

public class PersonalBoardDrawer {

    public static Pane drawPersonalBoard(PersonalBoard personalBoard, int canvasWidth) {
        Pane pane = new Pane();

        Image personalboardImage = loadImage("personalboard.png", canvasWidth);
        personalboardImage = setRoundedCorners(personalboardImage);
        personalboardImage = addLightEffects(personalboardImage);

        Canvas canvas = new Canvas(personalboardImage.getWidth(), personalboardImage.getHeight());
        pane.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(personalboardImage, 0, 0);

        drawWarehouseBaseDepots(pane, personalBoard.getWarehouse().getBaseDepots());
        drawStrongbox(pane, personalBoard.getStrongbox());
        drawDevelopmentCards(pane, personalBoard.getDevelopmentCardsSlots());
        drawFaithTrack(pane, personalBoard.getFaithTrack());

        return pane;
    }

    private static void drawWarehouseBaseDepots(Pane pane, List<Depot> baseDepots) {
        int[] hOffsets = new int[]{345, 275, 215};
        int vOffsetFirstDepot = 895;

        for (int i = 0; i < baseDepots.size(); i++) {
            Depot depot = baseDepots.get(i);
            Image resourceImage = loadImage("resources/" + depot.getContainedResourceType() + ".png", 100);
            resourceImage = addLightEffects(resourceImage);

            for (int j = 0; j < depot.getResources().size(); j++) {

                ImageView imageView = getImageView(resourceImage, hOffsets[i] + j * 100, vOffsetFirstDepot + i * 180);
                addMouseOverAnimation(imageView);
                pane.getChildren().add(imageView);

            }
        }
    }

    private static void drawStrongbox(Pane pane, List<Resource> resources) {
        int[] hOffsets = new int[]{150, 150, 400, 400};
        int[] vOffsets = new int[]{1750, 1600, 1750, 1600};
        int shift = 50;

        Random random = new Random();

        for (int i = 0; i < RESOURCES_SLOTS.length; i++) {
            Resource resource = RESOURCES_SLOTS[i];
            int multiplicity = Collections.frequency(resources, resource);

            Image resourceImage = loadImage("resources/" + resource + ".png", 100);
            resourceImage = addLightEffects(resourceImage);

            for (int j = 0; j < multiplicity; j++) {

                ImageView imageView = getImageView(
                        resourceImage,
                        hOffsets[i] + (shift - random.nextInt(2 * shift)),
                        vOffsets[i] + (shift - random.nextInt(2 * shift))
                );
                addMouseOverAnimation(imageView);
                pane.getChildren().add(imageView);
            }
        }
    }

    private static void drawDevelopmentCards(Pane pane, List<List<DevelopmentCard>> developmentCardSlots) {
        int[] hOffsets = new int[]{1120, 1665, 2240};
        int vOffset = 800;
        int shift = 200;

        for (int i = 0; i < developmentCardSlots.size(); i++) {
            for (int j = 0; j < developmentCardSlots.get(i).size(); j++) {

                Image developmentCardImage = loadImage("developmentcards/" + developmentCardSlots.get(i).get(j).toString() + ".png", 700);
                developmentCardImage = setRoundedCorners(developmentCardImage);
                developmentCardImage = addLightEffects(developmentCardImage);

                ImageView imageView = getImageView(developmentCardImage, hOffsets[i], vOffset + shift * j);
                addMouseOverAnimation(imageView);
                pane.getChildren().add(imageView);
            }
        }
    }

    private static void drawFaithTrack(Pane pane, FaithTrack faithTrack) {
        drawPopesFavorTiles(pane, faithTrack.getVaticanReportSections());
    }

    private static void drawPopesFavorTiles(Pane pane, VaticanReportSection[] vaticanReportSections) {
        int[] hOffsets = new int[]{705, 1410, 2250};
        int[] vOffsets = new int[]{295, 150, 295};
        String[] notActiveFiles = new String[]{"not_active_yellow", "not_active_orange", "not_active_red"};

        for (int i = 0; i < vaticanReportSections.length; i++) {

            if (vaticanReportSections[i].isPopesFavorTileActive()) {// if active
                // TODO: cut image
            } else if (!vaticanReportSections[i].isPopesFavorTileActive()) { // if not active
                Image tileImage = loadImage("faithtrack/" + notActiveFiles[i] + ".png", 200);
                tileImage = addLightEffects(tileImage);

                ImageView imageView = new ImageView();
                imageView.setImage(tileImage);
                imageView.setX(hOffsets[i]);
                imageView.setY(vOffsets[i]);

                pane.getChildren().add(imageView);
                addMouseOverAnimation(imageView);
                addMouseClickAnimation(imageView);

            } else {
                // TODO: if no more tile?
            }
        }
    }
}
