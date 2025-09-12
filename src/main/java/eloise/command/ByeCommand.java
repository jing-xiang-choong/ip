package eloise.command;

import eloise.task.TaskList;
import eloise.ui.Ui;
import eloise.storage.Storage;

public class ByeCommand implements Command{
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        ui.showExit();
        System.exit(0);
    }

    @Override
    public boolean isExit(){
        return true;
    }
}
