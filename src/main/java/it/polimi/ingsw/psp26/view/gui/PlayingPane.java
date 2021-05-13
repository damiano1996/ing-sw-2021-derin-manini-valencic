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

        new AsynchronousUpdateDrawer(
                () -> client.getCachedModel().getMarketTrayCached().getUpdatedObject(),
                () -> {
                    MarketTray marketTray = client.getCachedModel().getMarketTrayCached().getObsoleteObject();
                    hBox.getChildren().set(
                            0,
                            drawThumbNail(
                                    new MarketTrayDrawer(marketTray, boxSize).draw(),
                                    new MarketTrayDrawer(marketTray, zoomFactor * boxSize).draw(),
                                    boxSize, zoomFactor * boxSize, ratio)
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
                                    new DevelopmentCardGridDrawer(developmentGrid, boxSize).draw(),
                                    new DevelopmentCardGridDrawer(developmentGrid, zoomFactor * boxSize).draw(),
                                    boxSize, zoomFactor * boxSize, ratio)
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
                                        drawPlayer(player, boxSize, ratio),
                                        drawPlayer(player, zoomFactor * boxSize, ratio),
                                        boxSize, zoomFactor * boxSize, ratio)
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

    private static Pane addMainBox(Client client, int width, float ratio) {

        StackPane root = new StackPane();

        Pane content = drawPlayer(new Player(new VirtualView(), client.getNickname(), ""), width, ratio);
        root.getChildren().add(content);

        new AsynchronousUpdateDrawer(
                () -> client.getCachedModel().getMyPlayerCached().getUpdatedObject(),
                () -> {
                    Player player = client.getCachedModel().getMyPlayerCached().getObsoleteObject();
                    root.getChildren().set(0, drawPlayer(player, width, ratio));
                }
        ).start();

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
