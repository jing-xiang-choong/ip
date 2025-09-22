package eloise.command;

import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;

/// Used ChatGPT to generate JavaDoc Comments for SortCommand class
/**
 * Represents a command that sorts tasks in the {@link TaskList}.
 * <p>
 * Supports sorting by description (alphabetical order) or by date
 * (chronological order of deadlines/events). Tasks without dates are placed
 * after tasks with dates when sorting by date.
 */
public class SortCommand implements Command {
    /** Sorting criteria: either "desc" (by description) or "date". */
    private final String criteria;

    /**
     * Creates a {@code SortCommand} with the given criteria.
     * <p>
     * Defaults to sorting by description if {@code criteria} is null.
     *
     * @param criteria the sort criteria, either "desc" or "date"
     */
    public SortCommand(String criteria) {
        this.criteria = criteria == null ? "desc" : criteria.toLowerCase();
    }

    /**
     * Executes the sort operation on the given {@link TaskList}.
     * <p>
     * Sorts tasks based on the provided criteria, updates the UI to display
     * confirmation and the sorted list, and saves the updated list to storage.
     *
     * @param tasks   the {@link TaskList} to be sorted
     * @param storage the {@link Storage} used to persist the sorted tasks
     * @param ui      the {@link Ui} used to display messages to the user
     * @throws EloiseException if sorting fails (unlikely for this command)
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException {
        switch (criteria) {
        case "date":
            tasks.sortByDate();
            ui.showMessage("Tasks sorted by date.");
            break;
        case "desc":
        default:
            tasks.sortByDescription();
            ui.showMessage("Tasks sorted by description.");
            break;
        }
        storage.save(tasks.getAll());
        ui.showList(tasks.toString());
    }

    /**
     * Indicates that executing this command does not exit the program.
     *
     * @return {@code false} always, since this command does not terminate the app
     */
    @Override
    public boolean isExit() {
        return false;
    }

}
