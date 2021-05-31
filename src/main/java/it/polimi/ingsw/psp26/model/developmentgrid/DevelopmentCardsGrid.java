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

public class DevelopmentCardsGrid extends Observable<SessionMessage> {

    public static final Color[] COLORS = new Color[]{Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE};
    public static final Level[] LEVELS = new Level[]{Level.THIRD, Level.SECOND, Level.FIRST};

    private final DevelopmentGridCell[][] grid;

    public DevelopmentCardsGrid(VirtualView virtualView) {
        super();
        addObserver(virtualView);

        grid = new DevelopmentGridCell[LEVELS.length][COLORS.length];

        initializeGrid();
    }


    /**
     * Used when recovering a Match
     * It resets the List of Observers and adds the new VirtualView passed as a parameter
     *
     * @param virtualView The new VirtualView to add to the Observers List
     */
    public void restoreVirtualView(VirtualView virtualView) {
        resetObservers();
        addObserver(virtualView);
    }


    /**
     * This method creates a new Development Cards Grid by iterating over Colors and Levels
     * For each Color and Level, creates a new Cell of the Grid
     */
    private void initializeGrid() {
        for (int row = 0; row < LEVELS.length; row++) {
            for (int col = 0; col < COLORS.length; col++) {
                grid[row][col] = new DevelopmentGridCell(new DevelopmentCardType(COLORS[col], LEVELS[row]));
            }
        }
    }


    /**
     * Returns the row of the Grid of the desired Level
     *
     * @param level The desired Level of the Grid
     * @return The selected row of the Grid
     * @throws LevelDoesNotExistException Thrown if the selected Level is not present in the Grid
     */
    private int getRow(Level level) throws LevelDoesNotExistException {
        for (int row = 0; row < grid.length; row++)
            if (grid[row][0].getDevelopmentCardType().getLevel().equals(level))
                return row;
        throw new LevelDoesNotExistException();
    }


    /**
     * Returns the column of the Grid of the desired Color
     *
     * @param color The desired Color of the Grid
     * @return The selected column of the Grid
     * @throws ColorDoesNotExistException Thrown if the selected Color is not present in the Grid
     */
    private int getColumn(Color color) throws ColorDoesNotExistException {
        for (int col = 0; col < grid[0].length; col++)
            if (grid[0][col].getDevelopmentCardType().getColor().equals(color))
                return col;
        throw new ColorDoesNotExistException();
    }


    /**
     * Draws a Card from the Grid by removing it from the Grid and returning it
     *
     * @param color The Color of the desired Card
     * @param level The Level of the desired Card
     * @return The desired Development Card
     * @throws LevelDoesNotExistException      The Level selected is not present in the Grid
     * @throws ColorDoesNotExistException      The Color selected is not present in the Grid
     * @throws NoMoreDevelopmentCardsException There are no more Development Cards in the Grid
     */
    public DevelopmentCard drawCard(Color color, Level level) throws LevelDoesNotExistException, ColorDoesNotExistException, NoMoreDevelopmentCardsException {
        int row = getRow(level);
        int col = getColumn(color);

        DevelopmentCard developmentCard = grid[row][col].drawCard();

        notifyObservers(getDevelopmentGridModelUpdateMessage());

        return developmentCard;
    }


    /**
     * Checks if a Development Card is present in the Grid
     *
     * @param color The Color of the desired Card
     * @param level The Level of the desired Card
     * @return True if the Card of specified Level and Color is present in the Grid, false otherwise
     * @throws LevelDoesNotExistException The Level selected is not present in the Grid
     * @throws ColorDoesNotExistException The Color selected is not present in the Grid
     */
    public boolean isAvailable(Color color, Level level) throws LevelDoesNotExistException, ColorDoesNotExistException {
        int row = getRow(level);
        int col = getColumn(color);
        return !grid[row][col].isEmpty();
    }


    /**
     * By iterating over the entire Grid, returns a List containing all the visible Cards
     * The visible Cards are the one on the top of every Development Grid Cell
     * If a Cell is empty, nothing is added to the List to return
     *
     * @return An unmodifiable List containing all the visible Cards of the Grid
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
     * Retrieves and returns a Development Grid cell (one element of the bi-dimensional grid array)
     *
     * @param row The desired row of the Grid
     * @param col The desired column of the Grid
     * @return The corresponding Development Grid Cell
     */
    public DevelopmentGridCell getDevelopmentGridCell(int row, int col) {
        return grid[row][col];
    }
}
