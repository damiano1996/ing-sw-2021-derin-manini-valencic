package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsInitializer;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.HEIGHT_RATIO;
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.WIDTH_RATIO;
import static it.polimi.ingsw.psp26.view.gui.PersonalBoardDrawer.drawPersonalBoard;

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

    public GridPane addTopBar() {
        GridPane gridPane = new GridPane();

        int padding = 20;
        gridPane.setHgap(padding);
        gridPane.setVgap(padding);
        gridPane.setPadding(new Insets(padding, padding, padding, padding));

        Text welcome = new Text("Welcome\naaa\naaaa\njjjjjj\nggg\njjj\nuuuuu");
        welcome.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        welcome.setFill(Color.WHITE);
        gridPane.getChildren().add(welcome);

        gridPane.setStyle("-fx-background-color: #336699;");
        return gridPane;
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

    @Override
    public void start(Stage stage) throws Exception {
        PersonalBoard personalBoard = new PersonalBoard(new VirtualView(), new Player(new VirtualView(), "nickname", "sessionToken"));
        try {
            personalBoard.getWarehouse().addResource(Resource.COIN);
            personalBoard.getWarehouse().addResource(Resource.SHIELD);
            personalBoard.getWarehouse().addResource(Resource.SHIELD);
            personalBoard.getWarehouse().addResource(Resource.STONE);
            personalBoard.getWarehouse().addResource(Resource.STONE);
            personalBoard.getWarehouse().addResource(Resource.STONE);

            int nResourceStrongbox = 50;
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

            personalBoard.addDevelopmentCard(2, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.GREEN, Level.FIRST)).get(0));
            personalBoard.addDevelopmentCard(2, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.YELLOW, Level.SECOND)).get(0));
            personalBoard.addDevelopmentCard(2, DevelopmentCardsInitializer.getInstance().getByDevelopmentCardType(new DevelopmentCardType(it.polimi.ingsw.psp26.model.enums.Color.BLUE, Level.THIRD)).get(0));
        } catch (CanNotAddDevelopmentCardToSlotException | DevelopmentCardSlotOutOfBoundsException e) {
            e.printStackTrace();
        }

        GridPane root = new GridPane();

        root.setOnMouseClicked(event -> {
            System.out.println("Position: " + event.getSceneX() + ":" + event.getSceneY());
        });

        root.setAlignment(Pos.TOP_LEFT);

        int topBarHeight = (int) (windowHeight * 0.2);
        int mainBoxWidth = (int) (windowWidth * 0.7);
        root.add(addTopBar(), 0, 0, windowWidth, topBarHeight);
        root.add(
                drawPersonalBoard(personalBoard, mainBoxWidth),
                0, topBarHeight, mainBoxWidth, windowHeight - topBarHeight
        );
        root.add(addRightBar(), mainBoxWidth, topBarHeight, windowWidth - mainBoxWidth, windowHeight - topBarHeight);

        Scene scene = new Scene(root);
        scene.setFill(Color.WHITE);

        stage.setTitle(GAME_NAME);

        stage.setWidth(windowWidth);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }
}
