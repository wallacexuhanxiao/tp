package seedu.address.storage;

import static seedu.address.commons.util.StringFormatter.capitalizeWords;
import static seedu.address.model.person.Address.isValidAddress;
import static seedu.address.model.person.Email.isValidEmail;
import static seedu.address.model.person.FormClass.isValidClassName;
import static seedu.address.model.person.Name.isValidName;
import static seedu.address.model.person.Phone.isValidPhone;
import static seedu.address.model.person.StudentId.isValidStudentId;
import static seedu.address.model.tag.Tag.isValidTagName;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.util.FileUtil;

/**
 * Manages imports for the application.
 */
public class ImportManager implements Import {

    private Path pathToImportFrom;
    private Path pathToImportTo;

    /**
     * Creates an ImportManager with the specified paths.
     * @param pathToImportFrom Path to import from.
     * @param pathToImportTo Path to import to.
     */
    public ImportManager(Path pathToImportFrom, Path pathToImportTo) {
        this.pathToImportFrom = pathToImportFrom;
        this.pathToImportTo = pathToImportTo;
    }

    @Override
    public Path getPathToImportFrom() {
        return this.pathToImportFrom;
    }

    @Override
    public Path getPathToImportTo() {
        return this.pathToImportTo;
    }

    @Override
    public void setPathToImportTo(Path newPath) {
        this.pathToImportTo = newPath;
    }

    @Override
    public void setPathToImportFrom(Path newPath) {
        this.pathToImportFrom = newPath;
    }

    @Override
    public void importCsvFileAndConvertToJsonFile() throws IOException {
        String jsonString = convertCsvContentsToJsonContents();
        FileUtil.writeToFile(pathToImportTo, jsonString);
    }

    @Override
    public void importCsvFileAndAddToJsonFile() throws IOException {
        String existingJsonString = FileUtil.readFromFile(pathToImportTo).trim();
        Set<String> existingIds = extractStudentIds(existingJsonString);

        //Ensuring that the new json content is legitimate and throws an exception if there are duplicate student IDs.
        String newJsonContent = convertCsvContentsToJsonContents();
        ensureNoDuplicateIds(newJsonContent, existingIds);
        ensureNoInternalDuplicates(newJsonContent);

        String modifiedExistingJson = existingJsonString.substring(0, existingJsonString.lastIndexOf("]")).trim();
        String modifiedNewJsonContent = newJsonContent.substring(
                newJsonContent.indexOf("[") + 1, newJsonContent.lastIndexOf("]")).trim();

        if (!modifiedExistingJson.endsWith("[") && !modifiedNewJsonContent.isEmpty()) {
            modifiedNewJsonContent = ", " + modifiedNewJsonContent;
        }

        String combinedJson = modifiedExistingJson + modifiedNewJsonContent + "\n]}";
        FileUtil.writeToFile(pathToImportTo, combinedJson);
    }

    /**
     * Helper method to compile the existing student IDs in a json file currently open in the application.
     * @param jsonString String representation of the json file currently open in the application.
     * @return Set of student IDs in the json file currently open in the application.
     */
    private Set<String> extractStudentIds(String jsonString) {
        Set<String> ids = new HashSet<>();
        Pattern pattern = Pattern.compile("\"studentId\"\\s*:\\s*\"(\\d+)\"");
        Matcher matcher = pattern.matcher(jsonString);
        while (matcher.find()) {
            String id = matcher.group(1);
            ids.add(id);
        }
        return ids;
    }

    /**
     * Checks the student IDs in the String representation of new json content that is being migrated over against
     * the existing student IDs to ensure that there are no duplicates.
     * @param newJsonContent String representation of the json content that is being migrated over.
     * @param existingIds Set of student IDs in the json file currently open in the application.
     * @throws IOException If there are duplicate student IDs
     */
    private void ensureNoDuplicateIds(String newJsonContent, Set<String> existingIds) throws IOException {
        Pattern pattern = Pattern.compile("\"studentId\": \"(\\d+)\"");
        Matcher matcher = pattern.matcher(newJsonContent);
        while (matcher.find()) {
            String newId = matcher.group(1);
            if (existingIds.contains(newId)) {
                throw new IOException("Duplicate StudentId found: " + newId);
            }
        }
    }

    /**
     * Checks for duplicate student IDs within the new JSON content.
     * @param newJsonContent The new JSON content to be merged.
     * @throws IOException If there are duplicate student IDs.
     */
    private void ensureNoInternalDuplicates(String newJsonContent) throws IOException {
        Set<String> ids = new HashSet<>();
        Pattern pattern = Pattern.compile("\"studentId\"\\s*:\\s*\"(\\d+)\"");
        Matcher matcher = pattern.matcher(newJsonContent);
        while (matcher.find()) {
            String id = matcher.group(1);
            if (!ids.add(id)) {
                throw new IOException("Internal duplicate StudentId found: " + id);
            }
        }
    }

    /**
     * Converts the import csv contents to json contents to be stored.
     *
     * @return String to be stored in the json file.
     */
    private String convertCsvContentsToJsonContents() throws IOException {
        StringBuilder jsonStringBuilder = new StringBuilder();
        String csvContents = FileUtil.readFromFile(pathToImportFrom);
        String[] lines = csvContents.split("\n");
        jsonStringBuilder.append("{ \"persons\": [");

        // Skip the first line (header) and process the rest
        for (int i = 1; i < lines.length; i++) {
            if (i > 1) {
                jsonStringBuilder.append(",");
            }
            jsonStringBuilder.append("\n").append(convertLineToJsonPerson(lines[i].trim()));
        }

        jsonStringBuilder.append("\n]}");
        String jsonString = jsonStringBuilder.toString();
        ensureNoInternalDuplicates(jsonString);
        return jsonString;
    }

    /**
     * Converts a line in the import csv to a json person.
     * @param line csv line to be converted
     * @return String representation of a person in correct json format.
     */
    public String convertLineToJsonPerson(String line) throws IOException {
        String[] data = line.split(",");
        StringJoiner tagsJoiner = new StringJoiner("\", \"", "[\"", "\"]");
        boolean areTagsValid = true;
        if (data.length > 6) {
            for (String tag : data[6].split(";")) {
                areTagsValid = areTagsValid && isValidTagName((capitalizeWords(tag.trim())));
                tagsJoiner.add(capitalizeWords(tag.trim()));
            }
        }

        checksDataValid(data, areTagsValid);

        return String.format(
                "  { \"studentId\": \"%s\", \"name\": \"%s\", \"parentPhoneNumberOne\": \"%s\", "
                        + "\"parentPhoneNumberTwo\": \"%s\", \"email\": \"%s\", \"address\": \"%s\", \"tags\": %s,"
                        + " \"formClass\": \"%s\" }",
                data[0], capitalizeWords(data[1]), data[2], data[3], data[4], capitalizeWords(data[5]), tagsJoiner,
                data[7]);
    }

    /**
     * Private helper function to verify different data fields.
     */
    private void checksDataValid(String[] data, boolean areTagsValid) throws IOException {
        if (!isValidStudentId(data[0])) {
            throw new IOException("Invalid student ID in CSV file: " + data[0]);
        }
        if (!isValidName(capitalizeWords(data[1]))) {
            throw new IOException("Invalid name in CSV file: " + data[1]);
        }

        if (!isValidPhone(data[2])) {
            throw new IOException("Invalid phone number in CSV file: " + data[2]);
        }

        if (!isValidPhone(data[3])) {
            throw new IOException("Invalid phone number in CSV file: " + data[3]);
        }

        if (!isValidEmail(data[4])) {
            throw new IOException("Invalid email in CSV file: " + data[4]);
        }

        if (!isValidAddress(data[5])) {
            throw new IOException("Invalid address in CSV file: " + data[5]);
        }

        if (!areTagsValid) {
            throw new IOException("Invalid tag in CSV file: " + data[6]);
        }

        if (!isValidClassName(data[7])) {
            throw new IOException("Invalid form class name in CSV file: " + data[7]);
        }
    }

}
