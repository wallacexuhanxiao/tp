package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.storage.ExportManager;

/**
 * Represents a parser that parses the arguments of an export command.
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    public static final String EXPORT_PATH = "./exports/";
    @Override
    public ExportCommand parse(String args) throws ParseException {
        String fileNameToExportAs = args.trim();
        if (fileNameToExportAs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        if (!fileNameToExportAs.matches("[a-zA-Z0-9_\\s]+")) {
            throw new ParseException("Invalid file name, alphanumeric and white spaces only!");
        }

        String fileNameWithCsv = fileNameToExportAs + ".csv";
        Path pathToExportTo;
        try {
            pathToExportTo = Paths.get(EXPORT_PATH, fileNameWithCsv);
        } catch (InvalidPathException e) {
            throw new ParseException("Invalid file name!");
        }

        return new ExportCommand(new ExportManager(), pathToExportTo);
    }
}
