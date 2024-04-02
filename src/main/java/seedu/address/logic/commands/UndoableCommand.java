package seedu.address.logic.commands;

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
