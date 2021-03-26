package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.HashMap;

public class DevelopmentGrid {

    private static final Color[] COLORS = new Color[]{Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE};

    private final DevelopmentGridCell[][] grid;
    private final int initialQuantity;

    public DevelopmentGrid() {
        grid = new DevelopmentGridCell[4][4];
        initialQuantity = 3;

        initializeThirdLevel();
        initializeSecondLevel();
        initializeFirstLevel();
    }

    private void initializeThirdLevel() {
        Resource[] costs = new Resource[]{Resource.SHIELD, Resource.COIN, Resource.STONE, Resource.SERVANT};
        Resource[] productionCosts = new Resource[]{Resource.SERVANT, Resource.STONE, Resource.SHIELD, Resource.COIN};
        Resource[] productionReturns = new Resource[]{Resource.COIN, Resource.SHIELD, Resource.SERVANT, Resource.STONE};

        for (int col = 0; col < COLORS.length; col++) {
            int finalCol = col;
            grid[0][col] = new DevelopmentGridCell(
                    COLORS[col], Level.THIRD,
                    initialQuantity,
                    new HashMap<>() {{
                        put(costs[finalCol], 7);
                    }},
                    new HashMap<>() {{
                        put(productionCosts[finalCol], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns[finalCol], 1);
                        put(Resource.FAITH_MARKERS, 3);
                    }},
                    11);
        }
    }

    private void initializeSecondLevel() {
        Resource[] costs = new Resource[]{Resource.SHIELD, Resource.COIN, Resource.STONE, Resource.SERVANT};
        Resource[] productionCosts = new Resource[]{Resource.STONE, Resource.SERVANT, Resource.SHIELD, Resource.COIN};

        for (int col = 0; col < COLORS.length; col++) {
            int finalCol = col;
            grid[1][col] = new DevelopmentGridCell(
                    COLORS[col], Level.THIRD,
                    initialQuantity,
                    new HashMap<>() {{
                        put(costs[finalCol], 4);
                    }},
                    new HashMap<>() {{
                        put(productionCosts[finalCol], 1);
                    }},
                    new HashMap<>() {{
                        put(Resource.FAITH_MARKERS, 2);
                    }},
                    5);
        }
    }

    private void initializeFirstLevel() {
        Resource[] costs = new Resource[]{Resource.SHIELD, Resource.COIN, Resource.STONE, Resource.SERVANT};
        Resource[] productionCosts = new Resource[]{Resource.COIN, Resource.SHIELD, Resource.SERVANT, Resource.STONE};

        for (int col = 0; col < COLORS.length; col++) {
            int finalCol = col;
            grid[2][col] = new DevelopmentGridCell(
                    COLORS[col], Level.THIRD,
                    initialQuantity,
                    new HashMap<>() {{
                        put(costs[finalCol], 2);
                    }},
                    new HashMap<>() {{
                        put(productionCosts[finalCol], 1);
                    }},
                    new HashMap<>() {{
                        put(Resource.FAITH_MARKERS, 1);
                    }},
                    1);
        }
    }

    private int getRow(Level level) throws LevelDoesNotExistException {
        for (int row = 0; row < grid.length; row++)
            if (grid[row][0].getLevel().equals(level))
                return row;
        throw new LevelDoesNotExistException();
    }

    private int getColumn(Color color) throws ColorDoesNotExistException {
        for (int col = 0; col < grid[0].length; col++)
            if (grid[0][col].getColor().equals(color))
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
}
