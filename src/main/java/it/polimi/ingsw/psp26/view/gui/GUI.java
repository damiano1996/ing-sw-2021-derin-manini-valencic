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
import it.polimi.ingsw.psp26.network.server.memory.LeaderBoard;
import it.polimi.ingsw.psp26.view.ViewInterface;
import it.polimi.ingsw.psp26.view.gui.FXMLControllers.LoginController;
import it.polimi.ingsw.psp26.view.gui.asynchronousjobs.AsynchronousDrawer;
import it.polimi.ingsw.psp26.view.gui.choicesdrawers.*;
import it.polimi.ingsw.psp26.view.gui.loading.WaitingScreen;
import it.polimi.ingsw.psp26.view.gui.maincomponents.LeaderboardDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.PlayingPane;
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

/**
 * Graphical User Interface implementation.
 */
public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage primaryStage;
    private boolean displayingPlayingPane;

    /**
     * Class constructor.
     */
    public GUI() {
        displayingPlayingPane = false;
    }

    /**
     * Main method that launch the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Method used to set general window properties.
     *
     * @param stage target of the properties
     */
    private void setStageWindowProperties(Stage stage) {
        // stage.setMaximized(false);
        stage.setResizable(false);
        // stage.setFullScreen(false);
        // stage.setAlwaysOnTop(false);
        // stage.sizeToScene();
    }

    /**
     * Method to define what stage has to do on close.
     * It has to communicate to client that the window has been closed.
     *
     * @param stage target of the close property
     */
    private void setOnCloseEvent(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> client.close());
    }

    /**
     * Method to setup the primary stage.
     * It draw a background and sets the properties of the window.
     * Then it call the login form.
     *
     * @param primaryStage primary stage of the application
     * @throws Exception if unable to setup the primary stage (see javaFX documentations)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        setOnCloseEvent(primaryStage);

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

    /**
     * Method to start the primary stage.
     */
    @Override
    public void start() {
        launch();
    }

    /**
     * Method to display the dialog containing the login form.
     * The form requests the nickname, the password of the user and the server IP.
     * There is a connection button to connect the client to the server.
     */
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
            soundManager.setMusic(SoundManager.INTRO_SONG);

            dialog.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to create a dialog that shows the warehouse with the resources to add.
     *
     * @param warehouse      warehouse object of the player
     * @param resourcesToAdd list of resources received by the player that must be added
     */
    @Override
    public void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourcesToAdd) {
        Stage dialog = getDialog(
                primaryStage,
                new WarehousePlacerDrawer(client, warehouse, resourcesToAdd, getMinBetweenWindowWidthAndHeight()).draw()
        );
        dialog.show();
    }

    /**
     * Method that creates a dialog containing the grid of development cards.
     *
     * @param developmentCardsGrid object modelling the grid with the development cards
     * @param playerResources      the resources of the player
     */
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

    /**
     * Method used to display multiple choices.
     * It creates a dialog containing the admissible choices.
     * They are drawn following a strategy design pattern:
     * for each type of object, the corresponding strategy is selected.
     *
     * @param messageType   type of the message in which choices were present
     * @param question      question to display to player
     * @param choices       list of admissible choices
     * @param minChoices    minimum number of items that must be selected
     * @param maxChoices    maximum number of items that can be selected
     * @param hasUndoOption if true, implementations should develop a way to allow players to communicate to the server the will
     */
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
            case NEW_OR_OLD:
                SoundManager.getInstance().setMusic(SoundManager.MAIN_THEME_SONG);
            case CHOICE_NORMAL_ACTION:
            case CHOICE_LEADER_ACTION:
            case MENU:
                choicesDrawer = new MessageTypeChoicesDrawer();
                break;

            case CHOICE_LEADERS:
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
                SoundManager.getInstance().setSoundEffect(SoundManager.DIALOG_SOUND);
                PlayingPane.getInstance().showResumeButton();
                dialog.close();
            });
            mainContainer.getChildren().add(viewBoardButton);
        }

        if (hasUndoOption) {
            Button undoOptionButton = new Button("Undo");
            undoOptionButton.setId("undo-button");
            undoOptionButton.setOnAction(actionEvent -> {
                SoundManager.getInstance().setSoundEffect(SoundManager.DIALOG_SOUND);
                dialog.close();
                client.sendUndoMessage();
                client.viewNext();
            });
            mainContainer.getChildren().add(undoOptionButton);
        }

        dialog.show();
    }

    /**
     * Method to create a scroll pane object.
     *
     * @return scroll pane
     */
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

    /**
     * Method to choose if to draw buttons or a radio style selection for the available choices.
     * If the cardinality of the items to select is equal to one, then it will draw buttons
     * otherwise, it will draw buttons that are grouped and more than one will be selectable.
     *
     * @param container     pane that contains the items
     * @param inScrollPane  if items must be drawn in a scroll pane
     * @param dialog        stage containing the items
     * @param choicesDrawer strategy that must used to draw items
     * @param messageType   message type received with the items
     * @param choices       available items
     * @param minChoices    minimum number of items to select
     * @param maxChoices    maximum number of items to select
     */
    private void buttonOrCheckBox(Pane container, boolean inScrollPane, Stage dialog, ChoicesDrawer<?> choicesDrawer, MessageType messageType, List<Object> choices, int minChoices, int maxChoices) {
        if (minChoices == 1 && maxChoices == 1) {
            buttonMultipleChoices(container, inScrollPane, dialog, choicesDrawer, messageType, choices);
        } else {
            checkBoxMultipleChoices(container, inScrollPane, dialog, choicesDrawer, messageType, choices, minChoices, maxChoices);
        }
    }

    /**
     * Method to draw buttons that if clicked they send the selected item to the server.
     *
     * @param container     pane that contains the items
     * @param inScrollPane  if items must be drawn in a scroll pane
     * @param dialog        stage containing the items
     * @param choicesDrawer strategy that must used to draw items
     * @param messageType   message type received with the items
     * @param choices       available items
     */
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
                SoundManager.getInstance().setSoundEffect(SoundManager.DIALOG_SOUND);
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

    /**
     * Method to draw grouped buttons. More than one can be selected.
     * Selections will be sent to the server after having clicked on the confirmation button
     * that will be drawn in the dialog.
     *
     * @param container     pane that contains the items
     * @param inScrollPane  if items must be drawn in a scroll pane
     * @param dialog        stage containing the items
     * @param choicesDrawer strategy that must used to draw items
     * @param messageType   message type received with the items
     * @param choices       available items
     * @param minChoices    minimum number of items to select
     * @param maxChoices    maximum number of items to select
     */
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
                    SoundManager.getInstance().setSoundEffect(SoundManager.DIALOG_SOUND);
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

    /**
     * Method to draw a scroll pane or a grid.
     * It will be used to draw buttons of the multiple choice dialog.
     *
     * @param inScrollPane    if container is a scroll pane
     * @param vBox            vertical box containing the components
     * @param i               index of the component
     * @param buttonContainer button container to add to the pane
     */
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

    /**
     * Method to create and show a dialog containing the tokens.
     *
     * @param unusedTokens list of tokens
     */
    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {
        Stage dialog = getDialog(
                primaryStage,
                new ActionTokenDialogDrawer(client, unusedTokens, getMinBetweenWindowWidthAndHeight()).draw()
        );
        dialog.show();
    }

    /**
     * Method to create and to show the dialog containing the global leaderboard.
     *
     * @param leaderBoard leaderboard object
     */
    @Override
    public void displayGlobalLeaderboard(LeaderBoard leaderBoard) {
        Stage dialog = getDialog(primaryStage, new LeaderboardDrawer(
                client, leaderBoard.getLeaderboard(), "", getMinBetweenWindowWidthAndHeight()
        ).draw());
        dialog.show();
    }

    /**
     * Method to create and display a dialog containing a general text message.
     *
     * @param text   message to show
     * @param textId identifier of the text component
     */
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
            soundManager.setSoundEffect(SoundManager.DIALOG_SOUND_TWO);
            dialog.close();
            client.viewNext();
        });

        dialog.show();
    }

    /**
     * Method to display the dialog containing a text.
     *
     * @param text string to show
     */
    @Override
    public void displayText(String text) {
        displayTextDialog(text, "text-field");
    }

    /**
     * Method to create and to show a dialog containing the end game view.
     * It shows the leaderboard with nicknames and points of the players of the match.
     *
     * @param leaderboard   map with player - points association
     * @param winningPlayer username of the player that has won
     */
    @Override
    public void displayEndGame(Map<String, Integer> leaderboard, String winningPlayer) {
        LeaderboardDrawer leaderboardDrawer = new LeaderboardDrawer(
                client, leaderboard, winningPlayer, getMinBetweenWindowWidthAndHeight()
        );
        Stage dialog = getDialog(primaryStage, leaderboardDrawer.draw());
        dialog.show();
    }

    /**
     * Method to display the error message in a dialog.
     *
     * @param error string to display
     */
    @Override
    public void displayError(String error) {
        displayTextDialog(error, "error");
    }

    /**
     * Method to start to display the waiting screen.
     * It listens for the stop waiting message.
     * When it will arrive, it will execute the stopWaitingScreen method.
     *
     * @param message message to display with the waiting screen
     */
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

    /**
     * Method to show the playing pane on waiting screen stop, only if it is not shown yet.
     * Then calls for the next message from the client.
     */
    @Override
    public void stopDisplayingWaitingScreen() {
        if (!displayingPlayingPane) {
            drawPlayingPaneOnPrimaryStage();
            displayingPlayingPane = true;
        }

        client.viewNext();
    }

    /**
     * Method to start a background thread to wait for player turn.
     * In the while, no dialog are shown.
     */
    @Override
    public void waitForYourTurn() {
        new AsynchronousDrawer(
                () -> MessageSynchronizedFIFO.getInstance().lookingForNext(),
                () -> client.viewNext(),
                false
        ).start();
    }

    /**
     * Method to reset the primary stage.
     */
    @Override
    public void reset() {
        PlayingPane.getInstance().stopAsynchronousDrawers();

        displayingPlayingPane = false;

        Pane root = addBackground(new Pane(), getWindowWidth(), getWindowHeight());
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    /**
     * Method to draw the playing pane containing the main components of the game.
     */
    private void drawPlayingPaneOnPrimaryStage() {
        Pane pane = addBackground(
                PlayingPane.getInstance().getPlayingPane(primaryStage,
                        client,
                        getWindowWidth() - 100),
                getWindowWidth(),
                getWindowHeight()
        );
        // primaryStage.hide();
        primaryStage.getScene().setRoot(pane);
        primaryStage.show();
    }

}
