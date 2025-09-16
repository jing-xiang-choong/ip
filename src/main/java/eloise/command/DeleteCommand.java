package eloise.command;

import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;
import eloise.parser.Parser;
import eloise.exception.InvalidIndexException;

/**
 * Represents command that deletes a task from {@link TaskList}
 * <p>
 * Users are expected to use the following format to delete a task:
 * delete <task OneBasedIndex>
 */
public record DeleteCommand(String userInput) implements Command {

    /**
     * Parses task index into integer, uses index to remove task from {@link TaskList}.
     * Updated task list is then saved to {@link Storage}, then {@link Ui} prints a confirmation
     * message to the user.
     *
     * @param tasks   {@link TaskList} contains all current tasks
     * @param storage {@link Storage} used to load and store tasks data
     * @param ui      {@link Ui} used to interact with users through messages
     * @throws EloiseException if task index is invalid or out of range.
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException {
        String taskIdx = Parser.splitAtCommand(userInput, "delete");

        try {
            int index = Integer.parseInt(taskIdx);
            ui.showRemoved(tasks.delete(index), tasks.size());
            storage.save(tasks.getAll());

        } catch (NumberFormatException e) {
            throw new InvalidIndexException("Not a valid task number", tasks.size());
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
