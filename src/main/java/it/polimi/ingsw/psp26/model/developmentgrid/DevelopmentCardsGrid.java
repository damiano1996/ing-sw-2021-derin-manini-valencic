package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.psp26.network.server.MessageUtils.getDevelopmentGridModelUpdateMessage;

/**
 * Class representing the DevelopmentCardsGrid.
 * By the rules, it has 4 columns and 3 rows.
 * It contains 12 DevelopmentGridCells, each containing 4 DevelopmentCards.
 */
public class DevelopmentCardsGrid extends Observable<SessionMessage> {

    public static final Color[] COLORS = new Color[]{Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE};
    public static final Level[] LEVELS = new Level[]{Level.THIRD, Level.SECOND, Level.FIRST};

    private final DevelopmentGridCell[][] grid;

    /**
     * Constructor of the class.
     * It adds a VirtualView to the Observers List and creates a new DevelopmentCardsGrid.
     *
     * @param virtualView the VirtualView to add to the Observers List
     */
    public DevelopmentCardsGrid(VirtualView virtualView) {
        super();
        addObserver(virtualView);

        grid = new DevelopmentGridCell[LEVELS.length][COLORS.length];

        initializeGrid();
    }


    /**
     * Used when recovering a Match.
     * It resets the List of Observers and adds the new VirtualView passed as a parameter.
     *
     * @param virtualView the new VirtualView to add to the Observers List
     */
    public void restoreVirtualView(VirtualView virtualView) {
        resetObservers();
        addObserver(virtualView);
    }


    /**
     * This method creates a new DevelopmentCardsGrid by iterating over Colors and Levels.
     * For each Color and Level, creates a new Cell of the DevelopmentCardsGrid.
     */
    private void initializeGrid() {
        for (int row = 0; row < LEVELS.length; row++) {
            for (int col = 0; col < COLORS.length; col++) {
                grid[row][col] = new DevelopmentGridCell(new DevelopmentCardType(COLORS[col], LEVELS[row]));
            }
        }
    }


    /**
     * Returns the row of the DevelopmentCardsGrid of the desired Level.
     *
     * @param level the desired Level of the DevelopmentCardsGrid
     * @return the selected row of the DevelopmentCardsGrid
     * @throws LevelDoesNotExistException thrown if the selected Level is not present in the DevelopmentCardsGrid
     */
    private int getRow(Level level) throws LevelDoesNotExistException {
        for (int row = 0; row < grid.length; row++)
            if (grid[row][0].getDevelopmentCardType().getLevel().equals(level))
                return row;
        throw new LevelDoesNotExistException();
    }


    /**
     * Returns the column of the DevelopmentCardsGrid of the desired Color.
     *
     * @param color the desired Color of the DevelopmentCardsGrid
     * @return the selected column of the DevelopmentCardsGrid
     * @throws ColorDoesNotExistException thrown if the selected Color is not present in the DevelopmentCardsGrid
     */
    private int getColumn(Color color) throws ColorDoesNotExistException {
        for (int col = 0; col < grid[0].length; col++)
            if (grid[0][col].getDevelopmentCardType().getColor().equals(color))
                return col;
        throw new ColorDoesNotExistException();
    }


    /**
     * Draws a DevelopmentCard from the DevelopmentCardsGrid by removing it from the DevelopmentCardsGrid and returning it.
     *
     * @param color the Color of the desired DevelopmentCard
     * @param level the Level of the desired DevelopmentCard
     * @return the desired DevelopmentCard
     * @throws LevelDoesNotExistException      the Level selected is not present in the DevelopmentCardsGrid
     * @throws ColorDoesNotExistException      the Color selected is not present in the DevelopmentCardsGrid
     * @throws NoMoreDevelopmentCardsException there are no more Development Cards in the DevelopmentCardsGrid
     */
    public DevelopmentCard drawCard(Color color, Level level) throws LevelDoesNotExistException, ColorDoesNotExistException, NoMoreDevelopmentCardsException {
        int row = getRow(level);
        int col = getColumn(color);

        DevelopmentCard developmentCard = grid[row][col].drawCard();

        notifyObservers(getDevelopmentGridModelUpdateMessage());

        return developmentCard;
    }


    /**
     * Checks if a DevelopmentCard is present in the DevelopmentCardsGrid.
     *
     * @param color the Color of the desired DevelopmentCard
     * @param level the Level of the desired DevelopmentCard
     * @return true if the DevelopmentCard of specified Level and Color is present in the DevelopmentCardsGrid, false otherwise
     * @throws LevelDoesNotExistException the Level selected is not present in the DevelopmentCardsGrid
     * @throws ColorDoesNotExistException the Color selected is not present in the DevelopmentCardsGrid
     */
    public boolean isAvailable(Color color, Level level) throws LevelDoesNotExistException, ColorDoesNotExistException {
        int row = getRow(level);
        int col = getColumn(color);
        return !grid[row][col].isEmpty();
    }


    /**
     * By iterating over the entire DevelopmentCardsGrid, returns a List containing all the visible DevelopmentCards.
     * The visible DevelopmentCards are the one on the top of every DevelopmentGridCell.
     * If a DevelopmentGridCell is empty, nothing is added to the List to return.
     *
     * @return an unmodifiable List containing all the visible Cards of the DevelopmentCardsGrid
     */
    public List<DevelopmentCard> getAllVisibleCards() {
        List<DevelopmentCard> visibleCards = new ArrayList<>();
        for (int row = 0; row < LEVELS.length; row++) {
            for (int col = 0; col < COLORS.length; col++) {
                if (!grid[row][col].isEmpty()) visibleCards.add(getDevelopmentGridCell(row, col).getFirstCard());
            }
        }
        return Collections.unmodifiableList(visibleCards);
    }


    /**
     * Retrieves and returns a DevelopmentGridCell (one element of the bi-dimensional grid array).
     *
     * @param row the desired row of the DevelopmentGrid
     * @param col the desired column of the DevelopmentGrid
     * @return the corresponding DevelopmentGridCell
     */
    public DevelopmentGridCell getDevelopmentGridCell(int row, int col) {
        return grid[row][col];
    }
}
