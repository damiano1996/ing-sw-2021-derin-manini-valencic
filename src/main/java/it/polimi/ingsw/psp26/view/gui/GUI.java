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
import it.polimi.ingsw.psp26.view.gui.choicesdrawers.ResourceChoicesDrawer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.WINDOW_HEIGHT;
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.WINDOW_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.addStylesheet;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getGeneralRatio;
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
//        stage.setMaximized(true);
        stage.setResizable(false);
//        stage.setFullScreen(true);
        // stage.setAlwaysOnTop(true);
        stage.sizeToScene();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        client = new Client(this);

        root = addBackground(new Pane(), WINDOW_WIDTH, WINDOW_HEIGHT);

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
            VBox loginVBox = fxmlLoader.load();

            Stage dialog = getDialog(primaryStage, loginVBox);

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
    public void waitForYourTurn() {

    }

    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption) {

        VBox mainContainer = new VBox(10 * getGeneralRatio());
        Stage dialog = getDialog(primaryStage, mainContainer);

        Label label = new Label(question);
        label.setId("label");
        label.setWrapText(true);
        mainContainer.getChildren().add(label);

        ChoicesDrawer choicesDrawer;

        switch (messageType) {

            case MULTI_OR_SINGLE_PLAYER_MODE:
            case CHOICE_NORMAL_ACTION:
            case CHOICE_LEADER_ACTION:
                choicesDrawer = new MessageTypeChoicesDrawer();
                break;

            case CHOICE_LEADERS:
                choicesDrawer = new LeaderCardChoicesDrawer();
                break;

            case CHOICE_RESOURCE_FROM_WAREHOUSE:
            case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:
                choicesDrawer = new ResourceChoicesDrawer();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + messageType);
        }

        GridPane contentPane = new GridPane();
        contentPane.setHgap(10 * getGeneralRatio());
        contentPane.setVgap(10 * getGeneralRatio());
        mainContainer.getChildren().add(contentPane);
        buttonOrCheckBox(contentPane, dialog, choicesDrawer, messageType, choices, minChoices, maxChoices);

        if (hasUndoOption) {
            Button undoOptionButton = new Button("UNDO");
            undoOptionButton.setId("undo-button");
            undoOptionButton.setOnAction(actionEvent -> {
                dialog.close();
                client.sendUndoMessage();
                client.viewNext();
            });
            mainContainer.getChildren().add(undoOptionButton);
        }

        dialog.show();
    }

    private void buttonOrCheckBox(GridPane container, Stage dialog, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {
        if (minChoices == 1 && maxChoices == 1) {
            buttonMultipleChoices(container, dialog, choicesDrawer, messageType, choices);
        } else {
            checkBoxMultipleChoices(container, dialog, choicesDrawer, messageType, choices, minChoices, maxChoices);
        }
    }

    private void buttonMultipleChoices(GridPane container, Stage dialog, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices) {

        List<ButtonContainer> buttonContainers = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < choices.size(); i++) {

            ButtonContainer buttonContainer = choicesDrawer.decorateButtonContainer(new ButtonContainer(choices.get(i)));
            buttonContainers.add(buttonContainer);

            int finalI = i;
            buttonContainer.setOnAction(event -> {
                // disabling all buttons
                for (Button button1 : buttonContainers) button1.setDisable(true);

                try {
                    client.notifyObservers(new Message(messageType, buttonContainer.getContainedObject()));
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
            if (choices.size() % 2 != 0 && i == choices.size() - 1)
                container.add(new VBox(buttonContainer), 0, container.getRowCount() + 1, container.getColumnCount(), 1);
            else container.add(buttonContainer, i % 2, j % 2, 1, 1);
            j += i % 2;
        }
    }

    private void checkBoxMultipleChoices(GridPane container, Stage dialog, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {

        List<ButtonContainer> buttonContainers = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < choices.size(); i++) {

            ButtonContainer buttonContainer = choicesDrawer.decorateButtonContainer(new ButtonContainer(choices.get(i)));
            buttonContainers.add(buttonContainer);

            if (choices.size() % 2 != 0 && i == choices.size() - 1)
                container.add(new VBox(buttonContainer), 0, container.getRowCount() + 1, container.getColumnCount(), 1);
            else container.add(buttonContainer, i % 2, j % 2, 1, 1);
            j += i % 2;
        }

        Button confirmationButton = new Button("SELECT");

        confirmationButton.setOnAction(event -> {
            List<Object> selected = new ArrayList<>();
            for (ButtonContainer buttonContainer : buttonContainers)
                if (buttonContainer.isClicked())
                    selected.add(buttonContainer.getContainedObject());

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

        container.add(new VBox(confirmationButton), 0, container.getRowCount() + 1, container.getColumnCount(), 1);
    }

    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {

    }

    public void displayTextDialog(String text, String textId) {
        VBox vBox = new VBox(20 * getGeneralRatio());

        Text text1 = new Text(text);
        text1.setId(textId);
        // text1.setWrapText(true);
        vBox.getChildren().add(text1);

        Button confirmationButton = new Button("OK");
        confirmationButton.setId("confirm-button");
        vBox.getChildren().add(confirmationButton);

        Stage dialog = getDialog(primaryStage, vBox);
        confirmationButton.setOnAction(actionEvent -> {
            client.viewNext();
            dialog.close();
        });


        dialog.show();
    }

    @Override
    public void displayText(String text) {
        displayTextDialog(text, "text-field");
    }

    @Override
    public void displayEndGame(Map<String, Integer> leaderboard, String winningPlayer) {
    }

    @Override
    public void displayError(String error) {
        displayTextDialog(error, "error");
    }

    @Override
    public void displayWaitingScreen(Message message) {
//        try {
//            WaitingScreen.getInstance(root).startWaiting((String) message.getPayload());
//        } catch (EmptyPayloadException e) {
//            e.printStackTrace();
//        }
        client.viewNext();
    }

    @Override
    public void stopDisplayingWaitingScreen() {
        // WaitingScreen.getInstance(root).stopWaiting();

        Pane pane = addBackground(getPlayingPane(primaryStage, client, (int) (WINDOW_WIDTH * 0.5)), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.getScene().setRoot(pane);

        client.viewNext();
    }

}
