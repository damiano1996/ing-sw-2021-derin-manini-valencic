package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.client.NotificationsFIFO;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.AsynchronousDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.DevelopmentCardsGridDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.MarketTrayDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.NotificationStackDrawer;
import javafx.scene.CacheHint;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static it.polimi.ingsw.psp26.view.gui.FramePane.drawThumbNail;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.REFERENCE_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.PlayerDrawer.drawPlayer;

public class PlayingPane {

    private static final int ZOOM_FACTOR = 4;

    private static VBox addOpponents(Stage primaryStage, int thumbnailSize, Client client) {

        float ratio = thumbnailSize / REFERENCE_WIDTH;

        VBox vBox = new VBox();
        vBox.setCache(true);
        vBox.setCacheHint(CacheHint.SPEED);

        // adding opponents
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            vBox.getChildren().add(new Pane());
            new AsynchronousDrawer(
                    () -> client.getCachedModel().getOpponentCached(finalI).lookingForUpdate(),
                    () -> {
                        Player player = client.getCachedModel().getOpponentCached(finalI).getObject();
                        vBox.getChildren().set(
                                finalI,
                                drawThumbNail(
                                        primaryStage,
                                        drawPlayer(player, thumbnailSize, ratio, true, client.isMultiplayerMode()),
                                        drawPlayer(player, ZOOM_FACTOR * thumbnailSize, ZOOM_FACTOR * ratio, true, client.isMultiplayerMode()),
                                        thumbnailSize, thumbnailSize)
                        );
                    },
                    true
            ).start();
        }

        return vBox;
    }

    private static VBox addMatchComponents(Stage primaryStage, int thumbnailSize, Client client) {

        VBox vBox = new VBox();
        vBox.setCache(true);
        vBox.setCacheHint(CacheHint.SPEED);

        // adding market tray
        vBox.getChildren().add(new Pane());
        new AsynchronousDrawer(
                () -> client.getCachedModel().getMarketTrayCached().lookingForUpdate(),
                () -> {
                    MarketTray marketTray = client.getCachedModel().getMarketTrayCached().getObject();
                    vBox.getChildren().set(
                            0,
                            drawThumbNail(
                                    primaryStage,
                                    new MarketTrayDrawer(marketTray, thumbnailSize).draw(),
                                    new MarketTrayDrawer(marketTray, ZOOM_FACTOR * thumbnailSize).draw(),
                                    thumbnailSize, thumbnailSize)
                    );
                },
                true
        ).start();

        // adding development card grid
        vBox.getChildren().add(new Pane());
        new AsynchronousDrawer(
                () -> client.getCachedModel().getDevelopmentGridCached().lookingForUpdate(),
                () -> {
                    DevelopmentCardsGrid developmentCardsGrid = client.getCachedModel().getDevelopmentGridCached().getObject();
                    vBox.getChildren().set(
                            1,
                            drawThumbNail(
                                    primaryStage,
                                    new DevelopmentCardsGridDrawer(developmentCardsGrid, thumbnailSize).draw(),
                                    new DevelopmentCardsGridDrawer(developmentCardsGrid, ZOOM_FACTOR * thumbnailSize).draw(),
                                    thumbnailSize, thumbnailSize)
                    );
                },
                true
        ).start();

        return vBox;
    }

    private static Pane addRightBar(int width) {
        NotificationStackDrawer notificationStackDrawer = new NotificationStackDrawer(width);

        new AsynchronousDrawer(
                () -> notificationStackDrawer.setReceivedNotifications(NotificationsFIFO.getInstance().getNotifications()),
                notificationStackDrawer::displayNotifications,
                true
        ).start();

        return notificationStackDrawer.draw();
    }

    private static Pane addMainBox(Client client, int width) {

        StackPane root = new StackPane();
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);

        root.getChildren().add(
                drawPlayer(
                        new Player(new VirtualView(), client.getNickname(), ""),
                        width,
                        getGeneralRatio(),
                        false,
                        client.isMultiplayerMode()
                ));

        new AsynchronousDrawer(
                () -> client.getCachedModel().getMyPlayerCached().lookingForUpdate(),
                () -> {
                    Player player = client.getCachedModel().getMyPlayerCached().getObject();
                    root.getChildren().set(0, drawPlayer(player, width, getGeneralRatio(), false, client.isMultiplayerMode()));
                },
                true
        ).start();

        return root;
    }

    public static BorderPane getPlayingPane(Stage primaryStage, Client client, int width) {

        BorderPane border = new BorderPane();
        border.setCache(true);
        border.setCacheHint(CacheHint.SPEED);

        int thumbnailSize = (int) (width * 0.1);

        VBox vBox = new VBox();

        vBox.getChildren().add(addMatchComponents(primaryStage, thumbnailSize, client));
        if (client.isMultiplayerMode()) vBox.getChildren().add(addOpponents(primaryStage, thumbnailSize, client));

        border.setLeft(vBox);

        border.setCenter(addMainBox(client, (int) (width * 0.8)));

        border.setRight(addRightBar((int) (width * 0.1)));

        return border;
    }

}
