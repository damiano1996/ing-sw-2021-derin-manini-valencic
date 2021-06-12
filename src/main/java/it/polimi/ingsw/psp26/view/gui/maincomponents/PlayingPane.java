package it.polimi.ingsw.psp26.view.gui.maincomponents;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.client.NotificationsFIFO;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.AsynchronousDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.DevelopmentCardsGridDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.MarketTrayDrawer;
import javafx.scene.CacheHint;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static it.polimi.ingsw.psp26.view.gui.FramePane.drawThumbNail;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.REFERENCE_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.PlayerDrawer.drawPlayer;

/**
 * Class used to draw the main scene of the game.
 * It provides a static method that returns a BorderPane containing
 * the main components of the game: personal board (of the player and of the opponents)
 * market tray, the development cards grid, the notification stack and the leader cards.
 * All components are drawn in background threads using the AsynchronousDrawer to get live updates.
 * Market tray, development cards grid and the boards of the opponents are drawn in thumbnails that can be zoomed
 * passing over with the cursor.
 */
public class PlayingPane {

    private static final int ZOOM_FACTOR = 5;

    /**
     * Method to setup the boards of the opponents.
     * It creates the thumbnail in a vertical box.
     * Graphics of the boards is updated in background using the AsynchronousDrawer.
     *
     * @param primaryStage  primary stage
     * @param thumbnailSize size of the thumbnail (side)
     * @param client        client object
     * @return vertical box containing the pane for the thumbnails
     */
    private static VBox addOpponents(Stage primaryStage, int thumbnailSize, Client client) {

        float ratio = thumbnailSize / REFERENCE_WIDTH;

        VBox vBox = new VBox(0);
        vBox.setCache(true);
        vBox.setCacheHint(CacheHint.SPEED);

        // Adding opponents
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            vBox.getChildren().add(new Pane());
            new AsynchronousDrawer(
                    () -> {
                        System.out.println("PlayingPane - waiting opponent " + finalI);
                        client.getCachedModel().getOpponentCached(finalI).lookingForUpdate();
                        System.out.println("PlayingPane - model updated for " + finalI);
                    },
                    () -> {
                        System.out.println("PlayingPane - drawing board for opponent " + finalI);
                        Player player = client.getCachedModel().getOpponentCached(finalI).getObject();
                        vBox.getChildren().set(
                                finalI,
                                drawThumbNail(
                                        primaryStage,
                                        drawPlayer(player, thumbnailSize, ratio, true, client.isMultiplayerMode()),
                                        drawPlayer(player, ZOOM_FACTOR * thumbnailSize, ZOOM_FACTOR * ratio, true, client.isMultiplayerMode()),
                                        thumbnailSize, thumbnailSize)
                        );
                        System.out.println("PlayingPane - drawing board for opponent " + finalI + " done!");

                    },
                    true
            ).start();
        }

        return vBox;
    }

    /**
     * Method to setup the market tray and the development cards grid.
     * It creates a vertical box and insert the panes of the thumbnails.
     * Graphic is updated in background using the AsynchronousDrawer.
     *
     * @param primaryStage  primary stage
     * @param thumbnailSize size of the thumbnail (side)
     * @param client        client object
     * @return vertical box containing the thumbnails
     */
    private static VBox addMatchComponents(Stage primaryStage, int thumbnailSize, Client client) {

        VBox vBox = new VBox(0);
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

    /**
     * Method to setup the notification stack.
     * It creates the stack and it starts the AsynchronousDrawer that will update the stack in background.
     * It returns the pane containing the notification stack.
     *
     * @param maxWidth max width for the stack
     * @return the pane containing the stack
     */
    private static Pane addRightBar(int maxWidth) {
        NotificationStackDrawer notificationStackDrawer = new NotificationStackDrawer(maxWidth);

        new AsynchronousDrawer(
                () -> notificationStackDrawer.setReceivedNotifications(NotificationsFIFO.getInstance().getNotifications()),
                notificationStackDrawer::displayNotifications,
                true
        ).start();

        return notificationStackDrawer.draw();
    }

    /**
     * Method to setup the personal board of the player.
     * It starts the AsynchronousDrawer that will update the board in background.
     * If no board is in the CacheModel of the client, it will initialize an empty one.
     *
     * @param client   client object
     * @param maxWidth max width for the main box
     * @return the pane containing the personal board of the player
     */
    private static Pane addMainBox(Client client, int maxWidth) {

        StackPane root = new StackPane();
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);

        root.getChildren().add(
                drawPlayer(
                        new Player(null, client.getNickname(), null),
                        maxWidth,
                        getGeneralRatio(),
                        false,
                        client.isMultiplayerMode()
                ));

        new AsynchronousDrawer(
                () -> client.getCachedModel().getMyPlayerCached().lookingForUpdate(),
                () -> {
                    Player player = client.getCachedModel().getMyPlayerCached().getObject();
                    root.getChildren().set(0, drawPlayer(player, maxWidth, getGeneralRatio(), false, client.isMultiplayerMode()));
                },
                true
        ).start();

        return root;
    }

    /**
     * Getter of the pane containing all the main component of the game:
     * personal board of the player,
     * thumbnails of the opponents' boards, of the market tray and of the development cards grid;
     * it contains also the notification stack.
     * All components are drawn in backgrounds using the AsynchronousDrawer that receives updates by the CacheModel of the client.
     *
     * @param primaryStage primary stage
     * @param client       client object
     * @param maxWidth     width that can be used to draw the pane
     * @return a border pane containing the components
     */
    public static BorderPane getPlayingPane(Stage primaryStage, Client client, int maxWidth) {

        BorderPane border = new BorderPane();
        border.setCache(true);
        border.setCacheHint(CacheHint.SPEED);

        int thumbnailSize = (int) (maxWidth * 0.08);

        VBox vBox = new VBox(0);

        vBox.getChildren().add(addMatchComponents(primaryStage, thumbnailSize, client));
        if (client.isMultiplayerMode()) vBox.getChildren().add(addOpponents(primaryStage, thumbnailSize, client));

        border.setLeft(vBox);

        border.setCenter(addMainBox(client, (int) (maxWidth * 0.8)));

        border.setRight(addRightBar((int) (maxWidth * 0.1)));

        return border;
    }

}
