package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.DesiredVirtualViewDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.network.server.MessageUtils.lookingForMessage;

public class VirtualViewAssignment extends Thread {

    private final String sessionToken;
    private final NetworkNode clientNode;

    public VirtualViewAssignment(String sessionToken, NetworkNode clientNode) {
        this.sessionToken = sessionToken;
        this.clientNode = clientNode;
    }

    @Override
    public void run() {

        try {

            System.out.println("VirtualViewAssignment - New match or recover an old?");
            newOrOldMatchSetup(clientNode, sessionToken);

        } catch (IOException | EmptyPayloadException | InvalidPayloadException | ClassNotFoundException ignored) {
        }
    }


    private void newOrOldMatchSetup(NetworkNode clientNode, String sessionToken) throws InvalidPayloadException, IOException, ClassNotFoundException, EmptyPayloadException {
        // step: checking if there is a suspended match:
        if (isVirtualViewToRecoverMatch(sessionToken)) {
            System.out.println("VirtualViewAssignment - There is a suspended match, asking to recover.");

            clientNode.sendData(
                    new MultipleChoicesMessage(
                            sessionToken,
                            MessageType.NEW_OR_OLD,
                            "Would you like to start a new match,\nor you want to recover a suspended one?",
                            1, 1,
                            false,
                            MessageType.NEW_MATCH, MessageType.RECOVERY_MATCH));

            System.out.println("VirtualViewAssignment - Waiting response.");
            SessionMessage message = lookingForMessage(clientNode, NEW_OR_OLD);

            System.out.println("VirtualViewAssignment - Response received.");
            if (message.getPayload().equals(MessageType.NEW_MATCH)) {
                // TODO: We shall communicate to other users that the match will be suspended for ever!
                System.out.println("VirtualViewAssignment - New match selected.");
                // asking for a new match
                matchModeRequests(clientNode, sessionToken);
            } else {
                System.out.println("VirtualViewAssignment - Recovery match has been selected.");
                // recovery of the match
                assignNodeToExistingVirtualViewToRecoveryMatch(sessionToken, clientNode);
            }

        } else {
            System.out.println("VirtualViewAssignment - Asking the playing mode (multi or single player?).");
            // asking for a new match
            matchModeRequests(clientNode, sessionToken);
        }
    }

    private void matchModeRequests(NetworkNode clientNode, String sessionToken) throws InvalidPayloadException, IOException, ClassNotFoundException, EmptyPayloadException {
        System.out.println("VirtualViewAssignment - Sending request mode.");

        clientNode.sendData(
                new MultipleChoicesMessage(
                        sessionToken,
                        MessageType.MULTI_OR_SINGLE_PLAYER_MODE,
                        "Choose the playing mode:",
                        1, 1,
                        false,
                        SINGLE_PLAYER_MODE, TWO_PLAYERS_MODE, THREE_PLAYERS_MODE, FOUR_PLAYERS_MODE));

        System.out.println("VirtualViewAssignment - Waiting response.");
        SessionMessage message = lookingForMessage(clientNode, MULTI_OR_SINGLE_PLAYER_MODE);
        System.out.println("VirtualViewAssignment - Response has been received.");

        handlePlayerModeMessage(clientNode, message);
    }


    /**
     * Method to handle the assignment of the new network node to a virtual view.
     * Based on the playing mode selected, it checks if a match is waiting for a player.
     * If so, it assigns the node to the virtual view,
     * otherwise a new virtual view will be instantiated and the node will be assigned to it.
     *
     * @param clientNode network node of the new player
     * @param message    message containing as payload the selected playing mode
     * @throws EmptyPayloadException if message payload is empty
     */
    private void handlePlayerModeMessage(NetworkNode clientNode, SessionMessage message) throws EmptyPayloadException {
        if (message.getMessageType().equals(MessageType.MULTI_OR_SINGLE_PLAYER_MODE)) {
            System.out.println("VirtualViewAssignment - Playing mode: " + message.getPayload());

            switch ((MessageType) message.getPayload()) {

                case SINGLE_PLAYER_MODE:
                    assignNodeToVirtualView(1, message.getSessionToken(), clientNode);
                    break;

                case TWO_PLAYERS_MODE:
                    assignNodeToVirtualView(2, message.getSessionToken(), clientNode);
                    break;

                case THREE_PLAYERS_MODE:
                    assignNodeToVirtualView(3, message.getSessionToken(), clientNode);
                    break;

                case FOUR_PLAYERS_MODE:
                    assignNodeToVirtualView(4, message.getSessionToken(), clientNode);
                    break;

            }
        }
    }

    /**
     * Method to assign a network node to a virtual view.
     * It tries to assign the node to an already existing one (that is waiting for a player to start the match).
     * If no virtual view is free, with the requested characteristics, the node will be assigned to a new virtual view.
     *
     * @param maxNumberOfPlayers number of players requested by the player
     * @param sessionToken       session token of the new player
     * @param clientNode         network node client of the new player
     */
    private void assignNodeToVirtualView(int maxNumberOfPlayers, String sessionToken, NetworkNode clientNode) {
        try {
            assignNodeToExistingVirtualView(maxNumberOfPlayers, sessionToken, clientNode);
        } catch (DesiredVirtualViewDoesNotExistException e) {
            assignNodeToNewVirtualView(maxNumberOfPlayers, sessionToken, clientNode);
        }
    }

    /**
     * Method to assign a network node to an already existing virtual view.
     * It checks among all the active virtual views if there is one that is waiting for a player
     * and with the requested number of players.
     * If there is, the node will be added, otherwise an exception will be thrown.
     *
     * @param maxNumberOfPlayers number of requested players for the match
     * @param sessionToken       session token of the new player
     * @param clientNode         network node client of the new player
     * @throws DesiredVirtualViewDoesNotExistException if virtual view is not waiting with the requested characteristics
     */
    private void assignNodeToExistingVirtualView(int maxNumberOfPlayers, String sessionToken, NetworkNode clientNode) throws DesiredVirtualViewDoesNotExistException {
        boolean assigned = false;
        for (VirtualView virtualView : Server.getInstance().getVirtualViews()) {
            if (virtualView.getMatchController().isWaitingForPlayers() &&
                    virtualView.getMatchController().getMaxNumberOfPlayers() == maxNumberOfPlayers) {
                virtualView.addNetworkNodeClient(sessionToken, clientNode);
                assigned = true;
                break;
            }
        }
        if (!assigned) throw new DesiredVirtualViewDoesNotExistException();
    }

    /**
     * Method to assign the network node to a new virtual view.
     * It creates a new virtual view, and assigns the node to it.
     *
     * @param maxNumberOfPlayers number of requested players for the match
     * @param sessionToken       session token of te new player
     * @param clientNode         network node client of the new player
     */
    private void assignNodeToNewVirtualView(int maxNumberOfPlayers, String sessionToken, NetworkNode clientNode) {
        VirtualView virtualView = new VirtualView();
        // set the desired number of players
        virtualView.getMatchController().setMaxNumberOfPlayers(maxNumberOfPlayers);
        // add the node to the virtual view
        virtualView.addNetworkNodeClient(sessionToken, clientNode);
        // add virtual view to the list
        Server.getInstance().addVirtualView(virtualView);
    }

    private boolean isVirtualViewToRecoverMatch(String sessionToken) {
        for (VirtualView virtualView : Server.getInstance().getVirtualViews()) {
            for (Player player : virtualView.getMatchController().getMatch().getPlayers()) {
                if (player.getSessionToken().equals(sessionToken))
                    return true;
            }
        }
        return false;
    }

    private void assignNodeToExistingVirtualViewToRecoveryMatch(String sessionToken, NetworkNode clientNode) {
        for (VirtualView virtualView : Server.getInstance().getVirtualViews()) {
            for (Player player : virtualView.getMatchController().getMatch().getPlayers()) {
                if (player.getSessionToken().equals(sessionToken))
                    virtualView.addNetworkNodeClient(sessionToken, clientNode);
            }
        }
    }
}
