package eloise.command;

import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;

/**
 * Represents command that prints the list of task in {@link TaskList}
 *
 *
 */
public class ListCommand implements Command {
    /**
     * Checks if {@link TaskList} is empty, if {@code true} then print null.
     * Otherwise, convert task list to string then {@link Ui} prints list of tasks.
     *
     * @param tasks {@link TaskList} used to retrieve list of tasks to print
     * @param storage {@link Storage} (unused)
     * @param ui {@link Ui} used to display the list of tasks to users
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        ui.showList(tasks.isEmpty() ? null : tasks.toString());
    }

    /**
     * Indicates that program does not terminate program.
     *
     * @return {@code false} since this command does not exit application
     */
    @Override
    public boolean isExit(){
        return false;
    }
}
