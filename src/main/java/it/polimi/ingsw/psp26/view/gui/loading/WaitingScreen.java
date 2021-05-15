package it.polimi.ingsw.psp26.view.gui.loading;

import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.enums.Resource;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getResource;

public class WaitingScreen {

    private static WaitingScreen instance;

    private final Pane parent;
    private Pane animationContainer;
    private AnimationTimer animationTimer;
    private Thread thread;
    private boolean runningAnimation;

    private WaitingScreen(Pane parent) {
        this.parent = parent;
    }

    public static WaitingScreen getInstance(Pane parent) {
        if (instance == null) instance = new WaitingScreen(parent);
        return instance;
    }

    public void startWaiting(Task<Void> task, String message) {

        animationContainer = new VBox();
        parent.getChildren().add(animationContainer);

        Canvas canvas = new Canvas(500 * getGeneralRatio(), 500 * getGeneralRatio());
        animationContainer.getChildren().add(canvas);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

        List<Image> images = new ArrayList<>();
        for (Resource resource : ResourceSupply.RESOURCES_SLOTS)
            images.add(getResource(resource, getGeneralRatio()));

        final long startNanoTime = System.nanoTime();
        double phaseShift = 2 * Math.PI / images.size();

        runningAnimation = true;

        animationTimer = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (runningAnimation) {
                    double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                    graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    for (int i = 0; i < images.size(); i++) {
                        double x = 100 * Math.sin(2 * t + phaseShift * i);
                        double y = 100 * Math.cos(2 * t + phaseShift * i);

                        graphicsContext2D.drawImage(
                                images.get(i),
                                (canvas.getWidth() / 2 + x + 50) * getGeneralRatio(),
                                (canvas.getHeight() / 2 + y) * getGeneralRatio()
                        );
                    }
                }
            }
        };

        animationTimer.start();

        Text text = new Text(message);
        text.setId("title");
        animationContainer.getChildren().add(text);

        // TODO: to be solved
        task.run();
    }
}
