package eloise.command;

import eloise.task.Deadline;
import eloise.task.TaskList;
import eloise.task.Task;
import eloise.task.Event;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;
import eloise.exception.MissingArgumentException;
import eloise.exception.EmptyDescriptionException;
import eloise.parser.Parser;
import eloise.parser.DateParser;

/**
 * Represents command that adds a {@link Event} task to task list.
 * <p>
 * Users are expected to use the following format to enter a {@link Event} task:
 *     deadline <task description> /from <date and time> /to <date and time>
 */
public record EventCommand(String userInput) implements Command {

    /**
     * Parses the task description, start and end time, adds it to {@link TaskList}.
     * Task is then saved to {@link Storage}, then {@link Ui} prints a confirmation
     * message to the user.
     *
     * @param tasks   {@link TaskList} used to add new event task to
     * @param storage {@link Storage} used to persist updated task list
     * @param ui      {@link Ui} used to display successful entry or potential error messages
     * @throws EloiseException if input is invalid or if there are missing arguments.
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException {
        String taskDesc = Parser.splitAtCommand(userInput, "event");
        String[] splitFrom = taskDesc.split("/from", 2);

        if (splitFrom.length < 2) {
            throw new MissingArgumentException("'/from <start> /to <end>'"
                    , "/from 2/9/2025 1800 /to 2/9/2025 1900");
        }

        String task = splitFrom[0].trim();
        String rest = splitFrom[1].trim();

        String[] splitTo = rest.split("/to", 2);
        if (splitTo.length < 2) {
            throw new MissingArgumentException("'/to <end>'"
                    , "/to 2/9/2025 1900");
        }
        String from = splitTo[0].trim();
        String to = splitTo[1].trim();

        if (task.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }
        if (from.isEmpty()) {
            throw new MissingArgumentException("'/from <start>'"
                    , "/from 2/9/2025 1800");
        }
        if (to.isEmpty()) {
            throw new MissingArgumentException("'/to <end>'"
                    , "/to 2/9/2025 1900");
        }

        DateParser.Result r1 = DateParser.parser(from);
        DateParser.Result r2 = DateParser.parser(to);

        Task t = new Event(task, r1.dateTime, r2.dateTime, r1.hasTime, r2.hasTime);
        ui.showAdded(tasks.addTask(t), tasks.size());
        storage.save(tasks.getAll());

    }

    /**
     * Indicates that program does not terminate program.
     *
     * @return {@code false} since this command does not exit application
     */
    @Override
    public boolean isExit() {
        return false;
    }

}
