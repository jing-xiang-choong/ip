package eloise.command;

import eloise.task.TaskList;
import eloise.task.Task;
import eloise.ui.Ui;
import eloise.storage.Storage;
import eloise.exception.EloiseException;
import eloise.exception.InvalidIndexException;


public class MarkCommand implements Command {
    private final String userInput;
    private final boolean isMarked;

    public MarkCommand(String userInput, boolean isMarked) {
        this.userInput = userInput;
        this.isMarked = isMarked;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException{
        String [] parts = userInput.split("\\s+", 2);
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
    public boolean isExit(){
        return false;
    }
}
