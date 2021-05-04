package it.polimi.ingsw.psp26.network;

import java.io.*;
import java.net.Socket;

/**
 * Class to model a node of the network.
 * It contains input and output streams to receive and send messages over the network.
 */
public class NetworkNode {

    private final Socket socket;

    // To send data
    private final OutputStream outputStream;
    private final PrintWriter printWriter;
    private final ObjectOutputStream objectOutputStream;

    // To read data
    private final InputStream inputStream;
    private final InputStreamReader inputStreamReader;
    private final BufferedReader bufferedReader;
    private final ObjectInputStream objectInputStream;

    /**
     * Constructor of the class.
     *
     * @param socket socket to receive and send data
     * @throws IOException if stream instantiation error
     */
    public NetworkNode(Socket socket) throws IOException {
        this.socket = socket;

        // To send data
        outputStream = this.socket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);
        objectOutputStream = new ObjectOutputStream(outputStream);

        // To read data
        inputStream = this.socket.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
        objectInputStream = new ObjectInputStream(inputStream);
    }

    /**
     * Method to send string data.
     *
     * @param data data to send
     */
    public void sendData(String data) {
        printWriter.println(data);
    }

    /**
     * Method to send data object.
     *
     * @param data data to send
     * @throws IOException if object can not be sent
     */
    public synchronized void sendData(Object data) throws IOException { //TODO OCCHIO AL SYNCHRONISED
        objectOutputStream.writeObject(data);
    }

    /**
     * Method to receive string data.
     *
     * @return string line
     * @throws IOException if data can not be read
     */
    public String receiveStringData() throws IOException {
        return bufferedReader.readLine();
    }

    /**
     * Method to receive an object.
     *
     * @return received object
     * @throws IOException            if object can not be read
     * @throws ClassNotFoundException if object class not found
     */
    public Object receiveObjectData() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
    }

    /**
     * Method to close socket connection.
     *
     * @throws IOException if connection can not be closed
     */
    public void closeConnection() throws IOException {
        socket.close();
    }

}
