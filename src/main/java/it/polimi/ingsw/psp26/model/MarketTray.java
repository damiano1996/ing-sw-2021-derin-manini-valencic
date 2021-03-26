package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MarketTray {

    private final Resource[][] marketMarbles;
    private Resource marbleOnSlide;

    public MarketTray() {
        List<Resource> MarblesList = new ArrayList<>(Arrays.asList(Resource.EMPTY, Resource.EMPTY, Resource.EMPTY, Resource.EMPTY,
                Resource.COIN, Resource.COIN, Resource.SERVANT, Resource.SERVANT, Resource.SHIELD, Resource.SHIELD, Resource.STONE,
                Resource.STONE, Resource.FAITH_MARKERS));
        Collections.shuffle(MarblesList);

        marketMarbles = new Resource[3][4];
        marbleOnSlide = MarblesList.get(MarblesList.size() - 1);
        for (int i = 0; i < marketMarbles[0].length; i++) {
            for (int j = 0; j < marketMarbles.length; j++) {
                marketMarbles[j][i] = MarblesList.get(j + i * marketMarbles.length);
            }
        }
    }


    public Resource[] getMarblesOnRow(int row) {
        return marketMarbles[row];
    }


    public Resource[] getMarbleOnColumn(int column) {
        Resource[] marketColumn = new Resource[marketMarbles.length];
        for (int i = 0; i < marketMarbles.length; i++) {
            marketColumn[i] = marketMarbles[i][column];
        }
        return marketColumn;
    }


    public Resource getMarbleOnSlide() {
        return marbleOnSlide;
    }


    public void pushMarbleFromSlideToRow(int row) {

        Resource marbleTemp = marbleOnSlide;
        marbleOnSlide = marketMarbles[row][marketMarbles[0].length - 1];
        for (int i = 0; i < marketMarbles[0].length - 1; i++) {
            marketMarbles[row][marketMarbles[0].length - i - 1] = marketMarbles[row][marketMarbles[0].length - i - 2];
        }
        marketMarbles[row][0] = marbleTemp;
    }


    public void pushMarbleFromSlideToColumn(int column) {
        Resource marbleTemp = marbleOnSlide;
        marbleOnSlide = marketMarbles[marketMarbles.length - 1][column];
        for (int i = 0; i < marketMarbles.length - 1; i++)
            marketMarbles[marketMarbles.length - i - 1][column] = marketMarbles[marketMarbles.length - i - 2][column];
        marketMarbles[0][column] = marbleTemp;
    }
}
