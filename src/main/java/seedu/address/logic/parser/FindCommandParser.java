package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FormClassMatchesPredicate;
import seedu.address.model.person.IdMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagMatchesPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+", 2);
        if (nameKeywords.length <= 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        String modeToken = nameKeywords[0].toLowerCase();
        nameKeywords = Arrays.copyOfRange(nameKeywords, 1, nameKeywords.length);

        switch (modeToken) {
        case "name":
            String[] keywords = nameKeywords[0].split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "id":
            return new FindCommand(new IdMatchesPredicate(ParserUtil.parseStudentId(nameKeywords[0])));
        case "tag":
            return new FindCommand(new TagMatchesPredicate(ParserUtil.parseTag(nameKeywords[0])));
        case "class":
            return new FindCommand(new FormClassMatchesPredicate(ParserUtil.parseClass(nameKeywords[0])));
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

    }
}
