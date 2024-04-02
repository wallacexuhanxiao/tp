package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.storage.ImportManager;
import seedu.address.testutil.TestUtil;

public class ImportCommandParserTest {

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_validFileName_returnsImportCommand() throws ParseException {
        TestUtil.createMockCsvFile();
        Path pathToImportFrom = Paths.get("./imports/test.csv");
        Path pathToImportTo = Paths.get("data/test.json");
        ImportCommand expectedCommand = new ImportCommand(
                pathToImportFrom,
                pathToImportTo,
                new ImportManager(pathToImportFrom, pathToImportTo)
        );

        ImportCommand resultCommand = parser.parse("test");

        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        ImportCommandParser parser = new ImportCommandParser();
        String emptyInput = "";

        assertThrows(ParseException.class, () -> parser.parse(emptyInput));
    }

    @Test
    public void parse_invalidFileName_throwsParseException() {
        ImportCommandParser parser = new ImportCommandParser();
        String invalidFileName = "invalid/file*name";

        assertThrows(Exception.class, () -> parser.parse(invalidFileName));
    }

    @Test
    public void parse_validFileNameWithWhitespace_returnsImportCommand() throws ParseException {
        TestUtil.createMockCsvFile();
        Path pathToImportFrom = Paths.get("./imports/test.csv");
        Path pathToImportTo = Paths.get("data/test.json");
        ImportCommand expectedCommand = new ImportCommand(
                pathToImportFrom,
                pathToImportTo,
                new ImportManager(pathToImportFrom, pathToImportTo)
        );

        ImportCommand resultCommand = parser.parse("     test     ");

        assertEquals(expectedCommand, resultCommand);
    }
}
