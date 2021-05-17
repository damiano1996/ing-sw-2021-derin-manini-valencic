package it.polimi.ingsw.psp26.view.gui;

import java.awt.*;

public class GUIWindowConfigurations {

    public static final float REFERENCE_WIDTH = 2867;
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    public static float getGeneralRatio() {
        return (float) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / REFERENCE_WIDTH);
    }

    public static int getScreenWidth() {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }

    public static int getScreenHeight() {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }

    public static int getWindowWidth() {
        return Math.min(WINDOW_WIDTH, getScreenWidth()) - 100;
    }

    public static int getWindowHeight() {
        return Math.min(WINDOW_HEIGHT, getScreenHeight()) - 100;
    }
}
