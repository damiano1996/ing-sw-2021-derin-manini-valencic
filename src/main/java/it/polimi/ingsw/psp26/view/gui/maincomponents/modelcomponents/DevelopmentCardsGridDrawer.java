package it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Random;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;

/**
 * Class to draw the development cards grid.
 */
public class DevelopmentCardsGridDrawer extends RatioDrawer {

    private final DevelopmentCardsGrid developmentCardsGrid;

    private Client client;

    /**
     * Class constructor.
     *
     * @param developmentCardsGrid development cards grid that must be drawn
     * @param maxWidth             max allowed width for the root pane
     */
    public DevelopmentCardsGridDrawer(DevelopmentCardsGrid developmentCardsGrid, int maxWidth) {
        super(maxWidth);

        this.developmentCardsGrid = developmentCardsGrid;
    }

    /**
     * Class constructor.
     *
     * @param client               client object
     * @param developmentCardsGrid development cards grid that must be drawn
     * @param maxWidth             max allowed width for the root pane
     */
    public DevelopmentCardsGridDrawer(Client client, DevelopmentCardsGrid developmentCardsGrid, int maxWidth) {
        super(maxWidth);

        this.developmentCardsGrid = developmentCardsGrid;
        this.client = client;
    }

    /**
     * Method to draw the grid of cards.
     * It returns a pane containing the grid.
     *
     * @return pane
     */
    @Override
    public Pane draw() {

        return drawGrid();
    }

    /**
     * Method to draw the matrix containing the development cards.
     * If client object is defined,
     * if cards are clicked, an event is triggered and the card will be sent to the server.
     *
     * @return pane containing th grid
     */
    private Pane drawGrid() {

        int cellShift = 5;

        Random random = new Random();

        GridPane gridPane = new GridPane();
        gridPane.setHgap((20 * cellShift) * ratio);
        gridPane.setVgap((20 * cellShift) * ratio);

        for (int row = 0; row < DevelopmentCardsGrid.LEVELS.length; row++) {
            for (int col = 0; col < DevelopmentCardsGrid.COLORS.length; col++) {

                Pane cell = new Pane();
                for (int i = 0; i < developmentCardsGrid.getDevelopmentGridCell(row, col).getDevelopmentCardsSize(); i++) { // drawing multiple time the first card to show that there are others below

                    Image developmentCardImage = ModelDrawUtils.getCard(developmentCardsGrid.getDevelopmentGridCell(row, col).getFirstCard(), ratio);

                    ImageView imageView = getImageView(developmentCardImage, 0, 0);
                    imageView.setRotate(cellShift - random.nextInt(2 * cellShift));

                    if (client != null && i == developmentCardsGrid.getDevelopmentGridCell(row, col).getDevelopmentCardsSize() - 1) {

                        int finalRow = row;
                        int finalCol = col;
                        imageView.setOnMouseClicked(mouseEvent -> {
                            try {
                                SoundManager.getInstance().setSoundEffect(SoundManager.RESOURCESOUND);

                                client.notifyObservers(
                                        new Message(
                                                MessageType.CHOICE_CARD_TO_BUY,
                                                developmentCardsGrid.getDevelopmentGridCell(finalRow, finalCol).getFirstCard()
                                        ));
                                closeParentStageOfActionEvent(mouseEvent);
                                client.viewNext();
                            } catch (InvalidPayloadException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    cell.getChildren().add(imageView);
                }

                gridPane.add(cell, col, row, 1, 1);
            }
        }

        return gridPane;
    }
}
