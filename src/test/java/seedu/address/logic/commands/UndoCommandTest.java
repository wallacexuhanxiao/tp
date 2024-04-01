package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.StudentId;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

class UndoCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_undoOnAdd() throws CommandException {
        AddCommand add = new AddCommand(new PersonBuilder().build());
        add.execute(model);
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.execute(model);
        assertTrue(model.equals(expectModel));
    }

    @Test
    public void execute_undoOnDelete() throws CommandException {
        DeleteCommand deleteCommand = new DeleteCommand(new StudentId("00001"));
        deleteCommand.execute(model);
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.execute(model);
        assertFalse(model.equals(expectModel));
    }

    @Test
    public void execute_undoOnEdit() throws CommandException {
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withEmail("xxx@ddd.com").build();
        EditCommand editCommand = new EditCommand(new StudentId("00001"), descriptor);
        editCommand.execute(model);
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.execute(model);
        assertTrue(model.equals(expectModel));
    }
}
