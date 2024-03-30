package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.ONLY_BENSON_TAG;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.EditCommand.createEditedPerson;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;


/**
 * Contains integration tests and unit tests for DeleteTagCommand.
 */
public class DeleteTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_invalidTag_failure() {
        Tag targetTag = new Tag("NobodyWillPutATagLikeThisSurely");
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(targetTag);

        assertCommandFailure(deleteTagCommand, model,
                String.format(Messages.MESSAGE_NOBODY_HAS_TAG, targetTag));
    }

    @Test
    public void execute_validTag_success() {
        Tag targetTag = new Tag(ONLY_BENSON_TAG);
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(targetTag);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person editedPerson = createEditedPerson(BENSON, new EditCommand.EditPersonDescriptor());
        expectedModel.setPerson(model.getFilteredPersonList().get(1), editedPerson);

        assertCommandSuccess(deleteTagCommand, model,
                String.format("Deleted Tag: %s", targetTag), model);

    }
}
