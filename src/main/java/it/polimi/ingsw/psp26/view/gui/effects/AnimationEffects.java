package it.polimi.ingsw.psp26.view.gui.effects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AnimationEffects {

    public static void addMouseOverAnimation(ImageView imageView, float ratio) {
        int shift = (int) (10 * ratio);

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> imageView.setY(imageView.getY() - shift));
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> imageView.setY(imageView.getY() + shift));
    }

    public static void addTurnCardAnimation(ImageView imageView, Image frontImage, Image backImage) {
        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> imageView.setImage(frontImage));
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> imageView.setImage(backImage));
    }
}