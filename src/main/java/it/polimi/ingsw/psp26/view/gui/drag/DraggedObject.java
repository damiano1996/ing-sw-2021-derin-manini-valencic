package it.polimi.ingsw.psp26.view.gui.drag;

import it.polimi.ingsw.psp26.view.gui.drag.targetstrategies.TargetContainer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;

import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

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

                System.out.println("DraggedObject - width:" + tTargetContainer.getPane().getWidth() + " - x: " + tTargetContainer.getPane().localToScene(tTargetContainer.getPane().getBoundsInLocal()));

                if (imageView.localToScene(imageView.getBoundsInLocal()).intersects(tTargetContainer.getPane().localToScene(tTargetContainer.getPane().getBoundsInLocal()))) {

                    if (tTargetContainer.place(object)) {
                        movedSuccessfully = true;
                        sourcePane.getChildren().remove(imageView);
                        if (!tTargetContainer.getPane().getChildren().contains(imageView))
                            tTargetContainer.getPane().getChildren().add(imageView);
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
            if (mouseEvent.isPrimaryButtonDown() && selected) {
                imageView.setX(mouseEvent.getX() - imageView.getImage().getWidth() / 2);
                imageView.setY(mouseEvent.getY() - imageView.getImage().getHeight() / 2);
            }
        });
    }
}
