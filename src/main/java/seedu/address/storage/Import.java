package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * API of the import component.
 */
public interface Import {

    /**
     * Gets the path to import from.
     */
    Path getPathToImportFrom();

    /**
     * Gets the path to import to.
     */
    Path getPathToImportTo();

    /**
     * Sets the path to import to.
     */
    void setPathToImportTo(Path newPath);

    /**
     * Sets the path to import from.
     */
    void setPathToImportFrom(Path newPath);

    /**
     *
     */
    void importCsvFileAndAddToJsonFile() throws IOException, ParseException;

    /**
     * Imports a csv file as a json file.
     */
    void importCsvFileAndConvertToJsonFile() throws IOException, ParseException;
}
