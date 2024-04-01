package seedu.address.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.storage.ImportUserPrefs;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final Path SANDBOX_FOLDER = Paths.get("src", "test", "data", "sandbox");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting path.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static Path getFilePathInSandboxFolder(String fileName) {
        try {
            Files.createDirectories(SANDBOX_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER.resolve(fileName);
    }

    /**
     * Returns the person in the {@code model}'s person list at {@code index}.
     */
    public static Person getPerson(Model model, StudentId studentId) {
        for (Person person : model.getFilteredPersonList()) {
            if (person.getStudentId().equals(studentId)) {
                return person;
            }
        }
        return null;
    }

    /**
     * Generic method to create a mock CSV file
     */
    public static void createMockCsvFile() {
        try {
            Path jsonFilePath = Paths.get("./data/test.json");
            if (Files.exists(jsonFilePath)) {
                Files.delete(jsonFilePath);
            }
            Path csvDirectoryPath = Paths.get(ImportUserPrefs.IMPORTS_DIRECTORY).toAbsolutePath();
            if (Files.notExists(csvDirectoryPath)) {
                Files.createDirectories(csvDirectoryPath);
            }

            Path csvFilePath = csvDirectoryPath.resolve("test.csv");

            if (Files.exists(csvFilePath)) {
                Files.delete(csvFilePath); // Delete if exists
            }
            Files.createFile(csvFilePath); // Create a new file
        } catch (IOException e) {
            System.out.println("Error creating mock CSV file: " + e.getMessage());
        }
    }

    /**
     * Generic method to set up for migration commands
     */
    public static JsonAddressBookStorage setUpForMigration(Path temporaryFolder) {
        return new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
    }
}
