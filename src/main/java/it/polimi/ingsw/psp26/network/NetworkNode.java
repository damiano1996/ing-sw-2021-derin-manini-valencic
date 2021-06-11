package it.polimi.ingsw.psp26.network;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

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
     * Method to send data object.
     *
     * @param data data to send
     * @throws IOException if object can not be sent
     */
    public synchronized void sendData(Object data) throws IOException {
        objectOutputStream.writeObject(data);
    }

    /**
     * Method to receive an object.
     *
     * @return received object
     * @throws IOException            if object can not be read
     * @throws ClassNotFoundException if object class not found
     */
    public Object receiveData() throws IOException, ClassNotFoundException {
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

    /**
     * Equals method
     *
     * @param o Object to be compared
     * @return True if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkNode that = (NetworkNode) o;
        return Objects.equals(socket, that.socket) &&
                Objects.equals(outputStream, that.outputStream) &&
                Objects.equals(printWriter, that.printWriter) &&
                Objects.equals(objectOutputStream, that.objectOutputStream) &&
                Objects.equals(inputStream, that.inputStream) &&
                Objects.equals(inputStreamReader, that.inputStreamReader) &&
                Objects.equals(bufferedReader, that.bufferedReader) &&
                Objects.equals(objectInputStream, that.objectInputStream);
    }

    /**
     * hashCode method
     *
     * @return A hashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                socket, outputStream, printWriter,
                objectOutputStream, inputStream, inputStreamReader,
                bufferedReader, objectInputStream
        );
    }
}
