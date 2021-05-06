package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Random;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseOverAnimation;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getImage;

public class DevelopmentCardGridDrawer extends RatioDrawer {

    private final DevelopmentGrid developmentGrid;

    public DevelopmentCardGridDrawer(DevelopmentGrid developmentGrid, int maxWidth) {
        super(maxWidth);

        this.developmentGrid = developmentGrid;
    }

    @Override
    public Pane draw() {

        // TODO: drawing a cool background
//        Image marketBoardImage = loadImage("market/market_board.png", (int) (WIDTH * ratio));
//        marketBoardImage = addLightEffects(marketBoardImage, ratio);
//
//        Canvas canvas = new Canvas(marketBoardImage.getWidth(), marketBoardImage.getHeight());
//        pane.getChildren().add(canvas);
//
//        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
//        graphicsContext.drawImage(marketBoardImage, 0, 0);

        drawGrid();

        return pane;
    }

    private void drawGrid() {

        int hOffset = 0;
        int vOffset = 0;
        int hShift = 550;
        int vShift = 850;
        int cellShift = 5;

        Random random = new Random();

        for (int row = 0; row < DevelopmentGrid.LEVELS.length; row++) {
            for (int col = 0; col < DevelopmentGrid.COLORS.length; col++) {

                for (int i = 0; i < developmentGrid.getDevelopmentGridCell(row, col).getDevelopmentCardsSize(); i++) { // drawing multiple time the first card to show that there are others below

                    Image developmentCardImage = getImage(developmentGrid.getDevelopmentGridCell(row, col).getFirstCard(), ratio);

                    ImageView imageView = getImageView(developmentCardImage, (hOffset + col * hShift) * ratio, (vOffset + row * vShift) * ratio);
                    imageView.setRotate(cellShift - random.nextInt(2 * cellShift));

                    addMouseOverAnimation(imageView, ratio);
                    pane.getChildren().add(imageView);

                }
            }
        }
    }
}
