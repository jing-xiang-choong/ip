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

//    private static void requireEntry(String s, String error) {
//        if (s == null || s.trim().isEmpty()) {
//            throw new IllegalArgumentException(error);
//        }
//    }

    private static void addedMsg(Task t) {
        msgBox("Got it. I've added this task:\n"
                + " " + t + "\n"
                + "Now you have " + items.size() + " tasks in the list." );
    }

    private static void removedMsg(Task t) {
        msgBox("No problem! I have removed:\n"
                + " " + t + "\n"
                + "Now you have " + items.size() + " tasks in the list." );
    }


    public static void main(String[] args) {
        msgBox("""
                Hello, I'm Eloise! Your favourite productivity bot!
                For Todo: enter "todo <task>"
                For Deadline: enter "deadline <task> /by <date/time>"
                For Event: enter "event <task> /from <date/time> /to <date/time>"
                """);

        //checks for input
        Scanner sc = new Scanner(System.in);

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

    //separate out all logic from main

    private static void handleInput(String userInput) throws EloiseException{
        String lower = userInput.toLowerCase();
        //gives the actual input
        if (lower.equals("bye")) {
            msgBox("Bye! Hope to see you again!");
            System.exit(0);
            return;
        }

        if (lower.equals("list")) {
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


    private static void handleToDo(String userInput) throws EloiseException{
        String taskDesc = splitAtCommand(userInput, "todo");
        Task t = new ToDo(taskDesc);
        items.add(t);
        addedMsg(t);
    }


    private static void handleDeadline(String userInput) throws EloiseException{
        String taskDesc = splitAtCommand(userInput, "deadline");
        String[] parts = taskDesc.split("/by", 2);
        if (parts.length < 2) {
            throw new MissingArgumentException("'/by <when>'", "/by Sunday");
        }
        String task = parts[0].trim();
        String date =  parts[1].trim();
        if (task.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }
        if (date.isEmpty()) {
            throw new MissingArgumentException("'/by <when>'", "/by Sunday");
        }
        Task t = new Deadline(task, date);
        items.add(t);
        addedMsg(t);
    }

    private static void handleEvent(String userInput) throws EloiseException {
        String taskDesc = splitAtCommand(userInput, "event");
        String[] splitFrom = taskDesc.split("/from", 2);

        if (splitFrom.length < 2) {
            throw new MissingArgumentException("'/from <start> /to <end>'"
                                                , "/from Sunday 2pm /to 4pm");
        }

        String task = splitFrom[0].trim();
        String rest =  splitFrom[1].trim();

        String[] splitTo = rest.split("/to", 2);
        if (splitTo.length < 2) {
            throw new MissingArgumentException("'/to <end>'"
                                                , "/to 4pm");
        }
        String from = splitTo[0].trim();
        String to = splitTo[1].trim();

        if (task.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }
        if (from.isEmpty()) {
            throw new MissingArgumentException("'/from <start>'"
                                                , "/from 4pm");
        }
        if (to.isEmpty()) {
            throw new MissingArgumentException("'/to <end>'"
                                                , "/to 4pm");
        }

        Task t = new Event(task, from, to);
        items.add(t);
        addedMsg(t);
    }

    private static void handleMark(String userInput, boolean mark) throws EloiseException{
        String [] parts = userInput.split("\\s+", 2);
        //splits the string into command and task no.

        if (parts.length < 2) {
            throw new InvalidIndexException("Missing task number!");
        }

        try {
            int index = Integer.parseInt(parts[1]);
            if (index < 1 || index > items.size()) {
                throw new InvalidIndexException("Task number out of range", items.size());
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
            throw new InvalidIndexException("Not a valid task number", items.size());
        }
    }

    private static void handleDelete(String userInput) throws EloiseException{
        String taskIdx = splitAtCommand(userInput, "delete");

        try {
            int index = Integer.parseInt(taskIdx) - 1;
            if (index < 0 || index >= items.size()) {
                throw new InvalidIndexException("Task number out of range", items.size());
            }

            Task removed = items.remove(index);
            removedMsg(removed);

        } catch (NumberFormatException e) {
            throw new InvalidIndexException("Not a valid task number", items.size());
        }
    }

    private static String splitAtCommand(String userInput, String cmd) throws EmptyDescriptionException{
        String [] parts = userInput.split("\\s+", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EmptyDescriptionException(cmd);
        }
        return parts[1].trim();
    }
}
