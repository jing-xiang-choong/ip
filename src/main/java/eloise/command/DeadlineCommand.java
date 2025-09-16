package eloise.command;

import eloise.task.TaskList;
import eloise.task.Task;
import eloise.task.Deadline;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;
import eloise.exception.MissingArgumentException;
import eloise.exception.EmptyDescriptionException;
import eloise.parser.Parser;
import eloise.parser.DateParser;

/**
 * Represents command that adds a {@link Deadline} task to task list.
 * <p>
 * Users are expected to use the following format to enter a {@link Deadline} task:
 * deadline <task description> /by <date and time>
 */
public record DeadlineCommand(String userInput) implements Command {

    /**
     * Parses the task description and deadline, adds it to {@link TaskList}.
     * Task is then saved to {@link Storage}, then {@link Ui} prints a confirmation
     * message to the user.
     *
     * @param tasks   {@link TaskList} used to add new deadline task to
     * @param storage {@link Storage} used to persist updated task list
     * @param ui      {@link Ui} used to display successful entry or potential error messages
     * @throws EloiseException if input is invalid or if there are missing arguments.
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException {
        String taskDesc = Parser.splitAtCommand(userInput, "deadline");
        String[] parts = taskDesc.split("/by", 2);
        if (parts.length < 2) {
            throw new MissingArgumentException("'/by <when>'", "/by 2/9/2025");
        }
        String task = parts[0].trim();
        String date = parts[1].trim();
        if (task.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }
        if (date.isEmpty()) {
            throw new MissingArgumentException("'/by <when>'", "/by 2/9/2025");
        }

        DateParser.Result r = DateParser.parser(date);

        Task t = new Deadline(task, r.dateTime, r.hasTime);
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
