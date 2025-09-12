public class Eloise {

    private static final TaskList tasks = new TaskList();
    private static final Storage storage = new Storage();
    private static final Ui ui = new Ui();


    /**
     * Entry point for program. Greets users, loads previously stored tasks,
     * continuously reads user inputs until exit.
     * @param args (not used)
     */
    public static void main(String[] args) {
        ui.showWelcome();

        try {
            int added = tasks.addAll(storage.load());
            if (added > 0) {
                ui.showMessage("Loaded " + added + " tasks from your previous session.");
            }
        } catch (EloiseException e) {
            ui.showMessage(e.getMessage());
        }

        while (true) {
            String userInput = ui.readInput();
            if (userInput == null) break;
            if (userInput.isEmpty()) continue;

            try {
                Command c = Parser.parse(userInput);
                c.execute(tasks, storage, ui);
            } catch (EloiseException e) {
                ui.showMessage(e.getMessage());
            }
        }
    }

}
