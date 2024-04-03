package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.FormClass;
import seedu.address.model.person.FormClassMatchesPredicate;
import seedu.address.model.person.IdMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TagMatchesPredicate;
import seedu.address.model.tag.Tag;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "5 00001",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1 ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindNameCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        FindCommand expectedFindIdCommand =
                new FindCommand(new IdMatchesPredicate(new StudentId("00001")));
        FindCommand expectedFindTagCommand =
                new FindCommand(new TagMatchesPredicate(new Tag("SomeTag")));
        FindCommand expectedFindClassCommand =
                new FindCommand(new FormClassMatchesPredicate(new FormClass("SomeClass")));

        assertParseSuccess(parser, "name Alice Bob", expectedFindNameCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "name \n Alice \n \t Bob  \t", expectedFindNameCommand);

        assertParseSuccess(parser, "id 00001", expectedFindIdCommand);

        //expected to change when implemented 3
        assertParseSuccess(parser, "tag SomeTag", expectedFindTagCommand);

        // Checks for parser auto-capitalization of tags
        assertParseSuccess(parser, "tag someTag", expectedFindTagCommand);

        // Checks for successful Class find
        assertParseSuccess(parser, "class SomeClass", expectedFindClassCommand);

        // Checks for parser auto-capitalization of formClass
        assertParseSuccess(parser, "class someClass", expectedFindClassCommand);
    }

}
