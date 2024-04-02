package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_LIST_SORTED_SUCCESSFULLY;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class SortCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_sortByName_success() {
        SortCommand command = new SortCommand("name");
        String expectedMessage = String.format(MESSAGE_LIST_SORTED_SUCCESSFULLY, "name");
        expectedModel.sortFilteredPersonList(SortCommand.PERSON_NAME_COMPARATOR);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortById_success() {
        SortCommand command = new SortCommand("id");
        String expectedMessage = String.format(MESSAGE_LIST_SORTED_SUCCESSFULLY, "id");
        expectedModel.sortFilteredPersonList(SortCommand.PERSON_ID_COMPARATOR);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // @Test
    // public void execute_sortByInvalidField_throwsCommandException() {
    //     SortCommand command = new SortCommand("invalid");
    //     String expectedMessage = "Invalid arguments: args is null or empty";
    //     expectedModel.sortFilteredPersonList("abc");
    //     assertCommandSuccess(command, model, expectedMessage, expectedModel);
    // }

    public void execute_sortByNullField_throwsNullPointerException() {
        SortCommand command = new SortCommand(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> command.execute(model));
        assertEquals("Invalid arguments: args is null or empty", exception.getMessage());
    }

}
