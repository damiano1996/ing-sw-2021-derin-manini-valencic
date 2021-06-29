package it.polimi.ingsw.psp26.view.gui.maincomponents;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.client.NotificationsFIFO;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.AsynchronousDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.DevelopmentCardsGridDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.MarketTrayDrawer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.FramePane.drawThumbNail;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.*;
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
    private static PlayingPane instance;
    private final List<AsynchronousDrawer> asynchronousDrawers;

    private Button resumeButton;

    /**
     * Class constructor.
     */
    private PlayingPane() {
        asynchronousDrawers = new ArrayList<>();
    }

    /**
     * Getter of the instance of the singleton.
     *
     * @return instance of this singleton
     */
    public static PlayingPane getInstance() {
        if (instance == null) instance = new PlayingPane();
        return instance;
    }

    /**
     * Method to show the resume button.
     */
    public void showResumeButton() {
        resumeButton.setVisible(true);
    }

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
    private VBox addOpponents(Stage primaryStage, int thumbnailSize, Client client) {

        float ratio = thumbnailSize / REFERENCE_WIDTH;

        VBox vBox = new VBox(0);
        vBox.setCache(true);
        vBox.setCacheHint(CacheHint.SPEED);

        // Adding opponents
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            vBox.getChildren().add(new Pane());
            AsynchronousDrawer asynchronousDrawer = new AsynchronousDrawer(
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
            );
            asynchronousDrawer.start();
            asynchronousDrawers.add(asynchronousDrawer);
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
    private VBox addMatchComponents(Stage primaryStage, int thumbnailSize, Client client) {

        VBox vBox = new VBox(0);
        vBox.setCache(true);
        vBox.setCacheHint(CacheHint.SPEED);

        // adding market tray
        vBox.getChildren().add(new Pane());
        AsynchronousDrawer marketAsynchronousDrawer = new AsynchronousDrawer(
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
        );
        marketAsynchronousDrawer.start();
        asynchronousDrawers.add(marketAsynchronousDrawer);

        // adding development card grid
        vBox.getChildren().add(new Pane());
        AsynchronousDrawer developmentCardsAsynchronousDrawer = new AsynchronousDrawer(
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
        );
        developmentCardsAsynchronousDrawer.start();
        asynchronousDrawers.add(developmentCardsAsynchronousDrawer);

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
    private Pane addRightBar(int maxWidth) {
        NotificationStackDrawer notificationStackDrawer = new NotificationStackDrawer(maxWidth);

        AsynchronousDrawer asynchronousDrawer = new AsynchronousDrawer(
                () -> notificationStackDrawer.setReceivedNotifications(NotificationsFIFO.getInstance().getNotifications()),
                notificationStackDrawer::displayNotifications,
                true
        );
        asynchronousDrawer.start();
        asynchronousDrawers.add(asynchronousDrawer);

        return notificationStackDrawer.draw();
    }

    /**
     * Method to define and to add properties to the resume button.
     * If pressed the last message will be re-handled.
     * It will be initialized with visibility equals to false.
     *
     * @param client client object
     * @return resume button
     */
    private Button getResumeButton(Client client) {
        resumeButton = new Button("Resume");
        resumeButton.setId("button");
        resumeButton.setVisible(false);
        resumeButton.setOnMouseClicked(mouseEvent -> {
            resumeButton.setVisible(false);
            client.reHandleLastMessage();
        });
        return resumeButton;
    }


    /**
     * Method to create the top bar of the playing pane.
     * It contains the buttons to mute and unmute the music and sound effects.
     * it contains the resume button.
     *
     * @param client   client object
     * @param maxWidth maximum width allowed
     * @return horizontal box containing the components
     */
    private HBox addTopBar(Client client, int maxWidth) {

        Button MuteMusicButton = new Button();
        Button MuteEffectButton = new Button();

        MuteMusicButton.setMaxWidth(0.08 * getWindowHeight());
        MuteMusicButton.setMaxHeight(0.08 * getWindowHeight());

        MuteEffectButton.setMaxWidth(0.08 * getWindowHeight());
        MuteEffectButton.setMaxHeight(0.08 * getWindowHeight());

        MuteMusicButton.setId("music-on-button");
        MuteEffectButton.setId("sound-effect-on-button");


        MuteMusicButton.setOnMouseClicked(mouseEvent -> {

            SoundManager soundManager = SoundManager.getInstance();
            if ((int) soundManager.getVolumeMusic() != 0) {
                soundManager.muteMusic();
                MuteMusicButton.setId("music-off-button");
            } else {
                soundManager.unmuteMusic();
                MuteMusicButton.setId("music-on-button");
            }

        });

        MuteEffectButton.setOnMouseClicked(mouseEvent -> {

            SoundManager soundManager = SoundManager.getInstance();
            if ((int) soundManager.getVolumeEffect() != 0) {
                soundManager.muteEffect();
                MuteEffectButton.setId("sound-effect-off-button");
            } else {
                soundManager.unmuteEffect();
                MuteEffectButton.setId("sound-effect-on-button");
            }

        });

        HBox hBox = new HBox(10);

        hBox.getChildren().addAll(MuteEffectButton, MuteMusicButton, getResumeButton(client));

        hBox.setMaxHeight(0.1 * getWindowHeight());
        hBox.setMaxWidth(0.35 * getScreenWidth());
        hBox.setCache(true);
        hBox.setCacheHint(CacheHint.SPEED);


        return hBox;
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
    private Pane addMainBox(Client client, int maxWidth) {

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

        AsynchronousDrawer asynchronousDrawer = new AsynchronousDrawer(
                () -> client.getCachedModel().getMyPlayerCached().lookingForUpdate(),
                () -> {
                    Player player = client.getCachedModel().getMyPlayerCached().getObject();
                    root.getChildren().set(0, drawPlayer(player, maxWidth, getGeneralRatio(), false, client.isMultiplayerMode()));
                },
                true
        );
        asynchronousDrawer.start();
        asynchronousDrawers.add(asynchronousDrawer);

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
    public BorderPane getPlayingPane(Stage primaryStage, Client client, int maxWidth) {
        getResumeButton(client);

        BorderPane border = new BorderPane();
        border.setCache(true);
        border.setCacheHint(CacheHint.SPEED);

        int thumbnailSize = (int) (maxWidth * 0.08);

        VBox vBox = new VBox(0);

        vBox.getChildren().add(addMatchComponents(primaryStage, thumbnailSize, client));
        if (client.isMultiplayerMode()) vBox.getChildren().add(addOpponents(primaryStage, thumbnailSize, client));

        border.setLeft(vBox);

        border.setTop(addTopBar(client, (int) (maxWidth * 0.4)));

        border.setCenter(addMainBox(client, (int) (maxWidth * 0.8)));

        border.setRight(addRightBar((int) (maxWidth * 0.1)));

        return border;
    }

    /**
     * Method to stop loop of the asynchronous drawers and
     * to clean the list containing them.
     */
    public void stopAsynchronousDrawers() {
        for (AsynchronousDrawer asynchronousDrawer : asynchronousDrawers)
            asynchronousDrawer.stopLoop();

        asynchronousDrawers.clear();
    }

}
