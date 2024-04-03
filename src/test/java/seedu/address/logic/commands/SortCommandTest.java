package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_LIST_SORTED_SUCCESSFULLY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.StudentId;

public class SortCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_sortByName_success() {
        SortCommand command = new SortCommand("name");
        String expectedMessage = String.format(MESSAGE_LIST_SORTED_SUCCESSFULLY, "name");
        expectedModel.sortAddressBook(SortCommand.PERSON_NAME_COMPARATOR);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void compareTo_sameStudentId_returnsZero() {
        StudentId studentId1 = new StudentId("45671");
        StudentId studentId2 = new StudentId("45671");

        assertEquals(0, studentId1.compareTo(studentId2));
    }

    @Test
    public void compareTo_studentId1LessThanStudentId2_returnsNegative() {
        StudentId studentId1 = new StudentId("45671");
        StudentId studentId2 = new StudentId("45681");
        assertEquals(-1, studentId1.compareTo(studentId2));
    }

    @Test
    public void compareTo_studentId1GreaterThanStudentId2_returnsPositive() {
        StudentId studentId1 = new StudentId("45681");
        StudentId studentId2 = new StudentId("45671");
        assertEquals(1, studentId1.compareTo(studentId2));
    }

    @Test
    public void execute_sortById_success() {
        SortCommand command = new SortCommand("id");
        String expectedMessage = String.format(MESSAGE_LIST_SORTED_SUCCESSFULLY, "id");
        expectedModel.sortAddressBook(SortCommand.PERSON_ID_COMPARATOR);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void testCompare() {
        SortCommand.NameAlphabeticalComparator comparator = new SortCommand.NameAlphabeticalComparator();

        // Test when the first name comes before the second name alphabetically
        assertEquals(-1, comparator.compare("Alice", "Bob"));
    }

    @Test
    public void testCompare_sameName_returnsZero() {
        SortCommand.NameAlphabeticalComparator comparator = new SortCommand.NameAlphabeticalComparator();

        // Test when the first name is the same as the second name
        assertEquals(0, comparator.compare("Alice", "Alice"));
    }

    @Test
    public void testCompare_firstNameComesAfterSecondName_returnsPositive() {
        SortCommand.NameAlphabeticalComparator comparator = new SortCommand.NameAlphabeticalComparator();

        // Test when the first name comes after the second name alphabetically
        assertEquals(1, comparator.compare("Bob", "Alice"));
    }

    @Test
    public void longNameComesBeforeShortName_returnsNegative() {
        SortCommand.NameAlphabeticalComparator comparator = new SortCommand.NameAlphabeticalComparator();

        // Test when the first name comes before the second name alphabetically
        assertEquals(1, comparator.compare("Alice", "Alic"));
    }

    @Test
    public void invalidSortTypeException() {
        SortCommand command = new SortCommand("invalid");
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_COMMAND_FORMAT);
    }

    @Test
    public void addressBook_sortByName_success() {
        model.sortAddressBook(SortCommand.PERSON_NAME_COMPARATOR);
        expectedModel.sortAddressBook(SortCommand.PERSON_NAME_COMPARATOR);
        assertEquals(expectedModel, model);
    }


    public void execute_sortByNullField_throwsNullPointerException() {
        SortCommand command = new SortCommand(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> command.execute(model));
        assertEquals("Invalid arguments: args is null or empty", exception.getMessage());
    }
}
