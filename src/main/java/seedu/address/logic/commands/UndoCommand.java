package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

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
