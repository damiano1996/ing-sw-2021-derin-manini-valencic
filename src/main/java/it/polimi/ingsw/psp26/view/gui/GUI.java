package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.ViewInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_NAME;
import static it.polimi.ingsw.psp26.view.gui.FramePane.addBackground;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getWindowWidth;
import static it.polimi.ingsw.psp26.view.gui.PlayingPane.getPlayingPane;

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Scene scene;
    private Pane root;

    public GUI(Client client) {
        this.client = client;
    }

    public GUI() { // only to run the GUI without active server
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        client = new Client(this);

        root = new VBox();

        // temporary
        Text title = new Text(GAME_NAME);
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        root.getChildren().add(title);

        Label nicknameLabel = new Label("Nickname:");
        root.getChildren().add(nicknameLabel);
        TextField nicknameTextField = new TextField();
        root.getChildren().add(nicknameTextField);

        Label serverIPLabel = new Label("Server IP:");
        root.getChildren().add(serverIPLabel);
        TextField serverIPTextField = new TextField();
        root.getChildren().add(serverIPTextField);

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(event -> {

            String nickname = nicknameTextField.getText();
            String serverIP = serverIPTextField.getText();

            client.setNickname(nickname);
            client.initializeNetworkHandler(serverIP);

            // temporary to go to the playing scene
            stage.getScene().setRoot(addBackground(getPlayingPane(), getWindowWidth(), getWindowWidth() * 3 / 4, 1.2f, getWindowWidth()));
        });
        root.getChildren().add(connectButton);

        scene = new Scene(root);

        stage.setTitle(GAME_NAME);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void start() {
        launch();
    }

    @Override
    public void displayLogIn() {
    }

    @Override
    public void displayLeaderCards(List<LeaderCard> leaderCards) {

    }

    @Override
    public void displayNotifications(List<String> notifications) {

    }

    @Override
    public void displayInkwell(boolean isPrintable) {

    }

    @Override
    public void displayPersonalBoard(Player player, boolean isMultiplayerMode) {

    }

    @Override
    public void displayWarehouseDepots(Warehouse warehouse) {

    }

    @Override
    public void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourceToAdd) {

    }

    @Override
    public void displayDevelopmentCardBuyAction(DevelopmentGrid developmentGrid, List<Resource> playerResources) {

    }

    @Override
    public void displayStrongbox(List<Resource> strongbox) {

    }

    @Override
    public void displayMarketAction(MarketTray marketTray, List<Resource> playerResources) {

    }

    @Override
    public void displayFaithTrack(FaithTrack faithTrack, boolean isMultiplayerMode) {

    }

    @Override
    public void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots) {

    }

    @Override
    public void displayMarketTray(MarketTray marketTray) {

    }

    @Override
    public void displayDevelopmentGrid(DevelopmentGrid developmentGrid) {

    }

    @Override
    public void displayResourceSupply(ResourceSupply resourceSupply) {

    }

    @Override
    public void displayProductionActivation(List<Production> productions, List<Resource> playerResources) {

    }

    @Override
    public void displayMarketScreen(MarketTray marketTray) {

    }

    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasQuitOption) {

    }

    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {

    }

    @Override
    public void displayText(String text) {

    }

    @Override
    public void displayEndGame(HashMap<String, Integer> playersVictoryPoints) {

    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayWaitingScreen(Message message) {

    }

    @Override
    public void stopDisplayingWaitingScreen() {

    }

}
