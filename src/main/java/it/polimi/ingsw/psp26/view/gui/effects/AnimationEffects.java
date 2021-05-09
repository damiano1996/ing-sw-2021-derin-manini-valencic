package it.polimi.ingsw.psp26.view.gui.effects;

import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AnimationEffects {

    public static void addMouseOverAnimation(ImageView imageView, float ratio) {
        int shift = (int) (10 * ratio);

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> imageView.setY(imageView.getY() - shift));
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> imageView.setY(imageView.getY() + shift));
    }

    public static void addTurnCardAnimation(ImageView imageView, Image frontImage, Image backImage) {
        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> imageView.setImage(frontImage));
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> imageView.setImage(backImage));

//        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
//            RotateTransition firstHalfRotation = halfRotation(imageView);
//
//            firstHalfRotation.setOnFinished(actionEvent -> {
//                // changing image
//                imageView.setImage(frontImage);
//                RotateTransition secondHalfRotation = halfRotation(imageView);
//                secondHalfRotation.play();
//            });
//
//            firstHalfRotation.play();
//
//        });

//        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> {
//            RotateTransition firstHalfRotation = halfRotation(imageView);
//
//            firstHalfRotation.setOnFinished(actionEvent -> {
//                // changing image
//                imageView.setImage(backImage);
//                RotateTransition secondHalfRotation = halfRotation(imageView);
//                secondHalfRotation.play();
//            });
//
//            firstHalfRotation.play();
//
//        });
    }

    private static RotateTransition halfRotation(Node node) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), node);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setByAngle(90);
        rotateTransition.setAutoReverse(false);
        return rotateTransition;
    }
}