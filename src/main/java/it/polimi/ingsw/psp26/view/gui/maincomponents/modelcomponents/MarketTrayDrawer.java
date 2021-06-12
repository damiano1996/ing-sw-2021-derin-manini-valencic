package it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseOverAnimation;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

/**
 * Class to draw the market tray.
 */
public class MarketTrayDrawer extends RatioDrawer {

    private final MarketTray marketTray;

    private Client client;

    /**
     * Class constructor.
     *
     * @param marketTray market tray that must be drawn
     * @param maxWidth   max width for the root pane
     */
    public MarketTrayDrawer(MarketTray marketTray, int maxWidth) {
        super(maxWidth);
        this.marketTray = marketTray;
    }

    /**
     * Class constructor.
     *
     * @param client     client object
     * @param marketTray market tray that must be drawn
     * @param maxWidth   max width for the root pane
     */
    public MarketTrayDrawer(Client client, MarketTray marketTray, int maxWidth) {
        super(maxWidth);
        this.client = client;
        this.marketTray = marketTray;
    }

    /**
     * Method to draw the market.
     * It returns tha pane containing it.
     *
     * @return pane
     */
    @Override
    public Pane draw() {

        Image marketBoardImage = loadImage("market/market_board.png", initMaxWidth);
        marketBoardImage = addLightEffects(marketBoardImage, ratio);
        ImageView imageView = getImageView(marketBoardImage, 0, 0);
        pane.getChildren().add(imageView);

        drawMarketTray();
        drawGridMarbles();
        drawSlideMarble();
        drawArrows();

        return pane;
    }

    /**
     * Method to load the image of the market tray and to assign it to an image view
     * that will be added to the root pane.
     */
    private void drawMarketTray() {
        Image marketTrayImage = loadImage("market/market_tray.png", (int) (1550 * ratio));
        marketTrayImage = setRoundedCorners(marketTrayImage, ratio);
        marketTrayImage = addLightEffects(marketTrayImage, ratio);

        ImageView imageView = getImageView(marketTrayImage, 330 * ratio, 330 * ratio);

        pane.getChildren().add(imageView);
    }

    /**
     * Method to draw the marble on the slide.
     */
    private void drawSlideMarble() {
        Image marbleImage = loadImage("resources/" + marketTray.getMarbleOnSlide().getColor().getName() + "_MARBLE.png", (int) (250 * ratio));
        marbleImage = addLightEffects(marbleImage, ratio);

        ImageView imageView = getImageView(marbleImage, 1550 * ratio, 400 * ratio);
        addMouseOverAnimation(imageView, ratio);

        pane.getChildren().add(imageView);
    }

    /**
     * Method to draw the grid of marbles.
     */
    private void drawGridMarbles() {
        int hOffset = 650;
        int vOffset = 650;
        int shift = 250;

        for (int row = 0; row < marketTray.getMarblesOnColumn(0).length; row++) { // number of rows
            for (int col = 0; col < marketTray.getMarblesOnRow(0).length; col++) { // number of columns
                Resource resource = marketTray.getMarblesOnRow(row)[col];
                // drawing the row
                Image marbleImage = loadImage("resources/" + resource.getColor().getName() + "_MARBLE.png", (int) (250 * ratio));
                marbleImage = addLightEffects(marbleImage, ratio);

                ImageView imageView = getImageView(marbleImage, (hOffset + col * shift) * ratio, (vOffset + row * shift) * ratio);
                addMouseOverAnimation(imageView, ratio);

                pane.getChildren().add(imageView);
            }
        }
    }

    /**
     * Method that draws on the root pane the arrows targeting th rows and the columns of the market.
     */
    private void drawArrows() {
        drawHorizontalArrows();
        drawVerticalArrows();
    }

    /**
     * Method to draw the horizontal arrows.
     */
    private void drawHorizontalArrows() {
        int hOffset = 2040;
        int vOffset = 530;
        int shift = 260;

        for (int row = 0; row < marketTray.getMarblesOnColumn(0).length; row++) {
            ImageView imageView = getImageView(getArrowImage(), hOffset * ratio, (vOffset + row * shift) * ratio);
            imageView.setRotate(-90);
            addMouseOverAnimation(imageView, ratio);
            addArrowClickEvent(imageView, row);

            pane.getChildren().add(imageView);
        }
    }

    /**
     * Method to draw the vertical arrows.
     */
    private void drawVerticalArrows() {
        int hOffset = 650;
        int vOffset = 1680;
        int shift = 255;

        for (int col = 0; col < marketTray.getMarblesOnRow(0).length; col++) {
            ImageView imageView = getImageView(getArrowImage(), (hOffset + col * shift) * ratio, vOffset * ratio);
            addMouseOverAnimation(imageView, ratio);
            addArrowClickEvent(imageView, col + 3);

            pane.getChildren().add(imageView);
        }
    }

    /**
     * Getter of the arrow image.
     *
     * @return an image with the arrow
     */
    private Image getArrowImage() {
        Image arrowImage = loadImage("market/arrow.png", (int) (450 * ratio));
        arrowImage = addLightEffects(arrowImage, ratio);
        return arrowImage;
    }

    /**
     * Method to add the arrow click event. (Only if client has been defined)
     * If an arrow will be clicked, the event will send to the server the corresponding index of the arrow.
     * To choose the resources of the corresponding row or column
     *
     * @param arrowImageView image view of the arrow
     * @param numToSend      index associated to the arrow
     */
    private void addArrowClickEvent(ImageView arrowImageView, int numToSend) {
        if (client != null) {
            arrowImageView.setOnMouseClicked(mouseEvent -> {
                try {
                    SoundManager.getInstance().setSoundEffect("button_click_02.wav");

                    client.notifyObservers(new Message(MessageType.CHOICE_ROW_COLUMN, numToSend));
                    closeParentStageOfActionEvent(mouseEvent);
                    client.viewNext();
                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
