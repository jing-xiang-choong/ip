package eloise.parser;

import eloise.exception.EloiseException;
import eloise.exception.EmptyDescriptionException;
import eloise.exception.UnknownCommandException;
import eloise.command.*;



public class Parser {

    private static final String CMD_BYE = "bye";
    private static final String CMD_LIST = "list";
    private static final String CMD_MARK = "mark";
    private static final String CMD_UNMARK = "unmark";
    private static final String CMD_TODO = "todo";
    private static final String CMD_DEADLINE = "deadline";
    private static final String CMD_EVENT = "event";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_FIND = "find";
    private static final String CMD_SORT = "sort";


    /**
     * Splits the raw command string by users into command and its description.
     *
     * @param userInput raw command string input by users
     * @param cmd command that is used to split the user input
     * @return remaining description after command
     * @throws EmptyDescriptionException if description is missing
     */
    public static String splitAtCommand(String userInput, String cmd) throws EmptyDescriptionException{
        String [] parts = userInput.split("\\s+", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EmptyDescriptionException(cmd);
        }
        return parts[1].trim();
    }


    /**
     * Parses the user's command, and matches with the respective commands.
     * Executes the command that matches with the user input.
     *
     * @param userInput raw command string input by user
     * @return specific command to execute depending on user input
     * @throws EloiseException if command is invalid
     */
    public static Command parse(String userInput) throws EloiseException {
        assert userInput != null : "userInput should not be null";
        String lower = userInput.toLowerCase();
        //gives the actual input

        if (lower.equals(CMD_BYE)) {
            return new ByeCommand();
        } else if (lower.equals(CMD_LIST)) {
            return new ListCommand();
        } else if (lower.startsWith(CMD_MARK)) {
            return new MarkCommand(userInput, true);
        } else if (lower.startsWith(CMD_UNMARK)) {
            return new MarkCommand(userInput, false);
        } else if (lower.startsWith(CMD_TODO)) {
            return new TodoCommand(userInput);
        } else if (lower.startsWith(CMD_DEADLINE)) {
            return new DeadlineCommand(userInput);
        } else if (lower.startsWith(CMD_EVENT)) {
            return new EventCommand(userInput);
        } else if (lower.startsWith(CMD_DELETE)) {
            return new DeleteCommand(userInput);
        } else if (lower.startsWith(CMD_FIND)) {
            return new FindCommand(userInput);
        } else if (lower.startsWith(CMD_SORT)) {
            String[] parts = userInput.split("\\s+", 2);
            String criteria = parts.length > 1 ? parts[1] : "desc";
            return new SortCommand(criteria);
        }

        throw new UnknownCommandException(userInput);
    }
}
