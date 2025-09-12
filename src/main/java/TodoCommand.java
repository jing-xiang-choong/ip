public class TodoCommand implements Command {
    private final String userInput;

    public TodoCommand(String userInput) {
        this.userInput = userInput;
    }
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException{
        String taskDesc = Parser.splitAtCommand(userInput, "todo");
        Task t = new ToDo(taskDesc);
        ui.showAdded(tasks.addTask(t), tasks.size());
        storage.save(tasks.getAll());
    }


    @Override
    public boolean isExit(){
        return false;
    }

}
