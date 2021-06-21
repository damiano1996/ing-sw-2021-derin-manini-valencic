package it.polimi.ingsw.psp26.application.files;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.CannotReadMessageFileException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class that contains methods to operate with Files
 */
public class Files {

    /**
     * Writes a String on a File using a FileWriter
     *
     * @param fileName The name of the File where the String is written
     * @param content  The String to write in the File
     */
    public static void writeToFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }


    /**
     * Reads a String from a File using a Scanner
     *
     * @param fileName The name of the File to read
     * @return The String contained in the File
     * @throws FileNotFoundException Thrown if the File doesn't exist
     */
    public static String readFromFile(String fileName) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) content.append(scanner.nextLine());
        scanner.close();
        return content.toString();
    }


    /**
     * Deletes a File
     *
     * @param fileName The name of the File to delete
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFile(String fileName) {
        new File(fileName).delete();
    }


    /**
     * Writes a Message Object into a File
     *
     * @param fileName The name of the File where to write the Message Object
     * @param message  The Message Object to write into the File
     */
    public static void writeMessageToFile(String fileName, Message message) {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(message);

            fileOutputStream.close();
            objectOutputStream.close();

        } catch (IOException ignored) {
        }

    }


    /**
     * Reads a Message Object from a File
     *
     * @param fileName The name of the File containing the Message Object to read
     * @return The Message Object contained in the File
     * @throws CannotReadMessageFileException Thrown if the File cannot be read due to an IOException or ClassNotFoundException
     */
    public static Message readMessageFromFile(String fileName) throws CannotReadMessageFileException {
        try {

            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Message message = (Message) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();

            return message;

        } catch (IOException | ClassNotFoundException ignored) {
            throw new CannotReadMessageFileException();
        }
    }


    /**
     * Creates a new directory in the given directoryPath if it doesn't already exist
     *
     * @param directoryPath The path where to create the directory (if not already present)
     * @return True if a new directory is created, false if a new directory is not created
     */
    public static boolean createNewDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.mkdir();
    }


    /**
     * Method to delete recursively a directory: files and sub-directories will be deleted.
     *
     * @param directoryPath The path of the Directory to delete
     */
    public static void deleteDirectory(String directoryPath) {
        File directoryToDelete = new File(directoryPath);

        if (directoryToDelete.isDirectory()) {
            String[] filesInDirectory = directoryToDelete.list();
            if (filesInDirectory != null) {
                for (String fileName : filesInDirectory) {
                    String path = directoryToDelete.getPath() + "/" + fileName;
                    System.out.println("Files - looking for: " + path);
                    deleteDirectory(path);
                }
            }
        }

        System.out.println("Files - Deleting file: " + directoryPath);
        deleteFile(directoryPath);
    }


    /**
     * Creates and returns a List of all the File Names in the given path
     *
     * @param directoryPath The name of the path where to find all the Files names
     * @return The Files names contained in the directoryPath directory
     */
    public static List<String> getFilesNamesInDirectory(String directoryPath) {
        try {
            return Arrays.stream(Objects.requireNonNull(new File(directoryPath).list())).sorted().collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
