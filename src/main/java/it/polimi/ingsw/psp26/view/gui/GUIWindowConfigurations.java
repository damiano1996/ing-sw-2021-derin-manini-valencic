package it.polimi.ingsw.psp26.view.gui;

import java.awt.*;

/**
 * Utility class that contains constants and methods related to the screen size.
 */
public class GUIWindowConfigurations {

    public static final float REFERENCE_WIDTH = 2867;
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;


    /**
     * Getter of the ratio of the screen.
     * The ratio is used to dynamically adapt the dimensions of the shown elements among different screen sizes.
     *
     * @return the game window ratio
     */
    public static float getGeneralRatio() {
        return getWindowWidth() / REFERENCE_WIDTH;
    }


    /**
     * Getter of the width of the screen.
     *
     * @return the width of the screen
     */
    public static int getScreenWidth() {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }


    /**
     * Getter of the height of the screen.
     *
     * @return the height of the screen
     */
    public static int getScreenHeight() {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }


    /**
     * Getter of the game window width.
     *
     * @return the game window width
     */
    public static int getWindowWidth() {
        return Math.min(WINDOW_WIDTH, getScreenWidth()) - 100;
    }


    /**
     * Getter of the game window height.
     *
     * @return the game window height
     */
    public static int getWindowHeight() {
        return Math.min(WINDOW_HEIGHT, getScreenHeight()) - 100;
    }


    /**
     * Getter of the minimum value between windowWidth and windowHeight.
     *
     * @return the minimum value between windowWidth and windowHeight
     */
    public static int getMinBetweenWindowWidthAndHeight() {
        return Math.min(getWindowWidth(), getWindowHeight());
    }
}
