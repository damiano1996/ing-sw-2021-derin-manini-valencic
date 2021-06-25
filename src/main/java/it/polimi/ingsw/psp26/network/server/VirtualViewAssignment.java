package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.DesiredVirtualViewDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.SpecialToken;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.network.server.MessageUtils.lookingForMessage;

/**
 * Class that assigns Clients to existing or new VirtualViews.
 */
public class VirtualViewAssignment extends Thread {

    private final String sessionToken;
    private final NetworkNode clientNode;

    /**
     * Constructor of the class.
     * It sets the sessionToken and the clientNode attributes.
     *
     * @param sessionToken the sessionToken of the Client that has to recover a Match
     * @param clientNode   the NetworkNode of the Client that has to recover a Match
     */
    public VirtualViewAssignment(String sessionToken, NetworkNode clientNode) {
        this.sessionToken = sessionToken;
        this.clientNode = clientNode;
    }


    /**
     * Starts the newOrOldMatchSetup() method to see if the Client can recover a Match or not.
     */
    @Override
    public void run() {
        try {

            System.out.println("VirtualViewAssignment - New match or recover an old?");
            newOrOldMatchSetup(clientNode, sessionToken);

        } catch (IOException | EmptyPayloadException | InvalidPayloadException | ClassNotFoundException ignored) {
        }
    }


    /**
     * Method that checks if there is a suspended Match and if this is the case, sends a message to the Client asking
     * if it wants to recover it or create a new one.
     * If the Client decides to recover the Match, it is assigned to an existing VirtualView.
     * If the Client decides to start a new Match, the NoRecoverySelected() method is called.
     * If there are no Match to recover t all, the method sends to the Client a Message asking in which Match mode it
     * wants to play (a new Match will be created this way).
     *
     * @param clientNode   the NetworkNode of the client that has to be assigned to a VirtualView
     * @param sessionToken the sessionToken of the client that has to be assigned to a VirtualView
     * @throws InvalidPayloadException the payload can't be serialized
     * @throws IOException             if error in IO socket communication
     * @throws ClassNotFoundException  if object class not found
     * @throws EmptyPayloadException   the payload is empty
     */
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
                System.out.println("VirtualViewAssignment - Going to close other matches.");

                new Thread(() -> noRecoverySelected(sessionToken)).start();

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


    /**
     * Method that asks the Client which match mode it wants to play.
     *
     * @param clientNode   the NetworkNode of the client that has to be assigned to a VirtualView
     * @param sessionToken the sessionToken of the client that has to be assigned to a VirtualView
     * @throws InvalidPayloadException the payload can't be serialized
     * @throws IOException             if error in IO socket communication
     * @throws ClassNotFoundException  if object class not found
     * @throws EmptyPayloadException   the payload is empty
     */
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
     * @param sessionToken       session token of the new player
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


    /**
     * Checks if the VirtualView was of an old saved match of the Client.
     *
     * @param sessionToken the sessionToken of the client that has to be assigned to a VirtualView
     * @return true if the VirtualView already exists, false otherwise
     */
    private boolean isVirtualViewToRecoverMatch(String sessionToken) {
        for (VirtualView virtualView : Server.getInstance().getVirtualViews()) {
            for (Player player : virtualView.getMatchController().getMatch().getPlayers()) {
                if (player.getSessionToken().equals(sessionToken))
                    return true;
            }
        }
        return false;
    }


    /**
     * Assigns a Client to the corresponding existing VirtualView.
     *
     * @param sessionToken the sessionToken of the Client that has to recover a Match
     * @param clientNode   the NetworkNode of the Client that has to recover a Match
     */
    private void assignNodeToExistingVirtualViewToRecoveryMatch(String sessionToken, NetworkNode clientNode) {
        for (VirtualView virtualView : Server.getInstance().getVirtualViews()) {
            for (Player player : virtualView.getMatchController().getMatch().getPlayers()) {
                if (player.getSessionToken().equals(sessionToken)) {
                    virtualView.addNetworkNodeClient(sessionToken, clientNode);
                }
            }
        }
    }


    /**
     * Method to close virtual views (matches) that the player left un-completed.
     * It handles the two cases:
     * <p>
     * 1. Client lost connection during match: there are other players that are playing and waiting the client,
     * this method notifies them about the end of the match.
     * <p>
     * 2. The match has been recovered, but no client is in the virtual view, so it can directly delete the match file
     * and remove the virtual view from the virtual views handled by the server.
     *
     * @param sessionToken session token of the player that joins after termination or disconnection
     */
    private synchronized void noRecoverySelected(String sessionToken) {
        List<VirtualView> virtualViewsToRemove = new ArrayList<>();
        List<VirtualView> serverVirtualViews = new ArrayList<>(Server.getInstance().getVirtualViews());

        for (VirtualView virtualView : serverVirtualViews) {
            try {
                virtualView.getMatchController().getMatch().getPlayerBySessionToken(sessionToken);
                // if player was in this virtual view we have to close this match
                // case 1: In the virtual view there are other players waiting for recovery
                if (virtualView.getNumberOfNodeClients() > 0) {

                    // Sending the indefinite suspension message to match controller
                    virtualView.notifyObservers(
                            new SessionMessage(
                                    SpecialToken.BROADCAST.getToken(),
                                    INDEFINITE_SUSPENSION,
                                    sessionToken
                            )
                    );

                } else {
                    // case 2: the match can be deleted from directory.
                    // (case of server and client closed: no network nodes are in the virtual view)
                    GameSaver.getInstance().deleteDirectoryByMatchId(virtualView.getMatchController().getMatch().getId());
                    virtualViewsToRemove.add(virtualView);
                }

            } catch (PlayerDoesNotExistException | InvalidPayloadException ignored) {
            }
        }

        for (VirtualView virtualView : virtualViewsToRemove) Server.getInstance().removeVirtualView(virtualView);
    }

}
