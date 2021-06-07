package it.polimi.ingsw.psp26.application.files;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.CannotReadMessageFileException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Files {

    public static void writeToFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    public static String readFromFile(String fileName) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) content.append(scanner.nextLine());
        scanner.close();
        return content.toString();
    }

    public static boolean deleteFile(String fileName) {
        return new File(fileName).delete();
    }

    public static void writeMessageToFile(String fileName, Message message) {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(message);

        } catch (IOException ignored) {
        }

    }

    public static Message readMessageFromFile(String fileName) throws CannotReadMessageFileException {
        try {

            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Message) objectInputStream.readObject();

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

    public static List<String> getFilesNamesInDirectory(String directoryPath) {
        try {
            return Arrays.stream(Objects.requireNonNull(new File(directoryPath).list())).sorted().collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
