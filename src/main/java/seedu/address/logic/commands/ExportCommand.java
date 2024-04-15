package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.storage.ExportManager;

/**
 * Exports the current list to CSV.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports the current viewed list to a CSV file. "
            + "Specify the filename you wish to export the file as after the command word."
            + "If the filename is already present in the exports folder, a default export name will be used: "
            + "export_{date and time of export}.csv";

    public static final String MESSAGE_SUCCESS = "List successfully exported and can be found in the exports folder.";
    public static final String MESSAGE_SUCCESS_WITH_DUPLICATE_NAME = "A file with the same name was found in the "
            + "exports folder. Pedagogue Pages will still export this list but with a default name export_{date and "
            + "time of export}.csv. Please check the exports folder and rename the file if necessary.";

    public static final String MESSAGE_FAILURE = "Unable to export list.";

    private ObservableList<Person> personList;
    private ExportManager exportManager;

    private Path pathToExportTo;
    private Boolean fileAlreadyExistsAtPath = false;

    /**
     * Creates an ExportCommand with the specified exportManager
     */
    public ExportCommand(ExportManager exportManager, Path pathToExportTo) {
        this.exportManager = exportManager;
        this.pathToExportTo = pathToExportTo;
        if (FileUtil.isFileExists(pathToExportTo)) {
            fileAlreadyExistsAtPath = true;
        }

    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        ObservableList<Person> personList = model.getFilteredPersonList();
        this.personList = personList;
        assert this.exportManager != null;
        assert this.pathToExportTo != null;
        assert this.fileAlreadyExistsAtPath != null;
        try {
            this.exportManager.exportStudentList(personList, pathToExportTo);
        } catch (IOException e) {
            return new CommandResult(MESSAGE_FAILURE);
        }
        if (fileAlreadyExistsAtPath) {
            return new CommandResult(MESSAGE_SUCCESS_WITH_DUPLICATE_NAME);
        } else {
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return personList.equals(otherExportCommand.personList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Student list to export: ", personList)
                .toString();
    }


}
