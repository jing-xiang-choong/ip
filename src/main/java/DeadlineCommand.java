public class DeadlineCommand implements Command{
    private final String userInput;

    public DeadlineCommand(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException{
        String taskDesc = Parser.splitAtCommand(userInput, "deadline");
        String[] parts = taskDesc.split("/by", 2);
        if (parts.length < 2) {
            throw new MissingArgumentException("'/by <when>'", "/by 2/9/2025");
        }
        String task = parts[0].trim();
        String date =  parts[1].trim();
        if (task.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }
        if (date.isEmpty()) {
            throw new MissingArgumentException("'/by <when>'", "/by 2/9/2025");
        }

        DateParser.Result r = DateParser.parser(date);

        Task t = new Deadline(task, r.dateTime, r.hasTime);
        ui.showAdded(tasks.addTask(t), tasks.size());
        storage.save(tasks.getAll());
    }

    @Override
    public boolean isExit(){
        return false;
    }
}
