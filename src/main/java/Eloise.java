import java.util.Scanner;

public class Eloise {

    private static final String line  = "_".repeat(50);

    private static void msgBox(String msg) {
        System.out.println(line);
        System.out.println(" " + msg);
        System.out.println(line);

    }
    public static void main(String[] args) {
        msgBox("Hello, I'm Eloise!\n What can I do for you today?");

        //checks for input
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String userInput = sc.nextLine(); //gives the actual input
            if (userInput.equals("bye")) {
                msgBox("Bye! Hope to see you again!");
                break;
            }

            msgBox(userInput);
        }

    }
}
