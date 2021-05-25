package it.polimi.ingsw.psp26.application.files;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.CannotReadMessageFileException;

import java.io.*;
import java.util.Scanner;

import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_FILES;

public class Files {

    public static void writeToFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(GAME_FILES + fileName);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    public static String readFromFile(String fileName) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        File file = new File(GAME_FILES + fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) content.append(scanner.nextLine());
        scanner.close();
        return content.toString();
    }

    public static void deleteFile(String fileName) {
        new File(GAME_FILES + fileName).delete();
    }

    public static void writeMessageToFile(String fileName, Message message) {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(GAME_FILES + fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(message);

        } catch (IOException ignored) {
        }

    }

    public static Message readMessageFromFile(String fileName) throws CannotReadMessageFileException {
        try {

            FileInputStream fileInputStream = new FileInputStream(GAME_FILES + fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Message) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException ignored) {
            throw new CannotReadMessageFileException();
        }
    }

}
