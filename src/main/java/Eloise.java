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
                handleInput(userInput);
            } catch (EloiseException e) {
                ui.showMessage(e.getMessage());
            }
        }
    }


    /**
     * Parses command and executes it
     * @param userInput raw command entered by users.
     * @throws EloiseException if command is unknown or malformed
     */
    private static void handleInput(String userInput) throws EloiseException{
        String lower = userInput.toLowerCase();
        //gives the actual input
        if (lower.equals("bye")) {
            ui.showExit();
            System.exit(0);
            return;
        }

        if (lower.equals("list")) {
            ui.showList(tasks.isEmpty() ? null : tasks.toString());
            return;
        }

        if (lower.startsWith("mark")) {
            handleMark(userInput, true);
            return;
        }

        if (lower.startsWith("unmark")) {
            handleMark(userInput, false);
            return;
        }

        if (lower.startsWith("todo")) {
            handleToDo(userInput);
            return;
        }

        if (lower.startsWith("deadline")) {
            handleDeadline(userInput);
            return;
        }

        if (lower.startsWith("event")) {
            handleEvent(userInput);
            return;
        }

        if (lower.startsWith("delete")) {
            handleDelete(userInput);
            return;
        }

        throw new UnknownCommandException(userInput);

    }


    /**
     * Handles command by creating a new Todo task
     * @param userInput user inputs that begins with "todo"
     * @throws EloiseException if description is missing
     */
    private static void handleToDo(String userInput) throws EloiseException{
        String taskDesc = splitAtCommand(userInput, "todo");
        Task t = new ToDo(taskDesc);
        ui.showAdded(tasks.addTask(t), tasks.size());
        storage.save(tasks.getAll());
    }


    /**
     * Handles command by creating a new Deadline task
     * @param userInput user inputs that begins with "deadline"
     * @throws EloiseException if description is missing or {@code /by} argument is missing
     */
    private static void handleDeadline(String userInput) throws EloiseException{
        String taskDesc = splitAtCommand(userInput, "deadline");
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

    /**
     * Handles command by creating a new Event task
     * @param userInput user inputs that begins with "event"
     * @throws EloiseException if description, or {@code /from}, or {@code /to}
     * arguments are missing
     */
    private static void handleEvent(String userInput) throws EloiseException {
        String taskDesc = splitAtCommand(userInput, "event");
        String[] splitFrom = taskDesc.split("/from", 2);

        if (splitFrom.length < 2) {
            throw new MissingArgumentException("'/from <start> /to <end>'"
                                                , "/from 2/9/2025 1800 /to 2/9/2025 1900");
        }

        String task = splitFrom[0].trim();
        String rest =  splitFrom[1].trim();

        String[] splitTo = rest.split("/to", 2);
        if (splitTo.length < 2) {
            throw new MissingArgumentException("'/to <end>'"
                                                , "/to 2/9/2025 1900");
        }
        String from = splitTo[0].trim();
        String to = splitTo[1].trim();

        if (task.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }
        if (from.isEmpty()) {
            throw new MissingArgumentException("'/from <start>'"
                                                , "/from 2/9/2025 1800");
        }
        if (to.isEmpty()) {
            throw new MissingArgumentException("'/to <end>'"
                                                , "/to 2/9/2025 1900");
        }

        DateParser.Result r1 = DateParser.parser(from);
        DateParser.Result r2 = DateParser.parser(to);

        Task t = new Event(task, r1.dateTime, r2.dateTime, r1.hasTime, r2.hasTime);
        ui.showAdded(tasks.addTask(t), tasks.size());
        storage.save(tasks.getAll());

    }


    /**
     * Handles mark and unmark commands by updating the
     * completion status of the task at given index.
     * @param userInput user inputs that consist of "mark" or "unmark" (eg: "mark 1")
     * @param mark true to mark as done, false to unmark as not done
     * @throws EloiseException if index is missing or out of range
     */
    private static void handleMark(String userInput, boolean mark) throws EloiseException{
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

    /**
     * Handles command by deleting task at given index.
     * @param userInput user inputs that begins with "delete"
     * @throws EloiseException if index is missing or out of range
     */
    private static void handleDelete(String userInput) throws EloiseException{
        String taskIdx = splitAtCommand(userInput, "delete");

        try {
            int index = Integer.parseInt(taskIdx);
            ui.showRemoved(tasks.delete(index), tasks.size());
            storage.save(tasks.getAll());

        } catch (NumberFormatException e) {
            throw new InvalidIndexException("Not a valid task number", tasks.size());
        }
    }

    /**
     * Splits user input into its command and remaining description.
     * @param userInput full command string
     * @param cmd command keyword (eg. "todo", "deadline")
     * @return remaining description after command
     * @throws EmptyDescriptionException if no description follows after command
     */
    private static String splitAtCommand(String userInput, String cmd) throws EmptyDescriptionException{
        String [] parts = userInput.split("\\s+", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EmptyDescriptionException(cmd);
        }
        return parts[1].trim();
    }

}
