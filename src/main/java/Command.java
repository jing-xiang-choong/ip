public interface Command {
    void execute(TaskList tasks, Storage storage, Ui ui) throws EloiseException;

    boolean isExit();
}
