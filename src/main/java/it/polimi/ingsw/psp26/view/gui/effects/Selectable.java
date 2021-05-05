package it.polimi.ingsw.psp26.view.gui.effects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

public class Selectable {

    private final ImageView imageView;
    private final Image originalImage;
    private final Image selectedImage;
    private boolean selected;

    public Selectable(ImageView imageView) {

        this.imageView = imageView;

        originalImage = this.imageView.getImage();
        selectedImage = addSelectionShadow(originalImage);
        selected = false;

        this.imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!selected) {
                this.imageView.setImage(selectedImage);
                selected = true;
            } else {
                this.imageView.setImage(originalImage);
                selected = false;
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }
}
