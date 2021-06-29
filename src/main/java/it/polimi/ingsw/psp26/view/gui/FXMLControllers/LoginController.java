package it.polimi.ingsw.psp26.view.gui.FXMLControllers;

import it.polimi.ingsw.psp26.network.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Class that controls the login.fxml file
 */
public class LoginController {

    @FXML
    private Button connectionButton;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField serverIPTextField;


    /**
     * Method that sets the action of the connectionButton of the Login FXML file.
     * When the button is clicked, the method closes the current stage and gets the text the Player entered in the
     * TextFields and uses it to call the initializeNetworkHandler() method.
     * Once this is finished, calls the viewNext() method.
     *
     * @param stage  the Stage to close
     * @param client the client that will call the initializeNetworkHandler() and viewNext() methods
     */
    @FXML
    public void addConnectionButtonEvent(Stage stage, Client client) {
        connectionButton.setOnAction(event -> {

            connectionButton.setDisable(true);

            String nickname = nicknameTextField.getText();
            String password = passwordTextField.getText();
            String serverIP = serverIPTextField.getText();

            stage.close();

            client.initializeNetworkHandler(nickname, password, serverIP);
            client.viewNext();

        });
    }

}
