package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.enums.Resource;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getResource;

public class WaitingScreen {

    private static WaitingScreen instance;

    private final Pane parent;
    private Pane animationContainer;
    private Thread thread;
    private boolean running;

    private WaitingScreen(Pane parent) {
        this.parent = parent;
        running = true;
    }

    public static WaitingScreen getInstance(Pane parent) {
        if (instance == null) instance = new WaitingScreen(parent);
        return instance;
    }

    public void startWaiting(String message) {
        Platform.runLater(() -> {
            createAnimationContainer(message);
            startAnimationThread();
        });
    }

    private void createAnimationContainer(String message) {
        animationContainer = new Pane();

        parent.getChildren().add(animationContainer);

        for (int i = 0; i < ResourceSupply.RESOURCES_SLOTS.length; i++) {
            Resource resource = ResourceSupply.RESOURCES_SLOTS[i];

            Image resourceImage = getResource(resource, getGeneralRatio() * 3);
            ImageView imageView = getImageView(resourceImage,
                    (int) (getScreenWidth() * 0.5 - 200 * getGeneralRatio()),
                    (int) (getScreenHeight() * 0.5 - 300 * getGeneralRatio()));
            animationContainer.getChildren().add(imageView);
        }

        Text text = new Text(message);
        text.setId("title");
        parent.getChildren().add(text);
    }

    private void startAnimationThread() {
        final long startNanoTime = System.nanoTime();

        thread = new Thread(() -> {
            while (running) {
                double t = (System.nanoTime() - startNanoTime) / 1000000000.0;

                for (int i = 0; i < ResourceSupply.RESOURCES_SLOTS.length; i++) {

                    double phaseShift = 2 * Math.PI / ResourceSupply.RESOURCES_SLOTS.length;
                    double x = 300 * Math.sin(2 * t + phaseShift * i);
                    double y = 300 * Math.cos(2 * t + phaseShift * i);

                    ImageView imageView = (ImageView) (animationContainer.getChildren().get(i));
                    imageView.setX(imageView.getX() + x / 2 * getGeneralRatio());
                    imageView.setY(imageView.getY() + y / 2 * getGeneralRatio());
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }

    public void stopWaiting() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
