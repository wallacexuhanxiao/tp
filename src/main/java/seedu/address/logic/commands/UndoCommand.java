package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Command for undo the previous command only support add, delete, edit.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_SUCCESS = "Successfully undo the previous command!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Command reverseCommand = model.getLastCommand();
        return reverseCommand.execute(model);
    }
}
