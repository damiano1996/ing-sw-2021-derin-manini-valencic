package it.polimi.ingsw.psp26.view.gui.loading;

import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.AsynchronousDrawer;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.DelayedJob;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.JobListener;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getResource;

public class WaitingScreen {

    private final Pane parent;
    private final JobListener jobListener;
    private final DelayedJob delayedJob;
    private final String waitingMessage;

    private boolean runningAnimation;
    private AnimationTimer animationTimer;

    public WaitingScreen(Pane parent, JobListener jobListener, DelayedJob delayedJob, String waitingMessage) {
        this.parent = parent;
        this.jobListener = jobListener;
        this.delayedJob = delayedJob;
        this.waitingMessage = waitingMessage;
    }

    private void initializeWaitingScreen() {
        VBox animationContainer = new VBox();
        parent.getChildren().add(animationContainer);

        Canvas canvas = new Canvas(500 * getGeneralRatio(), 500 * getGeneralRatio());
        animationContainer.getChildren().add(canvas);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

        List<Image> images = new ArrayList<>();
        for (Resource resource : ResourceSupply.RESOURCES_SLOTS)
            images.add(getResource(resource, getGeneralRatio()));

        final long startNanoTime = System.nanoTime();
        double phaseShift = 2 * Math.PI / images.size();

        Text text = new Text(waitingMessage);
        text.setId("title");
        animationContainer.getChildren().add(text);

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
    }

    public void start() {
        initializeWaitingScreen();

        animationTimer.start();

        new AsynchronousDrawer(
                jobListener,
                () -> {
                    runningAnimation = false;
                    animationTimer.stop();
                    delayedJob.execute();
                },
                false
        ).start();
    }
}
