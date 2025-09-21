package eloise.ui;

import java.util.Scanner;
import java.io.PrintStream;
import eloise.task.Task;
import eloise.task.TaskList;

public class Ui {
    private static final String line  = "_".repeat(50);
    private final Scanner in;
    private final PrintStream out;

    public Ui() {
        this(new Scanner(System.in), System.out);
    }

    public Ui(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public String readInput() {
        if (!in.hasNextLine()) {
            return null;
        }
        String s = in.nextLine();
        return s == null ? null : s.trim();
    }

    /**
     * Displays welcome message when program is running
     */
    public void showWelcome() {
        showMessage("""
            Hello, I'm Eloise! Your favourite productivity bot!
            For Todo: enter "todo <task>"
            For Deadline: enter "deadline <task> /by <date/time>"
            For Event: enter "event <task> /from <date/time> /to <date/time>"
            """);
    }

    /**
     * Display exit message when program is terminated
     */
    public void showExit() {
        showMessage("Bye! Hope to see you again!");

    }

    public void showList(String tasklist) {
        if (tasklist == null || tasklist.isBlank()) {
            showMessage("No items added yet.");
        } else {
            showMessage(tasklist.stripTrailing());
        }
    }

    /**
     * Display confirmation message for added tasks.
     * Message contains added task and current list size.
     * @param t the task that was added
     * @param listSize current task list size after adding task
     */
    public void showAdded(Task t, int listSize) {
        showMessage("Got it. I've added this task:\n"
                + " " + t + "\n"
                + "Now you have " + listSize + " tasks in the list." );
    }

    /**
     * Displays confirmation message for removed tasks.
     * Message contains removed task and current list size.
     *
     * @param t the task that was removed
     * @param listSize current task list size after removing task
     */
    public void showRemoved(Task t, int listSize) {
        showMessage("No problem. I've removed this task:\n"
                + " " + t + "\n"
                + "Now you have " + listSize + " tasks in the list." );
    }


    /**
     * Display confirmation message after marking or unmarking tasks.
     * Message displayed differs, depending on {@code isMarked}.
     * @param t task that is being marked
     * @param isMarked boolean to determine what message to display
     */

    public void showMark(Task t, boolean isMarked) {
        showMessage((isMarked
                ? "Nice! I've marked this task as done:\n "
                : "OK, I've marked this task as not done yet:\n ") + t);
    }

    /**
     * General message box to display messages to user
     * @param msg messages to be displayed to user
     */
    public void showMessage(String msg){
        out.println(line);
        for (String line: msg.split("\\R")) {
            out.println(" " + line);
        }
        out.println(line);
    }


    public void showMatches(TaskList matches) {
        if (matches.isEmpty()) {
            showMessage("No matching items found.");
        } else {
            showMessage("Here are the matching tasks in your list:\n"
                    + matches.toString().stripTrailing());
        }
    }

    /**
     * helper function to format messages
     * @param msg message to be formatted and displayed
     */
    private void box(String msg) {
        out.println(line);
        for (String line: msg.split("\\R")) {
            out.println(" " + line);
        }
        out.println(line);
    }
}
