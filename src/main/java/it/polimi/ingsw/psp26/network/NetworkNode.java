package it.polimi.ingsw.psp26.network;

import java.io.*;
import java.net.Socket;

public class NetworkNode {

    private final Socket socket;

    // To read data
    private final InputStream inputStream;
    private final InputStreamReader inputStreamReader;
    private final BufferedReader bufferedReader;

    // To send data
    private final OutputStream outputStream;
    private final PrintWriter printWriter;

    public NetworkNode(Socket socket) throws IOException {
        this.socket = socket;

        // To read data
        inputStream = this.socket.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);

        // To send data
        outputStream = this.socket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);
    }

    public void sendData(String data) {
        printWriter.println(data);
    }

    public String readData() throws IOException {
        return bufferedReader.readLine();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
