package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.image.Image;

import static it.polimi.ingsw.psp26.configurations.Configurations.RESOURCES_PATH;

public class GUIUtils {

    public static String getCompletePath(String fileName) {
        return "file:" + RESOURCES_PATH + "gui/" + fileName;
    }

    public static Image loadImage(String fileName, int width) {
        System.out.println("Loading image: " + fileName);
        return new Image(getCompletePath(fileName), width, width, true, true);
    }
}
