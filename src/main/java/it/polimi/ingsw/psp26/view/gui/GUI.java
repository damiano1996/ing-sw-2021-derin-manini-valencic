package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.ServerIsNotReachableException;
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
import it.polimi.ingsw.psp26.view.gui.choicesdrawers.ChoicesDrawer;
import it.polimi.ingsw.psp26.view.gui.choicesdrawers.LeaderCardChoicesDrawer;
import it.polimi.ingsw.psp26.view.gui.choicesdrawers.MessageTypeChoicesDrawer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.view.gui.DialogStage.getDialog;
import static it.polimi.ingsw.psp26.view.gui.FramePane.addBackground;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.PlayingPane.getPlayingPane;

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage primaryStage;
    private Pane root;

    public GUI() {
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void setStageWindowProperties(Stage stage) {
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setAlwaysOnTop(true);
        stage.sizeToScene();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        client = new Client(this);

        root = addBackground(new Pane(), getScreenWidth(), getScreenHeight(), 1, 1);

        Scene scene = new Scene(root);
        addStylesheet(scene);

        this.primaryStage = primaryStage;
        this.primaryStage.setScene(scene);
        setStageWindowProperties(this.primaryStage);
        this.primaryStage.show();

        displayLogIn();
    }

    @Override
    public void start() {
        launch();
    }

    @Override
    public void displayLogIn() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/gui/fxml/login.fxml"));
            VBox loginPane = fxmlLoader.load();

            Stage dialog = getDialog(primaryStage, loginPane, 1000, 1000, getGeneralRatio());

            Button connectionButton = (Button) fxmlLoader.getNamespace().get("connectionButton");
            connectionButton.setOnAction(event -> {

                connectionButton.setDisable(true);

                String nickname = ((TextField) fxmlLoader.getNamespace().get("nicknameTextField")).getText();
                String serverIP = ((TextField) fxmlLoader.getNamespace().get("serverIPTextField")).getText();

                client.setNickname(nickname);
                try {
                    client.initializeNetworkHandler(serverIP);

                    displayChoices(
                            MULTI_OR_SINGLE_PLAYER_MODE,
                            "Choose the playing mode:",
                            Arrays.asList(new MessageType[]{SINGLE_PLAYER_MODE, TWO_PLAYERS_MODE, THREE_PLAYERS_MODE, FOUR_PLAYERS_MODE}),
                            1, 1,
                            false
                    );

                    dialog.close();

                } catch (ServerIsNotReachableException serverIsNotReachableException) {
                    ((Text) fxmlLoader.getNamespace().get("errorConnection")).setVisible(true);
                    connectionButton.setDisable(false);
                }

            });

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void displayLeaderCards(List<LeaderCard> leaderCards) {

    }

    @Override
    public void displayNotifications(List<String> notifications) {

    }


    @Override
    public void displayPersonalBoard(Player player, boolean isMultiplayerMode) {

    }


    @Override
    public void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourceToAdd) {

    }

    @Override
    public void displayDevelopmentCardBuyAction(DevelopmentGrid developmentGrid, List<Resource> playerResources) {

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
    public void displayDevelopmentGrid(DevelopmentGrid developmentGrid) {

    }

    @Override
    public void displayResourceSupply(ResourceSupply resourceSupply, List<Resource> resourcesTypes) {

    }

    @Override
    public void displayProductionActivation(List<Production> productions, List<Resource> playerResources) {

    }

    @Override
    public void displayMarketScreen(MarketTray marketTray) {

    }

    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption) {

        VBox choicesBox = new VBox(10 * getGeneralRatio());
        Stage dialog = getDialog(primaryStage, choicesBox, 1000, 1000, getGeneralRatio());

        Text title = new Text(question);
        title.setId("title");
        choicesBox.getChildren().add(title);

        Pane contentBox;
        ChoicesDrawer choicesDrawer;

        switch (messageType) {

            case MULTI_OR_SINGLE_PLAYER_MODE:
            case CHOICE_NORMAL_ACTION:
            case CHOICE_LEADER_ACTION:
                contentBox = new VBox(20 * getGeneralRatio());
                choicesDrawer = new MessageTypeChoicesDrawer();
                break;

            case CHOICE_LEADERS:
                contentBox = new HBox(5 * getGeneralRatio());
                choicesDrawer = new LeaderCardChoicesDrawer();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + messageType);
        }

        choicesBox.getChildren().add(contentBox);
        buttonOrCheckBox(contentBox, dialog, choicesDrawer, messageType, choices, minChoices, maxChoices);

        if (hasUndoOption) {
            Button undoOptionButton = new Button("UNDO");
            undoOptionButton.setId("undo-button");
            undoOptionButton.setOnAction(actionEvent -> {
                dialog.close();
                client.sendUndoMessage();
                client.viewNext();
            });
            choicesBox.getChildren().add(undoOptionButton);
        }

        dialog.show();
    }

    private void buttonOrCheckBox(Pane pane, Stage dialog, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {
        if (minChoices == 1 && maxChoices == 1) {
            buttonMultipleChoices(pane, dialog, choicesDrawer, messageType, choices);
        } else {
            checkBoxMultipleChoices(pane, dialog, choicesDrawer, messageType, choices, minChoices, maxChoices);
        }
    }

    private void buttonMultipleChoices(Pane pane, Stage dialog, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices) {

        List<Button> groupButtons = new ArrayList<>();

        for (int i = 0; i < choices.size(); i++) {

            Button button = choicesDrawer.decorateButton(new Button(), choices.get(i));
            groupButtons.add(button);

            int finalI = i;
            button.setOnAction(event -> {
                // disabling all buttons
                for (Button button1 : groupButtons) button1.setDisable(true);

                try {
                    client.notifyObservers(new Message(messageType, choices.get(finalI)));
                } catch (InvalidPayloadException ignored) {
                }

                if (messageType.equals(MULTI_OR_SINGLE_PLAYER_MODE)) {
                    client.setMatchModeType((MessageType) choices.get(finalI));
                    try {
                        client.notifyObservers(new Message(ADD_PLAYER, client.getNickname()));
                    } catch (InvalidPayloadException e) {
                        e.printStackTrace();
                    }
                }

                dialog.close();
                client.viewNext();

            });
            pane.getChildren().add(button);
        }
    }

    private void checkBoxMultipleChoices(Pane pane, Stage dialog, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {

        List<CheckBoxContainer> checkBoxContainers = new ArrayList<>();

        for (Object choice : choices) {

            CheckBoxContainer checkBox = new CheckBoxContainer<>(choice);
            checkBox = choicesDrawer.decorateCheckBoxContainer(checkBox, choice);
            checkBoxContainers.add(checkBox);
            pane.getChildren().add(checkBox);
        }

        Button confirmationButton = new Button("SELECT");
        confirmationButton.setOnAction(event -> {
            List<Object> selected = new ArrayList<>();
            for (CheckBox checkBox : checkBoxContainers)
                if (checkBox.isSelected())
                    selected.add(((CheckBoxContainer) checkBox).getContainedObject());

            if (selected.size() >= minChoices && selected.size() <= maxChoices) {
                try {
                    client.notifyObservers(
                            new Message(messageType, selected.toArray()));

                    dialog.close();
                    client.viewNext();

                } catch (InvalidPayloadException ignored) {
                }
            }
        });
        pane.getChildren().add(confirmationButton);
    }

    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {

    }

    @Override
    public void displayText(String text) {
        VBox vBox = new VBox();

        Text text1 = new Text(text);
        text1.setId("title");
        vBox.getChildren().add(text1);

        Button confirmationButton = new Button("OK");
        confirmationButton.setId("confirm-button");
        vBox.getChildren().add(confirmationButton);

        Stage dialog = getDialog(primaryStage, vBox, 1000, 1000, getGeneralRatio());
        confirmationButton.setOnAction(actionEvent -> {
            client.viewNext();
            dialog.close();
        });

        dialog.show();
    }

    @Override
    public void displayEndGame(Map<String, Integer> leaderboard) {

    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayWaitingScreen(Message message) {
        client.viewNext();
    }

    @Override
    public void stopDisplayingWaitingScreen() {

        Pane pane = addBackground(getPlayingPane(primaryStage, client), getScreenWidth(), getScreenHeight(), 1.2f, 1);
        primaryStage.getScene().setRoot(pane);

        client.viewNext();
    }

}
