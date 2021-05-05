package it.polimi.ingsw.psp26.utils;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.cli.CliUtils;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewUtils {

    /**
     * Checks if the inserted char is a number or a character
     *
     * @param c The char to examine
     * @return False if c is a number, true if it's not a number
     */
    public static boolean checkAsciiRange(char c) {
        return (48 > (int) c || (int) c > 57);
    }


    /**
     * Given a List of Resources, it returns the number of a specific ResourceType contained in the List
     *
     * @param resources    The Resource List
     * @param resourceType The Resource you want to know the number of elements contained in the List
     * @return The number of resourceType contained in resources
     */
    public static int getResourcesNumber(List<Resource> resources, Resource resourceType) {
        if (!resources.contains(resourceType)) return 0;
        return (int) resources.stream().filter(x -> x.equals(resourceType)).count();
    }


    /**
     * Prints a List of Resources where it is indicated the number of Resources the Player has
     *
     * @param resources      The Player's Resources
     * @param startingRow    The row at which the List is going to be printed
     * @param startingColumn The column at which the List is going to be printed
     */
    public static void printPlayerResources(List<Resource> resources, int startingRow, int startingColumn) {
        CliUtils cliUtils = new CliUtils(new PrintWriter(System.out));

        cliUtils.pPCS("Player's Resources:", Color.WHITE, startingRow, startingColumn);

        printResourceAndNumber(Resource.SHIELD, getResourcesNumber(resources, Resource.SHIELD), startingRow + 2, startingColumn);
        printResourceAndNumber(Resource.STONE, getResourcesNumber(resources, Resource.STONE), startingRow + 2, startingColumn + 10);
        printResourceAndNumber(Resource.COIN, getResourcesNumber(resources, Resource.COIN), startingRow + 2, startingColumn + 20);
        printResourceAndNumber(Resource.SERVANT, getResourcesNumber(resources, Resource.SERVANT), startingRow + 2, startingColumn + 30);
    }


    /**
     * Prints a Resource and the number (previously calculated) of this Resource in a List
     *
     * @param resource        The Resource to print
     * @param resourceNumbers The number of this Resource in the List
     * @param startingRow     The row at which the Resource is going to be printed
     * @param startingColumn  The column at which the Resource is going to be printed
     */
    private static void printResourceAndNumber(Resource resource, int resourceNumbers, int startingRow, int startingColumn) {
        CliUtils cliUtils = new CliUtils(new PrintWriter(System.out));
        PrintWriter pw = new PrintWriter(System.out);

        cliUtils.setCursorPosition(startingRow, startingColumn);
        pw.print(cliUtils.pCS("\u2588\u2588", resource.getColor()) + "  " + resourceNumbers);
        pw.flush();
    }

}
