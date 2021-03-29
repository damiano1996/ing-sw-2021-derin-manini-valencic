package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;

public class DevelopmentGrid {

    public static final Color[] COLORS = new Color[]{Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE};
    public static final Level[] LEVELS = new Level[]{Level.THIRD, Level.SECOND, Level.FIRST};

    private final DevelopmentGridCell[][] grid;

    public DevelopmentGrid() {
        grid = new DevelopmentGridCell[LEVELS.length][COLORS.length];

        initializeGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < LEVELS.length; row++) {
            for (int col = 0; col < COLORS.length; col++) {
                grid[row][col] = new DevelopmentGridCell(new DevelopmentCardType(COLORS[col], LEVELS[row]));
            }
        }
    }

    private int getRow(Level level) throws LevelDoesNotExistException {
        for (int row = 0; row < grid.length; row++)
            if (grid[row][0].getDevelopmentCardType().getLevel().equals(level))
                return row;
        throw new LevelDoesNotExistException();
    }

    private int getColumn(Color color) throws ColorDoesNotExistException {
        for (int col = 0; col < grid[0].length; col++)
            if (grid[0][col].getDevelopmentCardType().getColor().equals(color))
                return col;
        throw new ColorDoesNotExistException();
    }

    public DevelopmentCard drawCard(Color color, Level level) throws LevelDoesNotExistException, ColorDoesNotExistException, NoMoreDevelopmentCardsException {
        int row = getRow(level);
        int col = getColumn(color);
        return grid[row][col].drawCard();
    }

    public boolean isAvailable(Color color, Level level) throws LevelDoesNotExistException, ColorDoesNotExistException {
        int row = getRow(level);
        int col = getColumn(color);
        return !grid[row][col].isEmpty();
    }

    //rep exposed?
    public DevelopmentGridCell getDevelopmentGridCell(int row, int col) {
        return grid[row][col];
    }
}
