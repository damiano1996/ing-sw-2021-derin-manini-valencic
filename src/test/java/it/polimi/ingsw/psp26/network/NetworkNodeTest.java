package it.polimi.ingsw.psp26.network;

import it.polimi.ingsw.psp26.model.enums.Resource;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NetworkNodeTest {

    private ServerSocket serverSocket;
    private Thread thread;
    private Socket s1, s2;
    private NetworkNode n1, n2;

    @Before
    public void setUp() throws IOException, InterruptedException {
        startServer();

        s2 = new Socket("localhost", DEFAULT_SERVER_PORT);
        n2 = new NetworkNode(s2);

        thread.join();
    }

    private void startServer() throws IOException {
        serverSocket = new ServerSocket(DEFAULT_SERVER_PORT);

        thread = new Thread(() -> {
            while (s1 == null) {
                try {
                    s1 = serverSocket.accept();
                    n1 = new NetworkNode(s1);
                } catch (IOException ignored) {
                }
            }
        });
        thread.start();
    }

    private void closeAll() throws IOException {
        serverSocket.close();
        s1.close();
        s2.close();
    }

    @Test
    public void testSendReceiveStringData() throws IOException {
        String message = "hello world!";
        n1.sendData(message);
        assertEquals(message, n2.receiveStringData());
        closeAll();
    }

    @Test
    public void testSendReceiveObjectData() throws IOException, ClassNotFoundException {
        Resource resource = Resource.COIN;
        n1.sendData(resource);
        assertEquals(resource, n2.receiveObjectData());
        closeAll();
    }

    @Test
    public void testCloseConnection() throws IOException {
        n1.closeConnection();
        assertTrue(s1.isClosed());

        n2.closeConnection();
        assertTrue(s2.isClosed());

        serverSocket.close();
    }
}