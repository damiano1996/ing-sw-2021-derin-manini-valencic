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
import it.polimi.ingsw.psp26.view.gui.choices.ChoicesDrawer;
import it.polimi.ingsw.psp26.view.gui.choices.LeaderCardChoicesDrawer;
import it.polimi.ingsw.psp26.view.gui.choices.MessageTypeChoicesDrawer;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_NAME;
import static it.polimi.ingsw.psp26.view.gui.DialogStage.getDialog;
import static it.polimi.ingsw.psp26.view.gui.FramePane.addCoolFrame;
import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.REFERENCE_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage stage;

    private float ratio;

    public GUI() { // only to run the GUI without active server
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        client = new Client(this);

        ratio = getWindowWidth() / REFERENCE_WIDTH;

        this.stage = stage;
//        this.stage.setTitle(GAME_NAME);
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.initStyle(StageStyle.TRANSPARENT);

//        this.stage.setMaximized(true);
        this.stage.setResizable(false);
//        this.stage.setFullScreen(true);
        this.stage.sizeToScene();

        displayLogIn();
    }

    @Override
    public void start() {
        launch();
    }

    @Override
    public void displayLogIn() {
        VBox loginBox = new VBox();

        // temporary
        Text title = new Text(GAME_NAME);
        title.setFont(getFont(50, ratio));
        loginBox.getChildren().add(title);

        Label nicknameLabel = new Label("Nickname:");
        nicknameLabel.setFont(getFont(50, ratio));
        loginBox.getChildren().add(nicknameLabel);
        TextField nicknameTextField = new TextField();
        nicknameTextField.setFont(getFont(50, ratio));
        loginBox.getChildren().add(nicknameTextField);

        Label serverIPLabel = new Label("Server IP:");
        serverIPLabel.setFont(getFont(50, ratio));
        loginBox.getChildren().add(serverIPLabel);
        TextField serverIPTextField = new TextField();
        serverIPTextField.setFont(getFont(50, ratio));
        loginBox.getChildren().add(serverIPTextField);

        Button connectButton = new Button("Connect");
        connectButton.setFont(getFont(50, ratio));

        connectButton.setOnAction(event -> {

            connectButton.setVisible(false);

            String nickname = nicknameTextField.getText();
            String serverIP = serverIPTextField.getText();

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
            } catch (ServerIsNotReachableException serverIsNotReachableException) {
                serverIsNotReachableException.printStackTrace();
            }

//            Pane pane = addBackground(getPlayingPane(), getScreenWidth(), getScreenHeight(), 1.2f, getWindowWidth());
//            this.stage.setScene(setTransparentBackground(pane));
//            this.stage.setFullScreen(true);
//            this.stage.show();
        });

        loginBox.getChildren().add(connectButton);

        // getDialogStage(loginBox, 1000, 1000, 1.2f, getWindowWidth() / REFERENCE_WIDTH)
        Pane pane = addCoolFrame(loginBox, (int) (1000 * ratio), (int) (1000 * ratio), 1.2f, true, (int) (350 * ratio), ratio);
        this.stage.setScene(setTransparentBackground(pane));
        this.stage.centerOnScreen();
        this.stage.show();
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
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption) {

        VBox vBox = new VBox();

        Text title = new Text(question);
        title.setFont(getFont(50, ratio));
        vBox.getChildren().add(title);

        switch (messageType) {

            case MULTI_OR_SINGLE_PLAYER_MODE:
            case CHOICE_NORMAL_ACTION:
            case CHOICE_LEADER_ACTION:
                buttonOrRadio(vBox, new MessageTypeChoicesDrawer(), messageType, choices, minChoices, maxChoices);
                break;

            case CHOICE_LEADERS:
                buttonOrRadio(vBox, new LeaderCardChoicesDrawer(), messageType, choices, minChoices, maxChoices);
                break;
        }

        if (hasUndoOption) {
            Button undoOptionButton = new Button("UNDO");
            undoOptionButton.setOnAction(actionEvent -> {
                client.sendUndoMessage();
                client.viewNext();
            });
            vBox.getChildren().add(undoOptionButton);
        }

        Pane pane = addCoolFrame(vBox, (int) (1000 * ratio), (int) (1000 * ratio), 1.2f, true, (int) (350 * ratio), ratio);
        this.stage.setScene(setTransparentBackground(pane));
        this.stage.centerOnScreen();
        this.stage.show();
    }

    private void buttonOrRadio(Pane pane, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {
        if (minChoices == 1 && maxChoices == 1) {
            buttonMultipleChoices(pane, choicesDrawer, messageType, choices);
        } else {
            checkBoxMultipleChoices(pane, choicesDrawer, messageType, choices, minChoices, maxChoices);
        }
    }

    private void buttonMultipleChoices(Pane pane, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices) {

        for (int i = 0; i < choices.size(); i++) {

            Button button = choicesDrawer.decorateButton(new Button(), choices.get(i));

            int finalI = i;
            button.setOnAction(event -> {
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

                client.viewNext();

            });
            pane.getChildren().add(button);
        }
    }

    private void checkBoxMultipleChoices(Pane pane, ChoicesDrawer choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {

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
        text1.setFont(getFont(50, ratio));
        vBox.getChildren().add(text1);

        Button confirmationButton = new Button("OK");
        confirmationButton.setFont(getFont(50, ratio));
        vBox.getChildren().add(confirmationButton);

        Stage dialog = getDialog(vBox, (int) (500 * ratio), (int) (500 * ratio), 1.2f, true, (int) (350 * ratio), ratio);
        confirmationButton.setOnAction(actionEvent -> {
            client.viewNext();
            dialog.close();
        });

        dialog.show();
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
