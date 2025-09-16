package eloise.command;

import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;

/**
 * User command that will executed by Eloise
 * Each {@code Command} encapsulates the action to be performed on the current
 * TaskList, Storage and UI. Each implementation of this interface defines what
 * specific command are carried out.
 */
public interface Command {

    /**
     * Executes command given the tasklist, storage and ui, responsible for performing
     * command action
     * @param tasks {@link TaskList} contains all current tasks
     * @param storage {@link Storage} used to load and store tasks data
     * @param ui {@link Ui} used to interact with users through messages
     * @throws EloiseException
     */
    void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException;

    /**
     * Determines whether this command signals the program to exit
     *
     * @return {@code true} if this command should terminate the program
     *         {@code false} otherwise.
     */
    boolean isExit();
}
