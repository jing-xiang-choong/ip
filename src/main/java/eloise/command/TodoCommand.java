package eloise.command;

import eloise.task.Deadline;
import eloise.task.TaskList;
import eloise.task.Task;
import eloise.task.ToDo;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;
import eloise.parser.Parser;

/**
 * Represents command that adds a {@link ToDo} to task list.
 * <p>
 * Users are expected to use the following format to enter a {@link ToDo} task:
 *     todo <task description>
 */
public record TodoCommand(String userInput) implements Command {

    /**
     * Parses the task description, adds it to {@link TaskList}.
     * Task is then saved to {@link Storage}, then {@link Ui} prints a confirmation
     * message to the user.
     *
     * @param tasks   {@link TaskList} used to add new todo task to
     * @param storage {@link Storage} used to persist updated task list
     * @param ui      {@link Ui} used to display successful entry or potential error messages
     * @throws EloiseException if input is invalid or if there are missing arguments.
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException {
        String taskDesc = Parser.splitAtCommand(userInput, "todo");
        Task t = new ToDo(taskDesc);
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
