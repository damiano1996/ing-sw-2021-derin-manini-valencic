package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.DevelopmentCardGridDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.MarketTrayDrawer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.*;

import static it.polimi.ingsw.psp26.view.gui.FramePane.drawThumbNail;
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.REFERENCE_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getWindowWidth;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.PlayerDrawer.drawPlayer;

public class PlayingPane {

    private static HBox addTopBar(int windowWidth, Client client) { // TODO: temporary implementation, just to test asynchronous updates

        int boxSize = windowWidth / (2 + 4); // 2 are for market tray and development grid, 4 are for the players
        int zoomFactor = 3;
        float ratio = windowWidth / REFERENCE_WIDTH;

        HBox hBox = new HBox(10 * ratio);

        // adding market tray
        hBox.getChildren().add(new Pane());

        Task marketTrayTask = new Task() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    try {
                        MarketTray marketTray = client.getCachedModel().getObsoleteMarketTrayCached();
                        hBox.getChildren().set(
                                0,
                                drawThumbNail(
                                        new MarketTrayDrawer(marketTray, boxSize).draw(),
                                        new MarketTrayDrawer(marketTray, zoomFactor * boxSize).draw(),
                                        boxSize, zoomFactor * boxSize, ratio)
                        );
                    } catch (InterruptedException ignored) {
                    }
                });
                return null;
            }
        };

        new Thread(() -> {
            while (true) {
                try {
                    client.getCachedModel().getUpdatedMarketTrayCached();
                    marketTrayTask.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // adding development card grid
        hBox.getChildren().add(new Pane());

        Task developmentGridTask = new Task() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    try {
                        DevelopmentGrid developmentGrid = client.getCachedModel().getObsoleteDevelopmentGridCached();
                        hBox.getChildren().set(
                                1,
                                drawThumbNail(
                                        new DevelopmentCardGridDrawer(developmentGrid, boxSize).draw(),
                                        new DevelopmentCardGridDrawer(developmentGrid, zoomFactor * boxSize).draw(),
                                        boxSize, zoomFactor * boxSize, ratio)
                        );
                    } catch (InterruptedException ignored) {
                    }
                });
                return null;
            }
        };

        new Thread(() -> {
            while (true) {
                try {
                    client.getCachedModel().getObsoleteDevelopmentGridCached();
                    developmentGridTask.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        for (Player player : players) {
//            hBox.getChildren().add(
//                    drawThumbNail(
//                            drawPlayer(player, boxSize, ratio),
//                            drawPlayer(player, zoomFactor * boxSize, ratio),
//                            boxSize, zoomFactor * boxSize, ratio)
//            );
//        }
        return hBox;
    }

    private static VBox addRightBar() {
        VBox vBox = new VBox();

        return vBox;
    }

    private static Pane addMainBox(Client client, int width, float ratio) {

        StackPane root = new StackPane();

        Pane content = drawPlayer(new Player(new VirtualView(), client.getNickname(), ""), width, ratio);
        root.getChildren().add(content);

        Task task = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    try {
                        Player player = client.getCachedModel().getObsoleteMyPlayerCached();
                        root.getChildren().set(0, drawPlayer(player, width, ratio));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                return null;
            }
        };

        new Thread(() -> {
            while (true) {
                try {
                    client.getCachedModel().getUpdatedMyPlayerCached();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return root;
    }

    public static BorderPane getPlayingPane(Client client) {

        BorderPane border = new BorderPane();
        border.setTop(addTopBar(getWindowWidth(), client));

        HBox hBox = new HBox();
        hBox.getChildren().add(addMainBox(client, getWindowWidth(), getWindowWidth() / REFERENCE_WIDTH));
        hBox.getChildren().add(addRightBar());

        border.setLeft(hBox);
        return border;
    }

}
