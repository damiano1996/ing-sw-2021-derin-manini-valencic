package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsInitializer;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.DevelopmentCardGridDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.MarketTrayDrawer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Random;

import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_NAME;
import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;
import static it.polimi.ingsw.psp26.view.gui.FramePane.addBackground;
import static it.polimi.ingsw.psp26.view.gui.FramePane.drawThumbNail;
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.HEIGHT_RATIO;
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.WIDTH_RATIO;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.PlayerDrawer.drawPlayer;

public class PlayingPhase extends Application {

    private final int screenWidth;
    private final int screenHeight;

    private int windowWidth;
    private int windowHeight;

    public PlayingPhase() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) dimension.getWidth();
        screenHeight = (int) dimension.getHeight();
        System.out.println(screenWidth + ":" + screenHeight);

        windowWidth = (int) (WIDTH_RATIO * screenWidth);
        windowHeight = (int) (HEIGHT_RATIO * screenHeight);
//
//        System.out.println(windowWidth + ":" + windowHeight);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public HBox addTopBar(int windowWidth, MarketTray marketTray, DevelopmentGrid developmentGrid, Player... players) {
        HBox hBox = new HBox();

        int boxSize = windowWidth / (3 + players.length);
        int zoomFactor = 3;

        // adding market tray
        hBox.getChildren().add(
                drawThumbNail(
                        new MarketTrayDrawer(marketTray, boxSize).draw(),
                        new MarketTrayDrawer(marketTray, zoomFactor * boxSize).draw(),
                        boxSize, zoomFactor * boxSize)
        );
        // adding development card grid
        hBox.getChildren().add(
                drawThumbNail(
                        new DevelopmentCardGridDrawer(developmentGrid, boxSize).draw(),
                        new DevelopmentCardGridDrawer(developmentGrid, zoomFactor * boxSize).draw(),
                        boxSize, zoomFactor * boxSize)
        );

        for (Player player : players) {
            hBox.getChildren().add(
                    drawThumbNail(
                            drawPlayer(player, boxSize),
                            drawPlayer(player, zoomFactor * boxSize),
                            boxSize, zoomFactor * boxSize)
            );
        }
        return hBox;
    }

    public VBox addRightBar() {
        VBox vBox = new VBox();

        Text welcome = new Text("Welcome");
        welcome.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        welcome.setFill(Color.WHITE);
        vBox.getChildren().add(welcome);

        vBox.setStyle("-fx-background-color: #993350;");

        return vBox;
    }

    public Pane addMainBox(Player player, int width, int height) {
        return addBackground(drawPlayer(player, width), width, height, false);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Player player = new Player(new VirtualView(), "nickname", "sessionToken");
        player.setLeaderCards(LeaderCardsInitializer.getInstance().getLeaderCards().subList(0, 2));
        player.getLeaderCards().get(0).activate(player);

        PersonalBoard personalBoard = player.getPersonalBoard();
        try {
            personalBoard.getWarehouse().addResource(Resource.COIN);
            personalBoard.getWarehouse().addResource(Resource.SHIELD);
            personalBoard.getWarehouse().addResource(Resource.SHIELD);
            personalBoard.getWarehouse().addResource(Resource.STONE);
            personalBoard.getWarehouse().addResource(Resource.STONE);

            int nResourceStrongbox = 10;
            for (int i = 0; i < nResourceStrongbox; i++) {
                personalBoard.addResourceToStrongbox(RESOURCES_SLOTS[new Random().nextInt(RESOURCES_SLOTS.length)]);
            }
        } catch (CanNotAddResourceToWarehouse | CanNotAddResourceToStrongboxException canNotAddResourceToWarehouse) {
            canNotAddResourceToWarehouse.printStackTrace();
        }

        try {
            personalBoard.addDevelopmentCard(0, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.GREEN, Level.FIRST)).get(0));
            personalBoard.addDevelopmentCard(0, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.PURPLE, Level.SECOND)).get(0));

            personalBoard.addDevelopmentCard(1, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.YELLOW, Level.FIRST)).get(0));
            personalBoard.addDevelopmentCard(1, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.PURPLE, Level.SECOND)).get(0));

            personalBoard.addDevelopmentCard(2, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.GREEN, Level.FIRST)).get(0));
            personalBoard.addDevelopmentCard(2, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.YELLOW, Level.SECOND)).get(0));
            personalBoard.addDevelopmentCard(2, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.BLUE, Level.THIRD)).get(0));
        } catch (CanNotAddDevelopmentCardToSlotException | DevelopmentCardSlotOutOfBoundsException e) {
            e.printStackTrace();
        }

        personalBoard.getFaithTrack().getVaticanReportSections()[0].activatePopesFavorTile();
        personalBoard.getFaithTrack().addFaithPoints(3);

        windowWidth *= 0.4;
        windowHeight *= 0.4;

        BorderPane border = new BorderPane();
        border.setTop(addTopBar(windowWidth, new MarketTray(new VirtualView()), new DevelopmentGrid(new VirtualView()), player, player, player));
        border.setLeft(addMainBox(player, windowWidth, windowWidth * 9 / 16));
        border.setRight(addRightBar());

        Scene scene = new Scene(border);
        scene.setFill(Color.WHITE);

        stage.setTitle(GAME_NAME);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }
}
