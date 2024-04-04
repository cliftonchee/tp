package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;

import seedu.address.commons.core.date.Date;
import seedu.address.logic.commands.FindApptCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;
import seedu.address.model.appointment.Time;
import seedu.address.model.patient.Nric;

/**
 * Parses input arguments and creates a new FindApptCommand object
 */
public class FindApptCommandParser implements Parser<FindApptCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindApptCommand
     * and returns a FindApptCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindApptCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindApptCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_NRIC,
                PREFIX_DATE, PREFIX_START_TIME, PREFIX_END_TIME);

        if (argMultimap.anyPrefixesPresent(PREFIX_NAME, PREFIX_END_TIME, PREFIX_DOB, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_TAG, PREFIX_NOTE) || argMultimap.anyNewPrefixesPresent()
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindApptCommand.MESSAGE_USAGE));
        }

        Optional<Nric> nricFilter = Optional.empty();
        Optional<Date> dateFilter = Optional.empty();
        Optional<Time> timeFilter = Optional.empty();

        if (argMultimap.getValue(PREFIX_NRIC).isPresent()) {
            nricFilter = Optional.of(ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC).get()));
        }

        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            dateFilter = Optional.of(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }

        if (argMultimap.getValue(PREFIX_START_TIME).isPresent()) {
            timeFilter = Optional.of(ParserUtil.parseTime(argMultimap.getValue(PREFIX_START_TIME).get()));
        }

        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                nricFilter, dateFilter, timeFilter);

        return new FindApptCommand(predicate);
    }
}