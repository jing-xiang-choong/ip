import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Eloise {

    private static final String line  = "_".repeat(50);
    private static final List<String> items = new ArrayList<>();

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
                if(items.isEmpty()) {
                    msgBox("No items added yet.");
                } else {
                    StringBuilder list = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        list.append(i+1).append(". ")
                                .append(items.get(i))
                                .append(System.lineSeparator());
                        //appends number with item then next line, using lineSeparator is better than \n
                    }
                    msgBox(list.toString().stripTrailing());
                }
            } else if (!userInput.isBlank()) {
                items.add(userInput);
                msgBox("added: " + userInput);
            }

        }

    }
}
