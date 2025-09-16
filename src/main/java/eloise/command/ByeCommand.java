package eloise.command;

import eloise.exception.EloiseException;
import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;

/**
 * Represents command that terminates the program.
 *
 * When executed, command sends exit message to user,
 * then shuts down application
 */
public class ByeCommand implements Command{

    /**
     * Displays exit message through {@link Ui} then terminate program.
     *
     * @param tasks {@link TaskList} (unused)
     * @param storage {@link Storage} (unused)
     * @param ui {@link Ui} used to display exit message
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        ui.showExit();
        System.exit(0);
    }

    /**
     * Indicates that the program will be terminated.
     *
     * @return {@code true} as this command will terminate the program.
     */
    @Override
    public boolean isExit(){
        return true;
    }
}
