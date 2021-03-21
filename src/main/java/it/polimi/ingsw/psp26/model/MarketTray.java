package it.polimi.ingsw.psp26.model;

public class MarketTray {

    private Resource[][] marketMarbles;
    private Resource marbleOnSlide;


    public MarketTray() {

    }


    public void shuffle() {


    }


    public Resource[] getMarblesOnRow(int row) {
        return marketMarbles[row];
    }


    public Resource[] getMarbleOnColumn(int column) {
        Resource[] marketColumn = new Resource[3];
        for (int i = 0; i < 3; i++) {
            marketColumn[i] = marketMarbles[i][column]; //maybe a single marble getter is required
        }
        return marketColumn;

    }


    public Resource getMarbleOnSlide() {
        return marbleOnSlide;
    }


    public void pushMarbleFromSlideToRow(int row) {

    }


    public void pushMarbleFromSlideToColumn(int column) {

    }
}
