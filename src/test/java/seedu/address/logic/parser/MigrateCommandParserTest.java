package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.MigrateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.storage.ImportManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.testutil.TestUtil;

public class MigrateCommandParserTest {

    @TempDir
    public Path temporaryFolder;

    private JsonAddressBookStorage tempAddressBookStorage;

    private MigrateCommandParser parser = new MigrateCommandParser();

    @BeforeEach
    public void setUp() {
        this.tempAddressBookStorage = TestUtil.setUpForMigration(temporaryFolder);
    }

    @Test
    public void parse_validFileName_returnsMigrateCommand() throws ParseException {
        Path pathToImportFrom = Paths.get("imports/test.csv");
        MigrateCommand expectedCommand = new MigrateCommand(
                pathToImportFrom,
                new ImportManager(pathToImportFrom, tempAddressBookStorage.getAddressBookFilePath())
        );

        MigrateCommand resultCommand = parser.parse("test");

        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        String emptyInput = "";
        assertThrows(ParseException.class, () -> parser.parse(emptyInput));
    }

    @Test
    public void parse_invalidFileName_throwsParseException() {
        String invalidFileName = "invalid/file*name";
        assertThrows(Exception.class, () -> parser.parse(invalidFileName));
    }

    @Test
    public void parse_validFileNameWithWhitespace_returnsMigrateCommand() throws ParseException {
        Path pathToImportFrom = Paths.get("imports/test.csv");
        MigrateCommand expectedCommand = new MigrateCommand(
                pathToImportFrom,
                new ImportManager(pathToImportFrom, tempAddressBookStorage.getAddressBookFilePath())
        );

        MigrateCommand resultCommand = parser.parse("     test     ");

        assertEquals(expectedCommand, resultCommand);
    }

}
