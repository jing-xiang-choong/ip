package eloise.parser;

import eloise.exception.EloiseException;
import eloise.exception.EmptyDescriptionException;
import eloise.exception.UnknownCommandException;
import eloise.command.*;



public class Parser {

    public static String splitAtCommand(String userInput, String cmd) throws EmptyDescriptionException{
        String [] parts = userInput.split("\\s+", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EmptyDescriptionException(cmd);
        }
        return parts[1].trim();
    }


    public static Command parse(String userInput) throws EloiseException{
        String lower = userInput.toLowerCase();
        //gives the actual input
        if (lower.equals("bye")) {
            return new ByeCommand();
        }

        if (lower.equals("list")) {
            return new ListCommand();
        }

        if (lower.startsWith("mark")) {
            return new MarkCommand(userInput, true);
        }

        if (lower.startsWith("unmark")) {
            return new MarkCommand(userInput, false);
        }

        if (lower.startsWith("todo")) {
            return new TodoCommand(userInput);
        }

        if (lower.startsWith("deadline")) {
            return new DeadlineCommand(userInput);
        }

        if (lower.startsWith("event")) {
            return new EventCommand(userInput);
        }

        if (lower.startsWith("delete")) {
            return new DeleteCommand(userInput);
        }

        if (lower.startsWith("find")) {
            return new FindCommand(userInput);
        }

        throw new UnknownCommandException(userInput);

    }
}
