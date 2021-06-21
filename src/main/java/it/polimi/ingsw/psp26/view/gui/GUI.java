package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.client.MessageSynchronizedFIFO;
import it.polimi.ingsw.psp26.view.ViewInterface;
import it.polimi.ingsw.psp26.view.gui.FXMLControllers.LoginController;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.AsynchronousDrawer;
import it.polimi.ingsw.psp26.view.gui.choicesdrawers.*;
import it.polimi.ingsw.psp26.view.gui.loading.WaitingScreen;
import it.polimi.ingsw.psp26.view.gui.maincomponents.GlobalLeaderboardDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.LeaderboardDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.ActionTokenDialogDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.DevelopmentCardsGridDialogDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.MarketDialogDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.WarehousePlacerDrawer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.gui.DialogStage.getDialog;
import static it.polimi.ingsw.psp26.view.gui.FramePane.addBackground;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.addStylesheet;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.*;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.PlayingPane.getPlayingPane;
import static java.lang.Thread.sleep;

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage primaryStage;
    private boolean displayingPlayingPane;

    public GUI() {
        displayingPlayingPane = false;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void setStageWindowProperties(Stage stage) {
        // stage.setMaximized(false);
        stage.setResizable(false);
        // stage.setFullScreen(false);
        // stage.setAlwaysOnTop(false);
        // stage.sizeToScene();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // System.out.println("GUI - MAIN THREAD ID: " + Thread.currentThread().getId());

        client = new Client(this);

        Pane root = addBackground(new Pane(), getWindowWidth(), getWindowHeight());

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

            LoginController loginController = fxmlLoader.getController();
            loginController.addConnectionButtonEvent(dialog, client);

            SoundManager soundManager = SoundManager.getInstance();
            soundManager.setMusic("main_theme_03.wav");

            dialog.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourcesToAdd) {
        Stage dialog = getDialog(
                primaryStage,
                new WarehousePlacerDrawer(client, warehouse, resourcesToAdd, getMinBetweenWindowWidthAndHeight()).draw()
        );
        dialog.show();
    }

    @Override
    public void displayDevelopmentCardBuyAction(DevelopmentCardsGrid developmentCardsGrid, List<Resource> playerResources) {
        Stage dialog = getDialog(
                primaryStage,
                new DevelopmentCardsGridDialogDrawer(client, developmentCardsGrid, getMinBetweenWindowWidthAndHeight()).draw()
        );
        dialog.show();
    }

    @Override
    public void displayMarketAction(MarketTray marketTray, List<Resource> playerResources) {
        Stage dialog = getDialog(
                primaryStage,
                new MarketDialogDrawer(client, marketTray, getMinBetweenWindowWidthAndHeight()).draw()
        );
        dialog.show();
    }

    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption) {

        VBox mainContainer = new VBox(5 * getGeneralRatio());
        Stage dialog = getDialog(primaryStage, mainContainer);

        Label label = new Label(question);
        label.setId("label");
        label.setWrapText(true);
        mainContainer.getChildren().add(label);

        ChoicesDrawer<?> choicesDrawer;
        boolean inScrollPane = false;

        switch (messageType) {

            case MULTI_OR_SINGLE_PLAYER_MODE:
            case CHOICE_NORMAL_ACTION:
            case CHOICE_LEADER_ACTION:
            case NEW_OR_OLD:
            case MENU:
                choicesDrawer = new MessageTypeChoicesDrawer();
                break;

            case CHOICE_LEADERS:
                SoundManager.getInstance().setMusic("main_theme_04.wav");
                inScrollPane = true;
                choicesDrawer = new LeaderCardChoicesDrawer();
                break;

            case CHOICE_RESOURCE_FROM_WAREHOUSE:
            case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:
                choicesDrawer = new ResourceChoicesDrawer();
                break;

            case CHOICE_DEVELOPMENT_CARD_SLOT_POSITION:
                choicesDrawer = new StringChoicesDrawer();
                break;

            case CHOICE_PRODUCTIONS_TO_ACTIVATE:
                inScrollPane = true;
                choicesDrawer = new ProductionChoicesDrawer(client);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + messageType);
        }

        Pane contentPane = new Pane();
        mainContainer.getChildren().add(contentPane);
        buttonOrCheckBox(contentPane, inScrollPane, dialog, choicesDrawer, messageType, choices, minChoices, maxChoices);

        if (!messageType.equals(MessageType.MENU) &&
                !messageType.equals(MessageType.NEW_OR_OLD) &&
                !messageType.equals(MessageType.MULTI_OR_SINGLE_PLAYER_MODE)) {
            Button viewBoardButton = new Button("View Board");
            viewBoardButton.setOnAction(actionEvent -> {
                SoundManager.getInstance().setSoundEffect("button_click_01.wav");
                dialog.close();
                new AsynchronousDrawer(
                        () -> sleep(5000),
                        () -> client.reHandleLastMessage(),
                        false
                ).start();
            });
            mainContainer.getChildren().add(viewBoardButton);
        }

        if (hasUndoOption) {
            Button undoOptionButton = new Button("Undo");
            undoOptionButton.setId("undo-button");
            undoOptionButton.setOnAction(actionEvent -> {
                SoundManager.getInstance().setSoundEffect("button_click_01.wav");
                dialog.close();
                client.sendUndoMessage();
                client.viewNext();
            });
            mainContainer.getChildren().add(undoOptionButton);
        }

        dialog.show();
    }

    private ScrollPane getScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-border-color: transparent;");
        scrollPane.setPannable(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMaxHeight(getMinBetweenWindowWidthAndHeight() * 0.7);
        scrollPane.setMaxWidth(getMinBetweenWindowWidthAndHeight() * 0.7);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHvalue(0.5);
        return scrollPane;
    }

    private void buttonOrCheckBox(Pane container, boolean inScrollPane, Stage dialog, ChoicesDrawer<?> choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {
        if (minChoices == 1 && maxChoices == 1) {
            buttonMultipleChoices(container, inScrollPane, dialog, choicesDrawer, messageType, choices);
        } else {
            checkBoxMultipleChoices(container, inScrollPane, dialog, choicesDrawer, messageType, choices, minChoices, maxChoices);
        }
    }

    private void buttonMultipleChoices(Pane container, boolean inScrollPane, Stage dialog, ChoicesDrawer<?> choicesDrawer, MessageType messageType, List<Object> choices) {
        VBox vBox = new VBox(0);
        container.getChildren().add(vBox);

        List<ButtonContainer<?>> buttonContainers = new ArrayList<>();

        for (int i = 0; i < choices.size(); i++) {

            @SuppressWarnings({"rawtypes", "unchecked"})
            ButtonContainer<?> buttonContainer = choicesDrawer.decorateButtonContainer(new ButtonContainer(choices.get(i)));
            buttonContainers.add(buttonContainer);

            buttonContainer.setOnAction(event -> {
                // disabling all buttons
                for (Button button1 : buttonContainers) button1.setDisable(true);
                SoundManager.getInstance().setSoundEffect("button_click_01.wav");
                try {
                    client.notifyObservers(new Message(messageType, buttonContainer.getContainedObject()));
                } catch (InvalidPayloadException ignored) {
                }

                dialog.close();
                client.viewNext();

            });

            setScrollPaneOrGrid(inScrollPane, vBox, i, buttonContainer);
        }
    }

    private void checkBoxMultipleChoices(Pane container, boolean inScrollPane, Stage dialog, ChoicesDrawer<?> choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {

        VBox vBox = new VBox(0);
        container.getChildren().add(vBox);

        List<ButtonContainer<?>> buttonContainers = new ArrayList<>();

        for (int i = 0; i < choices.size(); i++) {

            @SuppressWarnings({"rawtypes", "unchecked"})
            ButtonContainer<?> buttonContainer = choicesDrawer.decorateButtonContainer(new ButtonContainer(choices.get(i)));
            buttonContainers.add(buttonContainer);

            setScrollPaneOrGrid(inScrollPane, vBox, i, buttonContainer);
        }

        Text errorText = new Text("Select " + minChoices + " items." + ((maxChoices > minChoices) ? " Up to " + maxChoices + " items." : ""));
        errorText.setId("error");
        errorText.setVisible(false);
        Button confirmationButton = new Button("Confirm");
        confirmationButton.setId("confirm-button");

        confirmationButton.setOnAction(event -> {
            List<Object> selected = new ArrayList<>();
            for (ButtonContainer<?> buttonContainer : buttonContainers)
                if (buttonContainer.isClicked()) {
                    SoundManager.getInstance().setSoundEffect("button_click_01.wav");
                    selected.add(buttonContainer.getContainedObject());
                }

            if (selected.size() >= minChoices && selected.size() <= maxChoices) {
                try {
                    client.notifyObservers(new Message(messageType, selected.toArray()));
                    dialog.close();
                    client.viewNext();
                } catch (InvalidPayloadException ignored) {
                }
            } else {
                errorText.setVisible(true);
            }
        });

        vBox.getChildren().add(errorText);
        vBox.getChildren().add(confirmationButton);
    }

    private void setScrollPaneOrGrid(boolean inScrollPane, VBox vBox, int i, ButtonContainer<?> buttonContainer) {
        if (!inScrollPane) {
            if (i % 2 == 0) {
                HBox hBox = new HBox(5 * getGeneralRatio());
                hBox.getChildren().add(buttonContainer);
                vBox.getChildren().add(hBox);
            } else {
                ((HBox) vBox.getChildren().get(vBox.getChildren().size() - 1)).getChildren().add(buttonContainer);
            }
        } else {
            if (i == 0) {
                HBox hBox = new HBox(5 * getGeneralRatio());
                hBox.getChildren().add(buttonContainer);
                ScrollPane scrollPane = getScrollPane();
                scrollPane.setContent(hBox);
                vBox.getChildren().add(scrollPane);
            } else {
                ((HBox) ((ScrollPane) vBox.getChildren().get(0)).getContent()).getChildren().add(buttonContainer);
            }
        }
    }

    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {
        Stage dialog = getDialog(
                primaryStage,
                new ActionTokenDialogDrawer(client, unusedTokens, getMinBetweenWindowWidthAndHeight()).draw()
        );
        dialog.show();
    }

    @Override
    public void displayGlobalLeaderboard() {
        Stage dialog = getDialog(primaryStage, new GlobalLeaderboardDrawer(client, getMinBetweenWindowWidthAndHeight()).draw());
        dialog.show();
    }

    public void displayTextDialog(String text, String textId) {
        VBox vBox = new VBox(20 * getGeneralRatio());

        Text text1 = new Text(text);
        text1.setId(textId);
        text1.setWrappingWidth(1000 * getGeneralRatio());
        vBox.getChildren().add(text1);

        Button confirmationButton = new Button("OK");
        confirmationButton.setId("confirm-button");
        vBox.getChildren().add(confirmationButton);

        Stage dialog = getDialog(primaryStage, vBox);
        confirmationButton.setOnAction(actionEvent -> {
            SoundManager soundManager = SoundManager.getInstance();
            soundManager.setSoundEffect("button_click_03.wav");
            dialog.close();
            client.viewNext();
        });

        dialog.show();
    }

    @Override
    public void displayText(String text) {
        displayTextDialog(text, "text-field");
    }

    @Override
    public void displayEndGame(Map<String, Integer> leaderboard, String winningPlayer) {
        LeaderboardDrawer leaderboardDrawer = new LeaderboardDrawer(
                client, leaderboard, winningPlayer, getMinBetweenWindowWidthAndHeight()
        );
        Stage dialog = getDialog(primaryStage, leaderboardDrawer.draw());
        dialog.show();
    }

    @Override
    public void displayError(String error) {
        displayTextDialog(error, "error");
    }

    @Override
    public void displayWaitingScreen(Message message) {
        try {
            new WaitingScreen(
                    primaryStage,
                    () -> {
                        Message stopMessage = MessageSynchronizedFIFO.getInstance().getNext();
                        while (!stopMessage.getMessageType().equals(MessageType.STOP_WAITING))
                            stopMessage = MessageSynchronizedFIFO.getInstance().getNext();
                    },
                    this::stopDisplayingWaitingScreen,
                    (String) message.getPayload()
            ).start();
        } catch (EmptyPayloadException ignored) {
        }

    }

    @Override
    public void stopDisplayingWaitingScreen() {
        if (!displayingPlayingPane) {
            Pane pane = addBackground(
                    getPlayingPane(primaryStage, client, getWindowWidth() - 100),
                    getWindowWidth(),
                    getWindowHeight()
            );
            // primaryStage.hide();
            primaryStage.getScene().setRoot(pane);
            primaryStage.show();

            displayingPlayingPane = true;
        }

        client.viewNext();
    }

    @Override
    public void waitForYourTurn() {
        new AsynchronousDrawer(
                () -> MessageSynchronizedFIFO.getInstance().lookingForNext(),
                () -> client.viewNext(),
                false
        ).start();
    }

}
