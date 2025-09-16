package eloise.command;

import eloise.task.TaskList;
import eloise.task.Task;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;
import eloise.exception.InvalidIndexException;

/**
 * Represents command to mark or unmark a task in {@link TaskList}
 *
 * Users are expected to use the following format to isMarked or unmark tasks:
 *     mark 1, unmark 2
 *
 * @param userInput the raw command string input by users
 * @param isMarked {@code true} to isMarked specified task done
 *             {@code false} to unmark it
 */
public record MarkCommand(String userInput, boolean isMarked) implements Command {
    
    /**
     * Parses the task description and task number, updates the task status in {@link TaskList}.
     * Task is then saved to {@link Storage}, then {@link Ui} prints a confirmation
     * message to the user.
     *
     * @param tasks   {@link TaskList} contains all tasks
     * @param storage {@link Storage} used to persist updated task list
     * @param ui      {@link Ui} used to display successful entry or potential error messages
     * @throws EloiseException if task number is invalid or missing.
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException {
        String[] parts = userInput.split("\\s+", 2);
        //splits the string into command and task no.

        if (parts.length < 2) {
            throw new InvalidIndexException("Missing task number!");
        }

        try {
            int index = Integer.parseInt(parts[1]);
            Task t = isMarked ? tasks.mark(index) : tasks.unmark(index);
            storage.save(tasks.getAll());
            ui.showMark(t, isMarked);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException("Not a valid task number", tasks.size());
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
