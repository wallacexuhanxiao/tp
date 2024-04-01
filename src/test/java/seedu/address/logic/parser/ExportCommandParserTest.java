package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.ExportManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class ExportCommandParserTest {

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    private ExportCommandParser parser = new ExportCommandParser();

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void parseValidInputs_returnsExportCommand() throws ParseException, IOException {
        Path testPath = Path.of("./exports/test.csv");
        ExportManager testManager = new ExportManager();
        ExportCommand expectedCommand = new ExportCommand(testManager, testPath);
        expectedCommand.execute(model);
        ExportCommand resultCommand = parser.parse("test");
        resultCommand.execute(model);
        assertEquals(expectedCommand, resultCommand);
        //Cleanup
        Files.deleteIfExists(testPath);
    }

    @Test
    public void parseEmptyFileName_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    public void parseInvalidFileName_throwsParseException() {
        assertThrows(Exception.class, () -> parser.parse("% !##@ . . ."));
    }
}
