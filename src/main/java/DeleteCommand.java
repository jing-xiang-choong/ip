public class DeleteCommand implements Command {
    private final String userInput;

    public DeleteCommand(String userInput) {
        this.userInput = userInput;
    }
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException{
        String taskIdx = Parser.splitAtCommand(userInput, "delete");

        try {
            int index = Integer.parseInt(taskIdx);
            ui.showRemoved(tasks.delete(index), tasks.size());
            storage.save(tasks.getAll());

        } catch (NumberFormatException e) {
            throw new InvalidIndexException("Not a valid task number", tasks.size());
        }
    }

    @Override
    public boolean isExit(){
        return false;
    }
}
