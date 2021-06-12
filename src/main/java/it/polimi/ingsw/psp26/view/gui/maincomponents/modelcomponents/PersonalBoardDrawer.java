package it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseOverAnimation;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.getResourceImage;

/**
 * Class to draw the personal board.
 */
public class PersonalBoardDrawer extends RatioDrawer {

    private final PersonalBoard personalBoard;
    private final boolean hasInkwell;
    private final boolean hideBlackCross;

    /**
     * Class constructor.
     *
     * @param personalBoard  personal board to draw
     * @param maxWidth       max width for the root pane
     * @param hasInkwell     boolean to draw or not the inkwell
     * @param hideBlackCross boolean to draw or not the black cross
     */
    public PersonalBoardDrawer(PersonalBoard personalBoard, int maxWidth, boolean hasInkwell, boolean hideBlackCross) {
        super(maxWidth);

        this.personalBoard = personalBoard;
        this.hasInkwell = hasInkwell;
        this.hideBlackCross = hideBlackCross;
    }

    /**
     * Method to draw the personal board.
     *
     * @return pane containing the personal board
     */
    @Override
    public Pane draw() {

        Image personalboardImage = loadImage("personalboard.png", initMaxWidth);
        personalboardImage = setRoundedCorners(personalboardImage, ratio);
        personalboardImage = addLightEffects(personalboardImage, ratio);
        ImageView imageView = getImageView(personalboardImage, 0, 0);
        pane.getChildren().add(imageView);

        drawInkwell();
        drawWarehouseBaseDepots();
        drawStrongbox();
        drawDevelopmentCards();
        drawFaithTrack();

        return pane;
    }

    /**
     * Method to draw the inkwell over the board.
     */
    private void drawInkwell() {
        int hOffset = 50;
        int vOffset = 50;

        if (hasInkwell) {

            Image imageInkwell = loadImage("inkwell.png", (int) (300 * ratio));
            imageInkwell = addLightEffects(imageInkwell, ratio);
            ImageView imageView = getImageView(imageInkwell, hOffset * ratio, vOffset * ratio);
            addMouseOverAnimation(imageView, ratio);
            pane.getChildren().add(imageView);
        }
    }

    /**
     * Method to draw the resources in the warehouse.
     */
    private void drawWarehouseBaseDepots() {
        int[] hOffsets = new int[]{345, 275, 215};
        int vOffsetFirstDepot = 895;

        List<Depot> baseDepots = personalBoard.getWarehouse().getBaseDepots();
        for (int i = 0; i < baseDepots.size(); i++) {
            Depot depot = baseDepots.get(i);

            Image resourceImage = getResourceImage(depot.getContainedResourceType(), ratio);

            for (int j = 0; j < depot.getResources().size(); j++) {

                ImageView imageView = getImageView(resourceImage, (hOffsets[i] + j * 100) * ratio, (vOffsetFirstDepot + i * 180) * ratio);
                addMouseOverAnimation(imageView, ratio);

                pane.getChildren().add(imageView);

            }
        }
    }

    /**
     * Method to draw the resources in the strongbox.
     */
    private void drawStrongbox() {
        int[] hOffsets = new int[]{150, 150, 400, 400};
        int[] vOffsets = new int[]{1750, 1600, 1750, 1600};
        int shift = 50;

        Random random = new Random();

        List<Resource> resources = personalBoard.getStrongbox();
        for (int i = 0; i < RESOURCES_SLOTS.length; i++) {
            Resource resource = RESOURCES_SLOTS[i];
            int multiplicity = Collections.frequency(resources, resource);

            Image resourceImage = getResourceImage(resource, ratio);

            for (int j = 0; j < multiplicity; j++) {

                ImageView imageView = getImageView(
                        resourceImage,
                        (hOffsets[i] + (shift - random.nextInt(2 * shift))) * ratio,
                        (vOffsets[i] + (shift - random.nextInt(2 * shift))) * ratio
                );
                addMouseOverAnimation(imageView, ratio);
                pane.getChildren().add(imageView);
            }
        }
    }

    /**
     * Method to draw the development cards in the corresponding slots.
     */
    private void drawDevelopmentCards() {
        int[] hOffsets = new int[]{1120, 1665, 2240};
        int vOffset = 800;
        int shift = 200;

        List<List<DevelopmentCard>> developmentCardSlots = personalBoard.getDevelopmentCardsSlots();
        for (int i = 0; i < developmentCardSlots.size(); i++) {
            for (int j = 0; j < developmentCardSlots.get(i).size(); j++) {

                Image developmentCardImage = ModelDrawUtils.getCard(developmentCardSlots.get(i).get(j), ratio);

                ImageView imageView = getImageView(developmentCardImage, hOffsets[i] * ratio, (vOffset + shift * j) * ratio);
                addMouseOverAnimation(imageView, ratio);
                pane.getChildren().add(imageView);
            }
        }
    }

    /**
     * Method to draw the faith track.
     * It draws the pope's favor tiles, the markers and the black cross.
     */
    private void drawFaithTrack() {
        drawPopesFavorTiles();
        drawFaithMarker();
        if (!hideBlackCross) drawBlackCross();
    }

    /**
     * Method to draw the faith marker.
     */
    private void drawFaithMarker() {
        drawMarker("resources/FAITH_MARKER.png", personalBoard.getFaithTrack().getMarkerPosition());
    }

    /**
     * Method to draw the black cross.
     */
    private void drawBlackCross() {
        drawMarker("faith_track/BLACK_CROSS.png", personalBoard.getFaithTrack().getBlackCrossPosition());
    }

    /**
     * Method to draw an image in a given position of the faith track.
     *
     * @param fileName file name of the image that must be drawn in the given position
     * @param position position of the faith track
     */
    private void drawMarker(String fileName, int position) {
        int[] hOffsets = new int[]{120, 260, 400, 400, 400, 540, 680, 820, 960, 1100, 1100, 1100, 1240, 1380, 1520, 1660, 1800, 1800, 1800, 1940, 2080, 2220, 2360, 2500, 2640};
        int[] vOffsets = new int[]{415, 415, 415, 275, 135, 135, 135, 135, 135, 135, 275, 415, 415, 415, 415, 415, 415, 275, 135, 135, 135, 135, 135, 135, 135};

        Image markerImage = loadImage(fileName, (int) (100 * ratio));
        markerImage = addLightEffects(markerImage, ratio);

        ImageView imageView = getImageView(markerImage,
                hOffsets[position] * ratio,
                vOffsets[position] * ratio
        );
        addMouseOverAnimation(imageView, ratio);

        pane.getChildren().add(imageView);
    }

    /**
     * Method to draw the pope's favor tiles.
     * They can be active, non-active and they can be discarded.
     */
    private void drawPopesFavorTiles() {
        int[] hOffsets = new int[]{705, 1410, 2250};
        int[] vOffsets = new int[]{295, 150, 295};
        String[] notActiveFiles = new String[]{"yellow", "orange", "red"};

        VaticanReportSection[] vaticanReportSections = personalBoard.getFaithTrack().getVaticanReportSections();
        for (int i = 0; i < vaticanReportSections.length; i++) {

            if (vaticanReportSections[i].isPopesFavorTileActive() && !vaticanReportSections[i].isPopesFavorTileDiscarded()) { // if active
                Image tileImage = loadImage("faith_track/active_" + notActiveFiles[i] + ".png", (int) (200 * ratio));
                tileImage = setRoundedCorners(tileImage, ratio);
                tileImage = addLightEffects(tileImage, ratio);

                ImageView imageView = getImageView(tileImage, hOffsets[i] * ratio, vOffsets[i] * ratio);
                addMouseOverAnimation(imageView, ratio);

                pane.getChildren().add(imageView);

            } else if (!vaticanReportSections[i].isPopesFavorTileActive() && !vaticanReportSections[i].isPopesFavorTileDiscarded()) { // if not active
                Image tileImage = loadImage("faith_track/not_active_" + notActiveFiles[i] + ".png", (int) (200 * ratio));
                tileImage = addLightEffects(tileImage, ratio);

                ImageView imageView = getImageView(tileImage, hOffsets[i] * ratio, vOffsets[i] * ratio);
                addMouseOverAnimation(imageView, ratio);

                pane.getChildren().add(imageView);
            }
        }
    }
}
