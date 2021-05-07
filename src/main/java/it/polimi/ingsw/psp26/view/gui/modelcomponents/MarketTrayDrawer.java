package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.gui.effects.Selectable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseOverAnimation;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

public class MarketTrayDrawer extends RatioDrawer {

    private final MarketTray marketTray;

    public MarketTrayDrawer(MarketTray marketTray, int maxWidth) {
        super(maxWidth);

        this.marketTray = marketTray;
    }

    @Override
    public Pane draw() {

        Image marketBoardImage = loadImage("market/market_board.png", (int) (WIDTH * ratio));
        marketBoardImage = addLightEffects(marketBoardImage, ratio);

        Canvas canvas = new Canvas(marketBoardImage.getWidth(), marketBoardImage.getHeight());
        pane.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(marketBoardImage, 0, 0);

        drawMarketTray();
        drawGridMarbles();
        drawSlideMarble();
        drawArrows();

        return pane;
    }

    private void drawMarketTray() {
        Image marketTrayImage = loadImage("market/market_tray.png", (int) (1550 * ratio));
        marketTrayImage = setRoundedCorners(marketTrayImage, ratio);
        marketTrayImage = addLightEffects(marketTrayImage, ratio);

        ImageView imageView = getImageView(marketTrayImage, 330 * ratio, 330 * ratio);

        pane.getChildren().add(imageView);
    }

    private void drawSlideMarble() {
        Image marbleImage = loadImage("resources/" + marketTray.getMarbleOnSlide().getColor().getName() + "_MARBLE.png", (int) (250 * ratio));
        marbleImage = addLightEffects(marbleImage, ratio);

        ImageView imageView = getImageView(marbleImage, 1550 * ratio, 400 * ratio);
        addMouseOverAnimation(imageView, ratio);

        pane.getChildren().add(imageView);
    }

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

    private void drawArrows() {
        drawHorizontalArrows();
        drawVerticalArrows();
    }

    private void drawHorizontalArrows() {
        int hOffset = 2040;
        int vOffset = 530;
        int shift = 260;

        for (int row = 0; row < marketTray.getMarblesOnColumn(0).length; row++) {
            ImageView imageView = getImageView(getArrowImage(), hOffset * ratio, (vOffset + row * shift) * ratio);
            imageView.setRotate(-90);
            addMouseOverAnimation(imageView, ratio);
            new Selectable(imageView);

            pane.getChildren().add(imageView);
        }
    }

    private void drawVerticalArrows() {
        int hOffset = 650;
        int vOffset = 1680;
        int shift = 255;

        for (int col = 0; col < marketTray.getMarblesOnRow(0).length; col++) {
            ImageView imageView = getImageView(getArrowImage(), (hOffset + col * shift) * ratio, vOffset * ratio);
            addMouseOverAnimation(imageView, ratio);
            new Selectable(imageView);

            pane.getChildren().add(imageView);
        }
    }

    private Image getArrowImage() {
        Image arrowImage = loadImage("market/arrow.png", (int) (450 * ratio));
        arrowImage = addLightEffects(arrowImage, ratio);
        return arrowImage;
    }
}
