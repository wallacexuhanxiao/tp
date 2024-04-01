package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.util.FileUtil;

public class ImportManagerTest {

    private static final String CSV_HEADER =
            "StudentId,Name,ParentPhoneOne,ParentPhoneTwo,Email,Address,Tags,Class\n";
    private static final String EXISTING_JSON =
            "{ \"persons\": [\n"
                    + "  { \"studentId\": \"00002\", \"name\": \"Jane Doe\", \"parentPhoneNumberOne\": \"81234567\", "
                    + "\"parentPhoneNumberTwo\": \"87654321\", \"email\": \"janedoe@example.com\", "
                    + "\"address\": \"124 Example Street\", \"tags\": [\"Friends\", \"Family\"], "
                    + "\"formClass\": \"6A\" }\n]}";
    private static final String INVALID_EMAIL = "111";
    private static final String INVALID_FORM_CLASS = "!94";
    private static final String INVALID_NAME = "John D!!";
    private static final String INVALID_PHONE_NUMBER = "00001111";
    private static final String INVALID_STUDENT_ID = "1";
    private static final String INVALID_TAG = "Track and Field";
    private static final String VALID_ADDRESS = "123 Example Street";
    private static final String VALID_EMAIL = "johndoe@example.com";
    private static final String VALID_FORM_CLASS = "6A";
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PHONE_NUMBER_ONE = "91234567";
    private static final String VALID_PHONE_NUMBER_TWO = "97654321";
    private static final String VALID_STUDENT_ID = "00001";
    private static final String VALID_TAG = "Track";
    private static final String DUPLICATE_ID_ENTRY = "00002" + "," + VALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
            + VALID_PHONE_NUMBER_TWO + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
            + VALID_FORM_CLASS;
    private static final String VALID_ENTRY = VALID_STUDENT_ID + "," + VALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
            + VALID_PHONE_NUMBER_TWO + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
            + VALID_FORM_CLASS;

    private static final String INVALID_NAME_ENTRY =
            VALID_STUDENT_ID + "," + INVALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
            + VALID_PHONE_NUMBER_TWO + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
            + VALID_FORM_CLASS;

    private static final String INVALID_STUDENT_ID_ENTRY =
            INVALID_STUDENT_ID + "," + VALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
                    + VALID_PHONE_NUMBER_TWO + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
                    + VALID_FORM_CLASS;

    private static final String INVALID_PHONE_ENTRY_ONE =
            VALID_STUDENT_ID + "," + VALID_NAME + "," + INVALID_PHONE_NUMBER + ","
                    + VALID_PHONE_NUMBER_TWO + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
                    + VALID_FORM_CLASS;

    private static final String INVALID_PHONE_ENTRY_TWO =
            VALID_STUDENT_ID + "," + VALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
                    + INVALID_PHONE_NUMBER + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
                    + VALID_FORM_CLASS;

    private static final String INVALID_EMAIL_ENTRY =
            VALID_STUDENT_ID + "," + VALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
                    + VALID_PHONE_NUMBER_TWO + "," + INVALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
                    + VALID_FORM_CLASS;

    private static final String INVALID_TAG_ENTRY =
            VALID_STUDENT_ID + "," + VALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
                    + VALID_PHONE_NUMBER_TWO + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + INVALID_TAG + ","
                    + VALID_FORM_CLASS;

    private static final String INVALID_FORM_CLASS_ENTRY =
            VALID_STUDENT_ID + "," + VALID_NAME + "," + VALID_PHONE_NUMBER_ONE + ","
                    + VALID_PHONE_NUMBER_TWO + "," + VALID_EMAIL + "," + VALID_ADDRESS + "," + VALID_TAG + ","
                    + INVALID_FORM_CLASS;

    @TempDir
    public Path tempDir;

    private ImportManager importManager;

    @BeforeEach
    public void setUp() {
        Path pathToImportFrom = tempDir.resolve("test.csv");
        Path pathToImportTo = tempDir.resolve("test.json");
        ImportManager importManager = new ImportManager(pathToImportFrom, pathToImportTo);
        this.importManager = importManager;
    }

    @Test
    public void importCsvFileAndConvertToJsonFile_validCsv_convertsToJson() throws IOException {
        String csvContent = CSV_HEADER + VALID_ENTRY;

        FileUtil.writeToFile(importManager.getPathToImportFrom(), csvContent);

        importManager.importCsvFileAndConvertToJsonFile();

        String expectedJson = "{ \"persons\": [\n"
                + "  { \"studentId\": \"00001\", \"name\": \"John Doe\", \"parentPhoneNumberOne\": \"91234567\", "
                + "\"parentPhoneNumberTwo\": \"97654321\", \"email\": \"johndoe@example.com\", "
                + "\"address\": \"123 Example Street\", \"tags\": [\"Track\"], \"formClass\": \"6A\""
                + " }\n]}";
        String actualJson = FileUtil.readFromFile(importManager.getPathToImportTo()).trim();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void importCsvFileAndConvertToJsonFile_withInternalDuplicates_throwsIoException() throws IOException {
        String csvContent = CSV_HEADER + VALID_ENTRY + "\n" + VALID_ENTRY;
        FileUtil.writeToFile(importManager.getPathToImportFrom(), csvContent);
        assertThrows(IOException.class, () -> importManager.importCsvFileAndConvertToJsonFile());
    }

    @Test
    public void convertLineToJsonPerson_withInvalidName_throwsIoException() throws IOException {
        String csvContent = INVALID_NAME_ENTRY;
        assertThrows(IOException.class, () -> importManager.convertLineToJsonPerson(csvContent));
        try {
            importManager.convertLineToJsonPerson(csvContent);
        } catch (IOException e) {
            assertEquals("Invalid name in CSV file: " + INVALID_NAME, e.getMessage());
        }
    }

    @Test
    public void convertLineToJsonPerson_withInvalidStudentId_throwsIoException() throws IOException {
        String csvContent = INVALID_STUDENT_ID_ENTRY;
        assertThrows(IOException.class, () -> importManager.convertLineToJsonPerson(csvContent));
        try {
            importManager.convertLineToJsonPerson(csvContent);
        } catch (IOException e) {
            assertEquals("Invalid student ID in CSV file: " + INVALID_STUDENT_ID, e.getMessage());
        }
    }

    @Test
    public void convertLineToJsonPerson_withInvalidPhoneOne_throwsIoException() throws IOException {
        String csvContent = INVALID_PHONE_ENTRY_ONE;
        assertThrows(IOException.class, () -> importManager.convertLineToJsonPerson(csvContent));
        try {
            importManager.convertLineToJsonPerson(csvContent);
        } catch (IOException e) {
            assertEquals("Invalid phone number in CSV file: " + INVALID_PHONE_NUMBER, e.getMessage());
        }
    }

    @Test
    public void convertLineToJsonPerson_withInvalidPhoneTwo_throwsIoException() throws IOException {
        String csvContent = INVALID_PHONE_ENTRY_TWO;
        assertThrows(IOException.class, () -> importManager.convertLineToJsonPerson(csvContent));
        try {
            importManager.convertLineToJsonPerson(csvContent);
        } catch (IOException e) {
            assertEquals("Invalid phone number in CSV file: " + INVALID_PHONE_NUMBER, e.getMessage());
        }
    }

    @Test
    public void convertLineToJsonPerson_withInvalidEmail_throwsIoException() throws IOException {
        String csvContent = INVALID_EMAIL_ENTRY;
        assertThrows(IOException.class, () -> importManager.convertLineToJsonPerson(csvContent));
        try {
            importManager.convertLineToJsonPerson(csvContent);
        } catch (IOException e) {
            assertEquals("Invalid email in CSV file: " + INVALID_EMAIL, e.getMessage());
        }
    }

    @Test
    public void convertLineToJsonPerson_withInvalidTag_throwsIoException() throws IOException {
        String csvContent = INVALID_TAG_ENTRY;
        assertThrows(IOException.class, () -> importManager.convertLineToJsonPerson(csvContent));
        try {
            importManager.convertLineToJsonPerson(csvContent);
        } catch (IOException e) {
            assertEquals("Invalid tag in CSV file: " + INVALID_TAG, e.getMessage());
        }
    }

    @Test
    public void convertLineToJsonPerson_withInvalidClass_throwsIoException() throws IOException {
        String csvContent = INVALID_FORM_CLASS_ENTRY;
        assertThrows(IOException.class, () -> importManager.convertLineToJsonPerson(csvContent));
        try {
            importManager.convertLineToJsonPerson(csvContent);
        } catch (IOException e) {
            assertEquals("Invalid form class name in CSV file: " + INVALID_FORM_CLASS, e.getMessage());
        }
    }

    @Test
    public void importCsvFileAndAddToJsonFile_validCsv_addsToJson() throws IOException {
        FileUtil.writeToFile(importManager.getPathToImportTo(), EXISTING_JSON);

        FileUtil.writeToFile(importManager.getPathToImportFrom(), CSV_HEADER + VALID_ENTRY);

        importManager.importCsvFileAndAddToJsonFile();

        String expectedJson = "{ \"persons\": [\n"
                + "  { \"studentId\": \"00002\", \"name\": \"Jane Doe\", \"parentPhoneNumberOne\": \"81234567\", "
                + "\"parentPhoneNumberTwo\": \"87654321\", \"email\": \"janedoe@example.com\", \"address\":"
                + " \"124 Example Street\", \"tags\": [\"Friends\", \"Family\"]"
                + ", \"formClass\": \"6A\" }, " + "{ \"studentId\": \"00001\", "
                + "\"name\": \"John Doe\", \"parentPhoneNumberOne\": \"91234567\", \"parentPhoneNumberTwo\": "
                + "\"97654321\", \"email\": \"johndoe@example.com\", \"address\": \"123 Example Street\", \"tags\": "
                + "[\"Track\"]"
                + ", \"formClass\": \"6A\""
                + " }\n]}";
        String actualJson = FileUtil.readFromFile(importManager.getPathToImportTo()).trim();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void importCsvFileAndAddToJsonFile_duplicateId_throwsIoException() throws IOException {
        FileUtil.writeToFile(importManager.getPathToImportTo(), EXISTING_JSON);

        String csvContent = CSV_HEADER + DUPLICATE_ID_ENTRY;
        FileUtil.writeToFile(importManager.getPathToImportFrom(), csvContent);

        assertThrows(IOException.class, () -> importManager.importCsvFileAndAddToJsonFile());
    }

    @Test
    public void importCsvFileAndAddToJsonFile_internalDuplicateId_throwsIoException() throws IOException {
        FileUtil.writeToFile(importManager.getPathToImportTo(), EXISTING_JSON);

        String csvContent = CSV_HEADER + VALID_ENTRY + "\n" + VALID_ENTRY;
        FileUtil.writeToFile(importManager.getPathToImportFrom(), csvContent);

        assertThrows(IOException.class, () -> importManager.importCsvFileAndAddToJsonFile());
    }

    @Test
    public void getPathToImportFrom_returnsCorrectPath() {
        Path expectedPath = tempDir.resolve("test.csv");
        assertEquals(expectedPath, importManager.getPathToImportFrom());
    }

    @Test
    public void getPathToImportTo_returnsCorrectPath() {
        Path expectedPath = tempDir.resolve("test.json");
        assertEquals(expectedPath, importManager.getPathToImportTo());
    }

    @Test
    public void setPathToImportFrom_setsNewPath() {
        Path newPath = tempDir.resolve("newTest.csv");
        importManager.setPathToImportFrom(newPath);
        assertEquals(newPath, importManager.getPathToImportFrom());
    }

    @Test
    public void setPathToImportTo_setsNewPath() {
        Path newPath = tempDir.resolve("newTest.json");
        importManager.setPathToImportTo(newPath);
        assertEquals(newPath, importManager.getPathToImportTo());
    }
}
