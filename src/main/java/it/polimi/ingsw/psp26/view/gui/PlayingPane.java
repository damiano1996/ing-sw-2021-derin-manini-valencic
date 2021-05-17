package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.AsynchronousDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.DevelopmentCardGridDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.MarketTrayDrawer;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static it.polimi.ingsw.psp26.view.gui.FramePane.drawThumbNail;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.PlayerDrawer.drawPlayer;

public class PlayingPane {

    private static HBox addOpponents(Stage primaryStage, int thumbnailSize, Client client) {

        int zoomFactor = 3;

        HBox hBox = new HBox();

        // adding opponents
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            hBox.getChildren().add(new Pane());
            new AsynchronousDrawer(
                    () -> client.getCachedModel().getOpponentCached(finalI).lookingForUpdate(),
                    () -> {
                        Player player = client.getCachedModel().getOpponentCached(finalI).getObject();
                        hBox.getChildren().set(
                                finalI,
                                drawThumbNail(
                                        primaryStage,
                                        drawPlayer(player, thumbnailSize, getGeneralRatio()),
                                        drawPlayer(player, zoomFactor * thumbnailSize, getGeneralRatio()),
                                        thumbnailSize, thumbnailSize)
                        );
                    },
                    true
            ).start();
        }

        return hBox;
    }

    private static VBox addMatchComponents(Stage primaryStage, int thumbnailSize, Client client) {

        int zoomFactor = 3;

        VBox vBox = new VBox();

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
                                    new MarketTrayDrawer(marketTray, zoomFactor * thumbnailSize).draw(),
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
                    DevelopmentGrid developmentGrid = client.getCachedModel().getDevelopmentGridCached().getObject();
                    vBox.getChildren().set(
                            1,
                            drawThumbNail(
                                    primaryStage,
                                    new DevelopmentCardGridDrawer(developmentGrid, thumbnailSize).draw(),
                                    new DevelopmentCardGridDrawer(developmentGrid, zoomFactor * thumbnailSize).draw(),
                                    thumbnailSize, thumbnailSize)
                    );
                },
                true
        ).start();

        return vBox;
    }

    private static VBox addRightBar() {
        return new VBox();
    }

    private static Pane addMainBox(Client client, int width) {

        StackPane root = new StackPane();

        Pane content = drawPlayer(new Player(new VirtualView(), client.getNickname(), ""), width, getGeneralRatio());
        root.getChildren().add(content);

        new AsynchronousDrawer(
                () -> client.getCachedModel().getMyPlayerCached().lookingForUpdate(),
                () -> {
                    Player player = client.getCachedModel().getMyPlayerCached().getObject();
                    root.getChildren().set(0, drawPlayer(player, width, getGeneralRatio()));
                },
                true
        ).start();

        return root;
    }

    public static BorderPane getPlayingPane(Stage primaryStage, Client client, int width) {

        BorderPane border = new BorderPane();

        int thumbnailSize = (int) (width * 0.1);

        boolean multiPlayer = !client.getMatchModeType().equals(MessageType.SINGLE_PLAYER_MODE);

        if (multiPlayer) border.setTop(addOpponents(primaryStage, thumbnailSize, client));

        border.setLeft(addMatchComponents(primaryStage, thumbnailSize, client));

        float resizeMainBlockFactor = (multiPlayer) ? 0.6f : 0.8f;
        border.setCenter(addMainBox(client, (int) (width * resizeMainBlockFactor)));

        border.setRight(addRightBar());

        return border;
    }

}
