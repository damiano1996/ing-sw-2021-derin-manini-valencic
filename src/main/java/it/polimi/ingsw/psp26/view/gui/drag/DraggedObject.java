package it.polimi.ingsw.psp26.view.gui.drag;

import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.TargetContainer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;

import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

/**
 * Class to model and control a draggable object.
 * It assigns properties to an object to be dragged and released using the mouse.
 *
 * @param <T> generic object type
 */
public class DraggedObject<T> {

    private final T object;

    private final ImageView imageView;

    private final Pane sourcePane;
    private final int initX;
    private final int initY;
    private final List<TargetContainer<T>> targetContainers;

    private final Image originalImage;
    private final Image selectedImage;

    private boolean selected;

    /**
     * Constructor of the class.
     * It defines variables and adds the properties to the imageView to allow drag and drop operations.
     *
     * @param object           object to drag
     * @param sourcePane       pane in which the object is in
     * @param imageView        image view associated to the given object
     * @param targetContainers list of feasible target containers
     */
    public DraggedObject(T object, Pane sourcePane, ImageView imageView, List<TargetContainer<T>> targetContainers) {
        this.object = object;
        this.imageView = imageView;
        this.sourcePane = sourcePane;
        initX = (int) this.imageView.getX();
        initY = (int) this.imageView.getY();
        this.targetContainers = targetContainers;

        originalImage = this.imageView.getImage();
        selectedImage = addSelectionShadow(this.originalImage);

        selected = false;

        addProperties();
    }

    /**
     * Method to add drag and drop properties to the imageView.
     * <p>
     * It defines mouse events for the imageView associated to the object.
     * Events defined are:
     * - Mouse pressed: object becomes selected and image changes to represent selection;
     * <p>
     * - Mouse released: object becomes un-selected and image changes.
     * The method checks if the object can be placed where it has been dropped
     * and it calls the place() method from the target containers to place it.
     * If it cannot be placed, it is positioned in the original position.
     * <p>
     * - Dragged event: it moves the imageview following the mouse cursor.
     */
    private void addProperties() {

        imageView.setOnMousePressed(mouseEvent -> {
            imageView.setImage(selectedImage);
            selected = true;
            mouseEvent.consume();
        });

        imageView.setOnMouseReleased(mouseEvent -> {
            imageView.setImage(originalImage);
            selected = false;

            boolean movedSuccessfully = false;
            for (TargetContainer<T> tTargetContainer : targetContainers) {

                // System.out.println("DraggedObject - width:" + tTargetContainer.getPane().getWidth() + " - x: " + tTargetContainer.getPane().localToScene(tTargetContainer.getPane().getBoundsInLocal()));

                if (imageView.localToScene(imageView.getBoundsInLocal()).intersects(
                        tTargetContainer.getTargetContainerPane().localToScene(tTargetContainer.getTargetContainerPane().getBoundsInLocal()))) {

                    if (tTargetContainer.place(object)) {
                        SoundManager soundManager = SoundManager.getInstance();
                        soundManager.setSoundEffect("button_click_02.wav");

                        movedSuccessfully = true;
                        sourcePane.getChildren().remove(imageView);
                        if (!tTargetContainer.getDraggableObjectsContainer().getChildren().contains(imageView))
                            tTargetContainer.getDraggableObjectsContainer().add(imageView, tTargetContainer.getDraggableObjectsContainer().getColumnCount() + 1, 1, 1, 1);
                    }
                    break;
                }
            }

            if (!movedSuccessfully) {
                imageView.setX(initX);
                imageView.setY(initY);
            }

            mouseEvent.consume();
        });

        imageView.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, mouseEvent -> {
            int margin = (int) (110 * getGeneralRatio());
            if (mouseEvent.isPrimaryButtonDown() && selected &&
                    sourcePane.contains(new Point2D(mouseEvent.getX() + margin, mouseEvent.getY() + margin)) &&
                    sourcePane.contains(new Point2D(mouseEvent.getX() - margin, mouseEvent.getY() - margin))) {
                // System.out.println("mouse position: " + mouseEvent.getX() + ":" + mouseEvent.getY() + " --- " + sourcePane.localToScene(sourcePane.getBoundsInLocal()));
                imageView.setX(mouseEvent.getX() - imageView.getImage().getWidth() / 2);
                imageView.setY(mouseEvent.getY() - imageView.getImage().getHeight() / 2);
            }
        });
    }
}
