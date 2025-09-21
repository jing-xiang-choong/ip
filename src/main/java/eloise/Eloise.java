package eloise;

import eloise.command.Command;
import eloise.parser.Parser;
import eloise.storage.Storage;
import eloise.task.TaskList;
import eloise.ui.GuiUi;
import eloise.ui.Ui;
import eloise.exception.EloiseException;

public class Eloise {

    private final TaskList tasks;
    private final Storage storage;
//    private static final Ui ui = new Ui();


    public Eloise() {
        this.tasks = new TaskList();
        this.storage = new Storage();

        try {
            int added = tasks.addAll(storage.load());

        } catch (EloiseException e) {
            // starts fresh with new empty list
        }
    }

    public String getResponse(String userInput) {

        try {
            Command c = Parser.parse(userInput);
            GuiUi tempUi = new GuiUi();
            c.execute(tasks, storage, tempUi);
            return tempUi.getOutput();
        } catch (EloiseException e) {
            return e.getMessage();
        }
    }


    /**
     * Entry point for program. Greets users, loads previously stored tasks,
     * continuously reads user inputs until exit.
     * @param args (not used)
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Eloise eloise = new Eloise();

        ui.showWelcome();

        while (true) {
            String userInput = ui.readInput();
            if (userInput == null) break;
            if (userInput.isEmpty()) continue;

            try {
                Command c = Parser.parse(userInput);
                c.execute(eloise.tasks, eloise.storage, ui);
            } catch (EloiseException e) {
                ui.showMessage(e.getMessage());
            }
        }
    }

}
