package eloise.command;

import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;

public class SortCommand implements Command {
    private final String criteria;

    public SortCommand(String criteria) {
        this.criteria = criteria == null ? "desc" : criteria.toLowerCase();
    }

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

    @Override
    public boolean isExit() {
        return false;
    }

}
