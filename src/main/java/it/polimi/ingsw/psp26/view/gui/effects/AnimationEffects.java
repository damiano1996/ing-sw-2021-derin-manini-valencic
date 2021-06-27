package it.polimi.ingsw.psp26.view.gui.effects;

import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Class containing static methods used to decorate javaFX components with animation effects.
 */
public class AnimationEffects {

    /**
     * Method to add a shift movement to the image view when mouse pass over it.
     *
     * @param imageView image view to decorate
     * @param ratio     ratio of the view
     */
    public static void addMouseOverAnimation(ImageView imageView, float ratio) {
        int shift = (int) (10 * ratio);

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> imageView.setY(imageView.getY() - shift));
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> imageView.setY(imageView.getY() + shift));
    }

    /**
     * Method to add a turn card effect to an image view.
     * It adds two event filters to the image view to change the image of the image view when mouse
     * enters and exists from the image view.
     * When mouse enters, the front image will be shown.
     * When existed, the back image will be shown.
     *
     * @param imageView  image view to decorate
     * @param frontImage image to show when mouse goes over the image view
     * @param backImage  image to show at steady state
     */
    public static void addTurnCardAnimation(ImageView imageView, Image frontImage, Image backImage) {
        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> imageView.setImage(frontImage));
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> imageView.setImage(backImage));
    }

    /**
     * Method to create a rota transition of 90 degrees associated to a javaFx node.
     *
     * @param node node object
     * @return rotate transition object
     */
    private static RotateTransition halfRotation(Node node) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), node);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setByAngle(90);
        rotateTransition.setAutoReverse(false);
        return rotateTransition;
    }
}