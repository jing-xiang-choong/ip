import java.util.Scanner;


public class Eloise {

    private static final String line  = "_".repeat(50);
    private static final TaskList tasks = new TaskList();
    private static final Storage storage = new Storage();


    /**
     * Entry point for program. Greets users, loads previously stored tasks,
     * continuously reads user inputs until exit.
     * @param args (not used)
     */
    public static void main(String[] args) {
        msgBox("""
                Hello, I'm Eloise! Your favourite productivity bot!
                For Todo: enter "todo <task>"
                For Deadline: enter "deadline <task> /by <date/time>"
                For Event: enter "event <task> /from <date/time> /to <date/time>"
                """);

        //checks for input
        Scanner sc = new Scanner(System.in);

        try {
            int added = tasks.addAll(storage.load());
            if (added > 0) {
                msgBox("Loaded " + added + " tasks from your previous session.");
            }
        } catch (EloiseException e) {
            msgBox(e.getMessage());
        }

        while (sc.hasNextLine()) {
            String userInput = sc.nextLine().trim();

            if(userInput.isEmpty()) continue;

            try {
                handleInput(userInput);
            } catch (EloiseException e) {
                msgBox(e.getMessage());
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
            msgBox("Bye! Hope to see you again!");
            System.exit(0);
            return;
        }

        if (lower.equals("list")) {
            if (tasks.isEmpty()) {
                msgBox("No items added yet.");
            } else {
                msgBox(tasks.toString());
            }
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
        addedMsg(tasks.addTask(t));
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
        addedMsg(tasks.addTask(t));
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
        addedMsg(tasks.addTask(t));
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
            msgBox((mark
                    ? "Nice! I've marked this task as done:\n "
                    : "OK, I've marked this task as not done yet:\n ") + t);
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
            removedMsg(tasks.delete(index));
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

    /**
     * Prints message that is surrounded by horizontal lines,
     * indents each line of text for better readability
     * @param msg message to be displayed
     */
    private static void msgBox(String msg) {
        System.out.println(line);
        //check if there is any next line, then add the indent, \\R matches all line endings
        for (String line: msg.split("\\R")) {
            System.out.println(" " + line);
        }
        System.out.println(line);

    }

    /**
     * Prints confirmation message after task has been added,
     * shows added task and updated list size
     * @param t task that is added
     */
    private static void addedMsg(Task t) {
        msgBox("Got it. I've added this task:\n"
                + " " + t + "\n"
                + "Now you have " + tasks.size() + " tasks in the list." );
    }

    /**
     * Prints confirmation message after task has been deleted,
     * shows removed task and updated list size
     * @param t task that is removed
     */
    private static void removedMsg(Task t) {
        msgBox("No problem! I have removed:\n"
                + " " + t + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.");
    }
}
