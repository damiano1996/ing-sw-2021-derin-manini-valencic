package it.polimi.ingsw.psp26.network;

import java.io.*;
import java.net.Socket;

public class NetworkNode {

    private final Socket socket;

    // To read data
    private final InputStream inputStream;
    private final InputStreamReader inputStreamReader;
    private final BufferedReader bufferedReader;
//    private final ObjectInputStream objectInputStream;

    // To send data
    private final OutputStream outputStream;
    private final PrintWriter printWriter;
    private final ObjectOutputStream objectOutputStream;

    public NetworkNode(Socket socket) throws IOException {
        this.socket = socket;

        // To read data
        inputStream = this.socket.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
//        objectInputStream = new ObjectInputStream(inputStream);

        // To send data
        outputStream = this.socket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);
        objectOutputStream = new ObjectOutputStream(outputStream);
    }

    public void sendData(String data) {
        printWriter.println(data);
    }

    public void sendData(Object data) throws IOException {
        objectOutputStream.writeObject(data);
    }

    public String readStringData() throws IOException {
        return bufferedReader.readLine();
    }

//    public Object readObjectData() throws IOException, ClassNotFoundException {
//        return objectInputStream.readObject();
//    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
