import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Eloise {

    private static final String line  = "_".repeat(50);
    private static final List<Task> items = new ArrayList<>();

    private static void msgBox(String msg) {
        System.out.println(line);
        //check if there is any next line, then add the indent, \\R matches all line endings
        for (String line: msg.split("\\R")) {
            System.out.println(" " + line);
        }
        System.out.println(line);

    }
    public static void main(String[] args) {
        msgBox("""
                Hello, I'm Eloise! 
                What can I do for you today?""");

        //checks for input
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String userInput = sc.nextLine().trim();
            //gives the actual input
            if (userInput.equalsIgnoreCase("bye")) {
                msgBox("Bye! Hope to see you again!");
                break;
            } else if (userInput.equalsIgnoreCase("list")) {
                if (items.isEmpty()) {
                    msgBox("No items added yet.");
                } else {
                    StringBuilder list = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        list.append(i + 1).append(". ")
                                .append(items.get(i))
                                .append(System.lineSeparator());
                        //appends number with item then next line, using lineSeparator is better than \n
                    }
                    msgBox(list.toString().stripTrailing());
                }
            } else if (userInput.toLowerCase().startsWith("mark")) {
                handleMark(userInput, true);
            } else if (userInput.toLowerCase().startsWith("unmark")) {
                handleMark(userInput, false);
            } else if (!userInput.isBlank()) {
                items.add(new Task(userInput));
                msgBox("added: " + userInput);
            }

        }

    }

    public static void handleMark(String userInput, boolean mark) {
        String [] parts = userInput.split("\\s+", 2);
        //splits the string into command and task no.

        if (parts.length < 2) {
            msgBox("Please provide a task number.");
            return;
        }
        try {
            int index = Integer.parseInt(parts[1]);
            if (index < 1 || index > items.size()) {
                msgBox("Task number out of range: " + index);
                return;
            }
            Task t = items.get(index-1);
            if (mark) {
                t.mark();
                msgBox("Nice! I've marked this task as done:\n " + t);
            } else {
                t.unmark();
                msgBox("OK, I've marked this task as not done yet:\n " + t);
            }
        } catch (NumberFormatException e) {
            msgBox("Not a valid task number: " + parts[1]);
        }
    }
}
