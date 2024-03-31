package seedu.address.logic.commands;

import seedu.address.model.ReadOnlyUserPrefs;

/**
 * The interface for undoable command
 */
public interface UndoableCommand {

    /**
     * Returns reverse command for teh current command.
     * @return Reverse command.
     */
    Command getReverseCommand();
}
