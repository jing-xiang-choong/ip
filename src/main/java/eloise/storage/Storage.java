package eloise.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import eloise.task.Task;
import eloise.task.ToDo;
import eloise.task.Event;
import eloise.task.Deadline;
import eloise.parser.DateParser;




public final class Storage {
    private final File dataFile = new File("data/eloise.txt");


    /**
     * Loads tasks from {@code dataFile}.
     * <p>Each line is parsed via {@link #parseStrLenient(String)} into a {@link Task}.
     * Malformed lines are skipped. If the file does not exist, an empty list is returned.</p>
     *
     * @return a mutable list of loaded tasks; never {@code null}
     */
    public List<Task> load() {
        //create method to check if data file is even created in the first place, if not create it
        checkDataDir();

        List<Task> tasks = new ArrayList<>();

        if (!dataFile.exists()) {
            return tasks;
        }

        try (Scanner sc = new Scanner(dataFile)) {
            while (sc.hasNextLine()) {
                String taskStr = sc.nextLine();
                Task t = parseStrLenient(taskStr);
                if (t != null) tasks.add(t);
            }
        } catch (FileNotFoundException e) {
            //files does not exist, treated like first run
        }

        assert tasks != null : "TaskList should not be null after loading";

        return tasks;

    }

    /**
     * Writes all tasks to {@code dataFile}, overwriting existing content.
     * <p>Each task is converted to String via {@link Task#toFileString()}.
     * Ensures the parent directory exists before writing. If an I/O error occurs, the
     * error is logged and the method returns without persisting changes.</p>
     *
     * @param tasks the list of tasks to persist; must not be {@code null}
     */
    public void save(List<Task> tasks ) {
        //check for datafile directory, if not create it
        checkDataDir();

        // for each task in arraylist, write the task into its respective string
        try (FileWriter fw = new FileWriter(dataFile, false)) {
            for (Task t : tasks) {
               fw.write(t.toFileString());
               fw.write(System.lineSeparator());
           }
        } catch (IOException e) {
            System.err.println("Failed to save: " + e.getMessage());
        }
    }

    /**
     * Check if parent directory eg: data has been created
     * Else, create necessary directories
     */
    private void checkDataDir() {
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    private Task parseStrLenient(String taskStr) {
        try {
            String[] parts = taskStr.split("\\s*\\|\\s*");

            String taskType = parts[0];
            boolean isDone = "1".equals(parts[1]);
            String desc = parts[2];

            Task t;

            switch (taskType) {
                case "T":
                    t = new ToDo(desc);
                    break;
                case "D":
                    String by = parts[3];
                    DateParser.Result r = DateParser.parser(by);
                    t = new Deadline(desc, r.dateTime, r.hasTime);
                    break;
                case "E":
                    String from = parts[3];
                    String to = parts[4];
                    DateParser.Result r1 = DateParser.parser(from);
                    DateParser.Result r2 = DateParser.parser(to);
                    t = new Event(desc, r1.dateTime, r2.dateTime, r1.hasTime, r2.hasTime);
                    break;
                default:
                    System.err.println("I don't know this task type: " + taskType);
                    return null;
            }
            if (isDone) {
                t.mark();
            }
            return t;
        } catch (Exception e) {
            System.err.println("Skipping corrupt line: " + taskStr);
            return null;
        }
    }




}
