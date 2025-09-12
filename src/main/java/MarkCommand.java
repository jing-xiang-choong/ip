public class MarkCommand implements Command {
    private final String userInput;
    private final boolean mark;

    public MarkCommand(String userInput, boolean mark) {
        this.userInput = userInput;
        this.mark = mark;
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
            Task t = mark ? tasks.mark(index) : tasks.unmark(index);
            storage.save(tasks.getAll());
            ui.showMark(t, mark);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException("Not a valid task number", tasks.size());
        }
    }

    @Override
    public boolean isExit(){
        return false;
    }
}
