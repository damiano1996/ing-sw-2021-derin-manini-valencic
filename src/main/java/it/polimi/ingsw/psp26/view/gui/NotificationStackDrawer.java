package it.polimi.ingsw.psp26.view.gui;


import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class NotificationStackDrawer {

    // The notifications displayed on screen
    private final List<Text> notificationsToShow;

    // The updated notifications received from the Server
    private List<String> receivedNotifications;

    public NotificationStackDrawer() {
        notificationsToShow = new ArrayList<>();
        receivedNotifications = new ArrayList<>();
    }


    /**
     * Creates a Notifications Stack
     *
     * @param width The width of the stack to create
     * @return The so created Notification Stack
     */
    public BorderPane getNotificationBox(int width) {
        width = width - 40;
        BorderPane borderPane = new BorderPane();


        // Making title of the NotificationsStack
        Text notificationTitle = new Text("Notification Stack");
        notificationTitle.setId("title");
        
        //TODO messo perchè altrimenti nel mio schermo sfora le dimensioni
        notificationTitle.setWrappingWidth(width);

        // Adding title at the top
        borderPane.setTop(notificationTitle);


        // Creating the VBox containing the notifications
        VBox notificationBox = new VBox();
        notificationBox.setSpacing(20);

        // Making text elements of the NotificationsStack
        int notificationStackSize = 10;
        for (int i = 0; i < notificationStackSize; i++) {
            Text text = new Text("");
            text.setId("notification-text");
            text.setWrappingWidth(width);

            notificationsToShow.add(text);
            notificationBox.getChildren().add(text);
        }


        // Creating a scrollable version of the NotificationStack
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxHeight(800);
        scrollPane.setMaxWidth(width + 40);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHvalue(0.5); // Centering the scrollPane x position
        scrollPane.setContent(notificationBox);

        // Adding scrollPane at the center
        borderPane.setCenter(scrollPane);
        
        // Setting panes margins
        BorderPane.setMargin(scrollPane, new Insets(20));

        return borderPane;
    }


    /**
     * Displays the new notifications by changing the text in the existing Text fields
     */
    public synchronized void displayNotifications() {
        for (int i = 0; i < receivedNotifications.size(); i++) {
            notificationsToShow.get(i).setText(receivedNotifications.get(receivedNotifications.size() - 1 - i));
        }
    }


    /**
     * Saves the updated notifications
     *
     * @param receivedNotifications The updated notifications to save
     */
    public synchronized void setReceivedNotifications(List<String> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }

}
