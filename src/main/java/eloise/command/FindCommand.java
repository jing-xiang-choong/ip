package eloise.command;


import eloise.exception.EloiseException;
import eloise.parser.Parser;
import eloise.storage.Storage;
import eloise.task.TaskList;
import eloise.ui.Ui;

public class FindCommand implements Command {
    private final String userInput;

    public FindCommand(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException {
        String keyword = Parser.splitAtCommand(userInput, "find");
        TaskList matches = tasks.findTasks(keyword);
        ui.showMatches(matches);
    }

    @Override
    public boolean isExit(){
        return false;
    }
}
