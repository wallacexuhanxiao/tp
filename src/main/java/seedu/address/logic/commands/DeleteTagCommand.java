package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Deletes tags from all students that contain tags that match the given string.
 */
public class DeleteTagCommand extends Command {

    public static final String COMMAND_WORD = "deleteTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Given a String, removes every tag from every student"
            + " that matches the String\n"
            + "Example: " + COMMAND_WORD + "Olympiad Student";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Tag: %1$s";

    private final Tag targetTag;
    public DeleteTagCommand(Tag targetTag) {
        this.targetTag = targetTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownlist = model.getFilteredPersonList();

        ArrayList<Person> peopleToEdit = new ArrayList<>();
        boolean found = false;

        // Iterate through the list to find people with the target tag
        for (Person candidate : lastShownlist) {
            Set<Tag> candidateTags = candidate.getTags();

            if (candidateTags.contains(targetTag)) {
                peopleToEdit.add(candidate);
                found = true;
            }
        }

        if (!found) {
            throw new CommandException(String.format(Messages.MESSAGE_NOBODY_HAS_TAG, targetTag));
        }

        for (Person toEdit : peopleToEdit) {
            EditCommand.EditPersonDescriptor edited = generateRemovedTags(toEdit, targetTag);
            Person editedPerson = EditCommand.createEditedPerson(toEdit, edited);
            assert (!editedPerson.equals(toEdit));

            model.setPerson(toEdit, editedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        }

        CommandResult result =
                new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, targetTag));
        result.setAddCommand();
        return result;
    }

    private EditCommand.EditPersonDescriptor generateRemovedTags(Person toEdit, Tag targetTag) {
        EditCommand.EditPersonDescriptor editedPerson = new EditCommand.EditPersonDescriptor();
        Set<Tag> removed = new HashSet<>();
        removed.addAll(toEdit.getTags());
        removed.remove(targetTag);
        editedPerson.setTags(removed);

        return editedPerson;
    }
}

