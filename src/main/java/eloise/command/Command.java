package eloise.command;

import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;

public interface Command {
    void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException;

    boolean isExit();
}
