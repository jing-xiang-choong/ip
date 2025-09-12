public class ListCommand implements Command{
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        ui.showList(tasks.isEmpty() ? null : tasks.toString());
    }

    @Override
    public boolean isExit(){
        return false;
    }
}
