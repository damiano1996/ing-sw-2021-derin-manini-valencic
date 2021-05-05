package it.polimi.ingsw.psp26.view.gui.effects;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AnimationEffects {

    public static void addMouseOverAnimation(ImageView imageView) {
        int shift = 10;

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> imageView.setY(imageView.getY() - shift));

        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> imageView.setY(imageView.getY() + shift));
    }

    public static void addMouseClickAnimation(ImageView imageView) {
        new Selectable(imageView);
    }
}