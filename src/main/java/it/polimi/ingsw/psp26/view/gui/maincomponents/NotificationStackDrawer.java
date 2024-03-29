package it.polimi.ingsw.psp26.view.gui.maincomponents;


import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Class to draw the notification stack.
 */
public class NotificationStackDrawer extends RatioDrawer {

    // The notifications displayed on screen
    private final List<Text> notificationsToShow;

    // The updated notifications received from the Server
    private List<String> receivedNotifications;

    /**
     * Class constructor.
     *
     * @param maxWidth max width that can be used to draw the stack
     */
    public NotificationStackDrawer(int maxWidth) {
        super(maxWidth);
        notificationsToShow = new ArrayList<>();
        receivedNotifications = new ArrayList<>();
    }

    /**
     * Method that returns the pane containing the stack.
     * Messages in the stack are initialized as empty text fields.
     * They will be updated
     *
     * @return border pane containing the stack
     */
    @Override
    public Pane draw() {

        BorderPane borderPane = new BorderPane();

        // Making title of the NotificationsStack
        Text notificationTitle = new Text("Notification Stack");
        notificationTitle.setId("title");
        notificationTitle.setStyle("-fx-font-size: " + 500 * ratio + ";");
        //notificationTitle.setWrappingWidth(initMaxWidth * ratio);

        // Adding title at the top
        borderPane.setTop(notificationTitle);


        // Creating the VBox containing the notifications
        VBox notificationBox = new VBox(400 * ratio);

        // Making text elements of the NotificationsStack
        int notificationStackSize = 10;
        for (int i = 0; i < notificationStackSize; i++) {
            Text text = new Text("");
            text.setId("notification-text");
            text.setStyle("-fx-font-size: " + 350 * ratio + ";");
            text.setWrappingWidth(initMaxWidth - 350 * ratio);

            notificationsToShow.add(text);
            notificationBox.getChildren().add(text);
        }


        // Creating a scrollable version of the NotificationStack
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxHeight(initMaxWidth * 4.5);
        scrollPane.setMaxWidth(initMaxWidth);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHvalue(0.5); // Centering the scrollPane x position
        scrollPane.setContent(notificationBox);

        // Adding scrollPane at the center
        borderPane.setCenter(scrollPane);

        // Setting panes margins
        BorderPane.setMargin(scrollPane, new Insets(20 * ratio));

        return borderPane;
    }


    /**
     * Method to display the new notifications by changing the text in the existing Text fields.
     */
    public synchronized void displayNotifications() {
        for (int i = 0; i < receivedNotifications.size(); i++) {
            notificationsToShow.get(i).setText(receivedNotifications.get(receivedNotifications.size() - 1 - i));
        }
    }


    /**
     * Setter of the received notifications.
     *
     * @param receivedNotifications list of notifications
     */
    public synchronized void setReceivedNotifications(List<String> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }

}
