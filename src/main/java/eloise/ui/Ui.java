package eloise.ui;

import java.util.Scanner;
import java.io.PrintStream;
import eloise.task.Task;

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

    public void showWelcome() {
        box("""
            Hello, I'm Eloise! Your favourite productivity bot!
            For Todo: enter "todo <task>"
            For Deadline: enter "deadline <task> /by <date/time>"
            For Event: enter "event <task> /from <date/time> /to <date/time>"
            """);
    }

    public void showExit() {
        box("Bye! Hope to see you again!");
    }

    public void showList(String tasklist) {
        if (tasklist == null || tasklist.isBlank()) {
            box("No items added yet.");
        } else {
            box(tasklist.stripTrailing());
        }
    }

    public void showAdded(Task t, int listSize) {
        box("Got it. I've added this task:\n"
                + " " + t + "\n"
                + "Now you have " + listSize + " tasks in the list." );
    }

    public void showRemoved(Task t, int listSize) {
        box("No problem. I've removed this task:\n"
                + " " + t + "\n"
                + "Now you have " + listSize + " tasks in the list." );
    }

    public void showMark(Task t, boolean isMarked) {
        box((isMarked
                ? "Nice! I've marked this task as done:\n "
                : "OK, I've marked this task as not done yet:\n ") + t);
    }

    public void showMessage(String msg){
        box(msg);
    }

    private void box(String msg) {
        out.println(line);
        for (String line: msg.split("\\R")) {
            out.println(" " + line);
        }
        out.println(line);
    }
}
