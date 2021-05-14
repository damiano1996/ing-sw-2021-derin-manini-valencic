package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.gui.asynchronousupdates.AsynchronousUpdateDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.DevelopmentCardGridDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.MarketTrayDrawer;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static it.polimi.ingsw.psp26.view.gui.FramePane.drawThumbNail;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getWindowWidth;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.PlayerDrawer.drawPlayer;

public class PlayingPane {

    private static HBox addTopBar(Stage primaryStage, int windowWidth, Client client) {

        int boxSize = windowWidth / (2 + 3); // 2 are for market tray and development grid, 3 are for the opponent players
        int zoomFactor = 2;

        HBox hBox = new HBox();

        // adding market tray
        hBox.getChildren().add(new Pane());
        new AsynchronousUpdateDrawer(
                () -> client.getCachedModel().getMarketTrayCached().getUpdatedObject(),
                () -> {
                    MarketTray marketTray = client.getCachedModel().getMarketTrayCached().getObsoleteObject();
                    hBox.getChildren().set(
                            0,
                            drawThumbNail(
                                    primaryStage,
                                    new MarketTrayDrawer(marketTray, (int) (boxSize * 0.8f)).draw(),
                                    new MarketTrayDrawer(marketTray, zoomFactor * (int) (boxSize * 0.8f)).draw(),
                                    boxSize, zoomFactor * boxSize, getGeneralRatio())
                    );
                }
        ).start();

        // adding development card grid
        hBox.getChildren().add(new Pane());
        new AsynchronousUpdateDrawer(
                () -> client.getCachedModel().getDevelopmentGridCached().getUpdatedObject(),
                () -> {
                    DevelopmentGrid developmentGrid = client.getCachedModel().getDevelopmentGridCached().getObsoleteObject();
                    hBox.getChildren().set(
                            1,
                            drawThumbNail(
                                    primaryStage,
                                    new DevelopmentCardGridDrawer(developmentGrid, boxSize).draw(),
                                    new DevelopmentCardGridDrawer(developmentGrid, zoomFactor * boxSize).draw(),
                                    boxSize, zoomFactor * boxSize, getGeneralRatio())
                    );
                }
        ).start();

        // adding opponents
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            hBox.getChildren().add(new Pane());
            new AsynchronousUpdateDrawer(
                    () -> client.getCachedModel().getOpponentCached(finalI).getUpdatedObject(),
                    () -> {
                        Player player = client.getCachedModel().getOpponentCached(finalI).getObsoleteObject();
                        hBox.getChildren().set(
                                2 + finalI,
                                drawThumbNail(
                                        primaryStage,
                                        drawPlayer(player, boxSize, getGeneralRatio()),
                                        drawPlayer(player, zoomFactor * boxSize, getGeneralRatio()),
                                        boxSize, zoomFactor * boxSize, getGeneralRatio())
                        );
                    }
            ).start();
        }

        return hBox;
    }

    private static VBox addRightBar() {
        VBox vBox = new VBox();

        return vBox;
    }

    private static Pane addMainBox(Client client, int width) {

        StackPane root = new StackPane();

        Pane content = drawPlayer(new Player(new VirtualView(), client.getNickname(), ""), width, getGeneralRatio());
        root.getChildren().add(content);

        new AsynchronousUpdateDrawer(
                () -> client.getCachedModel().getMyPlayerCached().getUpdatedObject(),
                () -> {
                    Player player = client.getCachedModel().getMyPlayerCached().getObsoleteObject();
                    root.getChildren().set(0, drawPlayer(player, width, getGeneralRatio()));
                }
        ).start();

        return root;
    }

    public static BorderPane getPlayingPane(Stage primaryStage, Client client) {

        BorderPane border = new BorderPane();
        border.setTop(addTopBar(primaryStage, getWindowWidth(), client));

        HBox hBox = new HBox();
        hBox.getChildren().add(addMainBox(client, getWindowWidth()));
        hBox.getChildren().add(addRightBar());

        border.setLeft(hBox);
        return border;
    }

}
