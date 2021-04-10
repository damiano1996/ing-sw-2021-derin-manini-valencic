package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class to model the market tray.
 */
public class MarketTray extends Observable<Message> {

    private final Resource[][] marketMarbles;
    private Resource marbleOnSlide;

    /**
     * Constructor of the class.
     * It initializes the marbles (with shuffling) on the market.
     *
     * @param virtualView virtual view that must be notified
     */
    public MarketTray(VirtualView virtualView) {
        super();
        addObserver(virtualView);

        List<Resource> MarblesList = new ArrayList<>(Arrays.asList(Resource.EMPTY, Resource.EMPTY, Resource.EMPTY, Resource.EMPTY,
                Resource.COIN, Resource.COIN, Resource.SERVANT, Resource.SERVANT, Resource.SHIELD, Resource.SHIELD, Resource.STONE,
                Resource.STONE, Resource.FAITH_MARKER));
        Collections.shuffle(MarblesList);

        marketMarbles = new Resource[3][4];
        marbleOnSlide = MarblesList.get(MarblesList.size() - 1);
        for (int i = 0; i < marketMarbles[0].length; i++) {
            for (int j = 0; j < marketMarbles.length; j++) {
                marketMarbles[j][i] = MarblesList.get(j + i * marketMarbles.length);
            }
        }

        notifyObservers(new Message()); // TODO: to be completed
    }


    /**
     * Getter of the marbles on the requested row.
     *
     * @param row row of the market
     * @return array containing the resources associated to the marbles on the requested row
     */
    public Resource[] getMarblesOnRow(int row) {
        return Arrays.copyOf(marketMarbles[row], marketMarbles[row].length);
    }


    /**
     * Getter of the marbles on the requested column.
     *
     * @param column column of the market
     * @return array containing the resources associated to marbles on the requested column
     */
    public Resource[] getMarblesOnColumn(int column) {
        Resource[] marketColumn = new Resource[marketMarbles.length];
        for (int i = 0; i < marketMarbles.length; i++) {
            marketColumn[i] = marketMarbles[i][column];
        }
        return Arrays.copyOf(marketColumn, marketColumn.length);
    }


    /**
     * Getter of the marble on the slide.
     *
     * @return resource located on the slide
     */
    public Resource getMarbleOnSlide() {
        return marbleOnSlide;
    }


    /**
     * Method to push the marble from the slide to the specified row.
     *
     * @param row row of the market
     */
    public void pushMarbleFromSlideToRow(int row) {

        Resource marbleTemp = marbleOnSlide;
        marbleOnSlide = marketMarbles[row][marketMarbles[0].length - 1];
        for (int i = 0; i < marketMarbles[0].length - 1; i++) {
            marketMarbles[row][marketMarbles[0].length - i - 1] = marketMarbles[row][marketMarbles[0].length - i - 2];
        }
        marketMarbles[row][0] = marbleTemp;

        notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * Method to push the marble fro the slide to the specified column.
     *
     * @param column column of the market
     */
    public void pushMarbleFromSlideToColumn(int column) {
        Resource marbleTemp = marbleOnSlide;
        marbleOnSlide = marketMarbles[marketMarbles.length - 1][column];
        for (int i = 0; i < marketMarbles.length - 1; i++)
            marketMarbles[marketMarbles.length - i - 1][column] = marketMarbles[marketMarbles.length - i - 2][column];
        marketMarbles[0][column] = marbleTemp;

        notifyObservers(new Message()); // TODO: to be completed
    }
}
